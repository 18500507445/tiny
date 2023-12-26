## mq-rabbit配置

### yml配置
~~~yaml
spring:
  rabbitmq:
    addresses: xx.xxx.xxx.xxx:5672 #注意这里是amqp协议端口5672
    username: root #账号
    password: password #密码
    virtual-host: / #虚拟主机环境
    listener:
      simple:
        retry:
          initial-interval: 1000 #1秒后重试
          enabled: true #启用发布重试
          max-attempts: 3 #传递消息的最大尝试次数
          max-interval: 10000 #尝试的最大时间间隔
          multiplier: 1.0 #应用于先前传递重试时间间隔的乘数

    cache:
      channel:
        size: 5

  rabbitmq2:
    addresses: xx.xxx.xxx.xxx:5672
    username: root
    password: password
    virtual-host: /
~~~

### 说明
目前rabbit-starter（ParentRabbitConfig）可以装配2个数据源，第二个MQ的配置文件需要设置`enable：true`才可以正常加载
~~~java
@Slf4j(topic = "avic-parent-starter ==> ParentRabbitConfig")
@Configuration
@EnableRabbit
public class ParentRabbitConfig {
    /**
     * 主配置
     */
    @Bean(name = "primaryProperties")
    @Primary
    @ConfigurationProperties(prefix = "spring.rabbitmq")
    @ConditionalOnMissingBean
    public RabbitProperties primaryProperties() {
        return new RabbitProperties();
    }

    /**
     * 主配置连接工厂
     */
    @Bean(name = "primaryConnectionFactory")
    public ConnectionFactory connectionFactory(@Qualifier("primaryProperties") RabbitProperties primaryProperties) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        //默认2047
        connectionFactory.getRabbitConnectionFactory().setRequestedChannelMax(primaryProperties.getRequestedChannelMax());
        connectionFactory.setAddresses(primaryProperties.getAddresses());
        connectionFactory.setUsername(primaryProperties.getUsername());
        connectionFactory.setPassword(primaryProperties.getPassword());
        connectionFactory.setVirtualHost(primaryProperties.getVirtualHost());
        if (null != primaryProperties.getCache().getChannel().getSize()) {
            connectionFactory.setChannelCacheSize(primaryProperties.getCache().getChannel().getSize());
        }
        log.warn("装配【primaryProperties】：第一个数据源配置，【primaryConnectionFactory】第一个连接工厂");
        return connectionFactory;
    }

    /**
     * 第二个配置
     */
    @Bean(name = "secondProperties")
    @ConfigurationProperties(prefix = "spring.rabbitmq2")
    @ConditionalOnProperty(prefix = "spring.rabbitmq2", name = "enabled", havingValue = "true")
    public RabbitProperties secondProperties() {
        return new RabbitProperties();
    }

    /**
     * 第二个配置连接工厂
     */
    @Bean(name = "secondConnectionFactory")
    @ConditionalOnProperty(prefix = "spring.rabbitmq2", name = "enabled", havingValue = "true")
    public ConnectionFactory secondConnectionFactory(@Qualifier("secondProperties") RabbitProperties secondProperties) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.getRabbitConnectionFactory().setRequestedChannelMax(secondProperties.getRequestedChannelMax());
        connectionFactory.setAddresses(secondProperties.getAddresses());
        connectionFactory.setUsername(secondProperties.getUsername());
        connectionFactory.setPassword(secondProperties.getPassword());
        connectionFactory.setVirtualHost(secondProperties.getVirtualHost());
        if (null != secondProperties.getCache().getChannel().getSize()) {
            connectionFactory.setChannelCacheSize(secondProperties.getCache().getChannel().getSize());
        }
        log.warn("装配【secondProperties】：第二个数据源配置，【secondConnectionFactory】第二个连接工厂");
        return connectionFactory;
    }
}
~~~


### 模块如何使用
~~~java
@Configuration
@EnableRabbit
public class RabbitConfig {
    /**
     * 生产者设置
     * （1）添加消息转换器
     */
    @Bean
    public RabbitTemplate rabbitTemplate(@Qualifier("primaryConnectionFactory") ConnectionFactory primaryConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(primaryConnectionFactory);
        rabbitTemplate.setMessageConverter(new CustomMessageConverter());
        return rabbitTemplate;
    }

    /**
     * 监听者设置
     * （1）添加消息转换器MessageConverter
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("primaryConnectionFactory") ConnectionFactory primaryConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(primaryConnectionFactory);
        factory.setMessageConverter(new CustomMessageConverter());
        return factory;
    }
}
~~~