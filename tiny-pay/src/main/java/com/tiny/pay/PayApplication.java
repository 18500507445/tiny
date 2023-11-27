package com.tiny.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author: wzh
 * @description 支付应用启动类
 * @date: 2023/10/11 19:13
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.tiny"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.tiny.common.starter.*")})
@EnableFeignClients(basePackages = "com.tiny.api")
public class PayApplication {

    public static void main(String[] args) {
        System.setProperty("pagehelper.banner", "false");
        SpringApplication.run(PayApplication.class, args);
    }
}
