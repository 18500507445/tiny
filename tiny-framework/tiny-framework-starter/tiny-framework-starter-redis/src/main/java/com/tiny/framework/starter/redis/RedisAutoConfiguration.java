package com.tiny.framework.starter.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @description: redis自动配置类
 * @author: wzh
 * @date: 2023/8/22 20:08
 */
@Slf4j(topic = "tiny-framework-starter ==> RedisAutoConfiguration")
@Configuration
public class RedisAutoConfiguration {

    /**
     * 第一数据源配置信息
     */
    @Primary
    @Bean(name = "primaryRedisProperties")
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisProperties primaryRedisProperties() {
        return new RedisProperties();
    }

    /**
     * 第二数据源配置信息
     */
    @Bean(name = "secondRedisProperties")
    @ConfigurationProperties(prefix = "spring.redis2")
    public RedisProperties secondRedisProperties() {
        return new RedisProperties();
    }

    @Bean(name = "redisService")
    public RedisService redisService() {
        log.warn("装配【RedisService】");
        return new RedisService();
    }

    /**
     * 创建 RedisTemplate Bean，使用 JSON 序列化方式
     */
    @Bean(name = {"primaryRedisTemplate", "redisTemplate"})
    public RedisTemplate<String, Object> primaryRedisTemplate(@Qualifier("primaryRedisProperties") RedisProperties primaryRedisProperties) {
        // 创建 RedisTemplate 对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置 RedisConnection 工厂。😈 它就是实现多种 Java Redis 客户端接入的秘密工厂。
        template.setConnectionFactory(getRedisconnectionFactory(primaryRedisProperties));
        // 使用 String 序列化方式，序列化 KEY 。
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }

    @Bean(name = "secondRedisTemplate")
    public RedisTemplate<String, Object> secondRedisTemplate(@Qualifier("secondRedisProperties") RedisProperties secondRedisProperties) {
        if ("localhost".equals(secondRedisProperties.getHost())) {
            return null;
        }
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(getRedisconnectionFactory(secondRedisProperties));
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }

    /**
     * 通用获取链接工厂方法
     * 使用lettuce配置Redis连接信息
     */
    private static RedisConnectionFactory getRedisconnectionFactory(RedisProperties redisProperties) {
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
