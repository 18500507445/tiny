package com.tiny.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author: wzh
 * @description 网关启动类
 * @date: 2023/08/29 14:19
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.tiny"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.tiny.common.starter.*", "com.tiny.common.core.*"})})
@EnableFeignClients(basePackages = "com.tiny")
@Slf4j
public class TinyGatewayApplication {

    /**
     * 如果web路径下的模块有需要注册bean的，最好自动装配，否则排除掉
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(TinyGatewayApplication.class, args);
    }
}
