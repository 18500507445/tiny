package com.tiny.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description 样例工程启动类
 * @date: 2023/08/29 14:45
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TinyExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyExampleApplication.class, args);
    }

}
