package com.tiny.framework.starter.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author: wzh
 * @description: 父类Rabbit配置类
 * <p>
 * @date: 2023/12/14 13:43
 */
@Slf4j(topic = "tiny-framework-starter ==> ParentRabbitConfig")
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
    public ConnectionFactory primaryConnectionFactory(@Qualifier("primaryProperties") RabbitProperties primaryProperties) {
        CachingConnectionFactory connectionFactory = getRabbitConnectionFactory(primaryProperties);
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
        CachingConnectionFactory connectionFactory = getRabbitConnectionFactory(secondProperties);
        log.warn("装配【secondProperties】：第二个数据源配置，【secondConnectionFactory】第二个连接工厂");
        return connectionFactory;
    }

    // 通用获取链接，设置属性
    private static CachingConnectionFactory getRabbitConnectionFactory(RabbitProperties properties) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.getRabbitConnectionFactory().setRequestedChannelMax(properties.getRequestedChannelMax());
        connectionFactory.setAddresses(properties.getAddresses());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setVirtualHost(properties.getVirtualHost());
        if (null != properties.getCache().getChannel().getSize()) {
            connectionFactory.setChannelCacheSize(properties.getCache().getChannel().getSize());
        }
        if (null != properties.getCache().getConnection().getSize()) {
            connectionFactory.setConnectionCacheSize(properties.getCache().getConnection().getSize());
        }
        return connectionFactory;
    }
}
