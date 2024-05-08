package com.tiny.framework.cloud;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wzh
 * @description: openFeign配置类
 * @date: 2023/09/25 17:09
 */
@Slf4j(topic = "tiny-framework-cloud ==> FeignConfig")
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        try {
            return new FeignRequestInterceptor();
        } finally {
            log.warn("装配【FeignConfig】，全局openFeign拦截器，处理traceId透传");
        }
    }
}
