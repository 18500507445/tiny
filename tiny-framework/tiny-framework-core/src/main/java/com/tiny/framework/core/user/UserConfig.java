package com.tiny.framework.core.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description 用户过滤器配置类
 * @date: 2023/10/12 14:05
 */
@Slf4j(topic = "tiny-common-core ==> UserConfig")
@Configuration
public class UserConfig {

    @Resource
    private UserFilter userFilter;

    @Bean
    public FilterRegistrationBean<UserFilter> registerUserFilter() {
        try {
            FilterRegistrationBean<UserFilter> registration = new FilterRegistrationBean<>();
            registration.setFilter(userFilter);
            registration.addUrlPatterns("/*");
            registration.setName("userFilter");
            registration.setOrder(2);
            return registration;
        } finally {
            log.warn("装配【UserConfig】，可从UserContext.UserToken拿到当先线程用户信息");
        }
    }
}
