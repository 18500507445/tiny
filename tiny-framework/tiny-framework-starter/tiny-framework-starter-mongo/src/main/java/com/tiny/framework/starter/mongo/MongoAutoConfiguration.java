package com.tiny.framework.starter.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * @description: mongo配置类
 * @author: wzh
 * @date: 2023/8/22 20:53
 */
@Slf4j(topic = "tiny-framework-starter ==> MongoAutoConfiguration")
@Configuration
public class MongoAutoConfiguration {

    @Bean(name = "mongoService")
    public MongoService mongoService() {
        log.warn("装配【MongoService】");
        return new MongoService();
    }

    /**
     * 第一数据源配置信息
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.data.mongodb")
    public MongoProperties primaryMongoProperties() {
        return new MongoProperties();
    }

    /**
     * 默认 MongoTemplate
     */
    @Bean(name = "primaryMongoTemplate")
    @Primary
    public MongoTemplate primaryMongoTemplate() throws UnsupportedEncodingException {
        MongoProperties mongoProperties = this.primaryMongoProperties();
        if (null != mongoProperties.getHost()) {
            MongoTemplate mongoTemplate = new MongoTemplate(getMongoFactory(mongoProperties));
            MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
            mongoMapping.afterPropertiesSet();
            return mongoTemplate;
        } else {
            return null;
        }
    }

    /**
     * 通用获取链接工厂方法
     * 默认 MongoDatabaseFactory
     * url = 'mongodb://userName:password@ip:port/database?authSource=authentication-database'
     */
    private static MongoDatabaseFactory getMongoFactory(MongoProperties mongoProperties) throws UnsupportedEncodingException {
        String url;
        if (null != mongoProperties.getUri()) {
            url = mongoProperties.getUri();
        } else {
            StringBuilder uri = new StringBuilder("mongodb://");
            if (null != mongoProperties.getUsername()) {
                uri.append(mongoProperties.getUsername());
                uri.append(":");
            }
            if (null != mongoProperties.getPassword()) {
                String password = Arrays.toString(mongoProperties.getPassword());
                password = password.replace("[", "").replace("]", "").replace(", ", "");
                uri.append(URLEncoder.encode(password, "utf-8"));
                uri.append("@");
            }
            uri.append(mongoProperties.getHost());
            uri.append(":");
            uri.append(mongoProperties.getPort());
            if (null != mongoProperties.getDatabase()) {
                uri.append("/").append(mongoProperties.getDatabase());
            }
            if (null != mongoProperties.getAuthenticationDatabase()) {
                uri.append("?authSource=").append(mongoProperties.getAuthenticationDatabase());
            }
            url = uri.toString();
        }
        return new SimpleMongoClientDatabaseFactory(url);
    }
}
