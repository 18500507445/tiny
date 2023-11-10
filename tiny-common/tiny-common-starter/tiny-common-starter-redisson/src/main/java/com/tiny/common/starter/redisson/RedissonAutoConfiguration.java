package com.tiny.common.starter.redisson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wzh
 * @description redisson自动装配类
 * @date: 2023/11/02 15:14
 */
@Configuration
public class RedissonAutoConfiguration {

    @Bean(name = "redissonLock")
    public RedissonLock redissonLock() {
        return new RedissonLock();
    }
}
