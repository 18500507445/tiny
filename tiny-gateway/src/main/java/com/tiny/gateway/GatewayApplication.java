package com.tiny.gateway;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author: wzh
 * @description: 网关启动类
 * @date: 2023/08/29 14:19
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.tiny"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.tiny.framework.*"})})
@EnableFeignClients(basePackages = "com.tiny")
public class GatewayApplication {

    /**
     * 如果web路径下的模块有需要注册bean的，最好自动装配，否则排除掉
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        System.err.println("初始化 bean BeanPostProcessor");
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
                System.err.println("加载bean -> " + beanName);
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
                return bean;
            }
        };
    }
}
