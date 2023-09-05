package com.tiny.common.starter.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: wzh
 * @date: 2023/8/22 20:53
 */
@Configuration
public class MongoAutoConfiguration {

    @Bean(name = "mongoService")
    public MongoService mongoService() {
        return new MongoService();
    }
}
