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
 * @description 父类Rabbit配置类
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
        log.warn("装配【secondProperties】：第二个数据源配置，【secondConnectionFactory】第二个连接工厂");
        return connectionFactory;
    }
}
