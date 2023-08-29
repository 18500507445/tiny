package com.tiny.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description 网关启动类
 * @date: 2023/08/29 14:19
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TinyGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(TinyGatewayApplication.class, args);
    }
}
