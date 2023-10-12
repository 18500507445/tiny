package com.tiny.common.core.user;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description 用户过滤器配置类
 * @date: 2023/10/12 14:05
 */
@Configuration
public class UserConfig {

    @Resource
    private UserFilter userFilter;

    @Bean
    public FilterRegistrationBean<UserFilter> registerUserFilter() {
        FilterRegistrationBean<UserFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(userFilter);
        registration.addUrlPatterns("/*");
        registration.setName("userFilter");
        registration.setOrder(2);
        return registration;
    }
}
