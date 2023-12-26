package com.tiny.framework.core.trace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @description: TraceFilter配置
 * @author: wzh
 * @date: 2023/09/20 11:16
 */
@Slf4j(topic = "tiny-framework-starter ==> TraceConfig")
@Configuration
public class TraceConfig {

    @Resource
    private TraceFilter traceFilter;

    @Bean
    public FilterRegistrationBean<TraceFilter> registerTraceFilter() {
        try {
            FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<>();
            registration.setFilter(traceFilter);
            registration.addUrlPatterns("/*");
            registration.setName("traceFilter");
            registration.setOrder(1);
            return registration;
        } finally {
            log.warn("装配【TraceConfig】，链路追踪已开启");
        }
    }
}
