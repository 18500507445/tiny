package com.tiny.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author: wzh
 * @description 网关启动类
 * @date: 2023/08/29 14:19
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.tiny"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.tiny.common.starter.*", "com.avic.common.web.*"})})
public class TinyGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(TinyGatewayApplication.class, args);
    }
}
