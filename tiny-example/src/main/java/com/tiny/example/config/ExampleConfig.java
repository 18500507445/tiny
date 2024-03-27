package com.tiny.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wzh
 * @description: 样例配置类，两种方式（1.@Value注解获取  2.@ConfigurationProperties(prefix = "spring.data")通配整个类）
 * @date: 2023/11/29 15:33
 */
@Data
@Configuration
public class ExampleConfig {

    //给个默认值，万一线上没配置
    @Value("${spring.data.example:a}")
    private String a;

}
