package com.tiny.framework.starter.redisson;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wzh
 * @description redisson自动装配类
 * @date: 2023/11/02 15:14
 */
@Slf4j(topic = "tiny-common-starter ==> RedissonAutoConfiguration")
@Configuration
public class RedissonAutoConfiguration {

    @Bean(name = "redissonLock")
    public RedissonLock redissonLock() {
        log.warn("装配【RedissonLock】");
        return new RedissonLock();
    }
}
