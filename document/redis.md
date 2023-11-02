## redis多数据源

### yml配置
~~~yml
spring:
  redis:
    # redis数据库索引（默认为0）
    database: 0
    # redis服务器地址
    host: xx.xx.xxx.xxx
    # redis服务器连接端口
    port: 1
    # redis服务器连接密码（尽量设置密码，防止远程注入服务器挖矿）
    password: 1
    lettuce:
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: -1
        # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: 3000
        # 连接池中的最大空闲连接
        max-idle: 8
        # 连接池中的最小空闲连接
        min-idle: 0
    #连接超时时间
    timeout: 10000

  redis2:
    database: 1
    host: xx.xx.xxx.xxx
    port: 1
    password: 1
    lettuce:
      pool:
        max-active: -1
        max-wait: 3000
        max-idle: 8
        min-idle: 0
    timeout: 10000
~~~

### 配置类代码
~~~java
@Configuration
public class RedisAutoConfiguration {
    /**
     * 第二数据源配置信息
     */
    @Bean(name = "secondRedisProperties")
    @ConfigurationProperties(prefix = "spring.redis2")
    public RedisProperties secondRedisProperties() {
        return new RedisProperties();
    }

    @Bean(name = "secondRedisTemplate")
    public RedisTemplate<String, Object> secondRedisTemplate(@Qualifier("secondRedisProperties") RedisProperties secondRedisProperties) {
        if ("localhost".equals(secondRedisProperties.getHost())) {
            return null;
        }
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory(secondRedisProperties));
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }

    /**
     * 使用lettuce配置Redis连接信息
     */
    public RedisConnectionFactory connectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        int database = redisProperties.getDatabase();

        Duration timeout = null == redisProperties.getTimeout() ? Duration.ZERO : redisProperties.getTimeout();

        int maxIdle = redisProperties.getLettuce().getPool().getMaxIdle();
        int minIdle = redisProperties.getLettuce().getPool().getMinIdle();
        int maxActive = redisProperties.getLettuce().getPool().getMaxActive();
        Duration maxWait = redisProperties.getLettuce().getPool().getMaxWait();

        configuration.setHostName(host);
        configuration.setPort(port);
        if (null != password) {
            configuration.setPassword(password);
        }
        if (database != 0) {
            configuration.setDatabase(database);
        }
        GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWait(maxWait);

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(timeout)
                .poolConfig(genericObjectPoolConfig)
                .build();

        LettuceConnectionFactory lettuce = new LettuceConnectionFactory(configuration, clientConfig);
        lettuce.afterPropertiesSet();
        return lettuce;
    }
}
~~~