package com.tiny.example;

import com.tiny.common.core.result.RespResult;
import com.tiny.common.core.utils.common.IpUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.concurrent.CompletableFuture;

/**
 * @author: wzh
 * @description 样例工程启动类
 * @date: 2023/08/29 14:45
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.tiny"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.tiny.common.starter.*")})
@EnableFeignClients(basePackages = "com.tiny")
public class ExampleApplication {

    /**
     * ComponentScan 组件扫描指定路径，因为例如SpringUtils打上组件注解，但是跨包扩这项目了，不会自己注册为bean
     * 除非starter这种自动装配的，所以还需要把starter路径下已经装配手动排除掉
     */
    public static void main(String[] args) {
        //关闭 pageHelper启动banner
        System.setProperty("pagehelper.banner", "false");

        ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleApplication.class, args);
        String redis = applicationContext.getEnvironment().getProperty("spring.redis.host");
        String mongo = applicationContext.getEnvironment().getProperty("spring.data.mongodb.host");
        String profile = applicationContext.getEnvironment().getProperty("spring.config.activate.on-profile");
        String name = applicationContext.getEnvironment().getProperty("spring.application.name");
        String serverAddr = applicationContext.getEnvironment().getProperty("spring.cloud.nacos.discovery.server-addr");

        System.err.println("profile :" + profile);
        System.err.println("name :" + name);
        System.err.println("serverAddr :" + serverAddr);
        System.err.println("redis :" + redis);
        System.err.println("mongo :" + mongo);

        //公网ip 后两位初始化ResultVO
        CompletableFuture.runAsync(() -> {
            System.err.println("当前模块启动成功，初始化ResultVO");
            String internetIp = IpUtils.getInternetIp("curl cip.cc");
            RespResult.setIP(internetIp);
        });
    }

}
