package com.tiny.order;

import com.tiny.framework.core.exception.GlobalExceptionAdvice;
import com.tiny.framework.core.result.ResResult;
import com.tiny.framework.core.utils.common.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.concurrent.CompletableFuture;

/**
 * @author: wzh
 * @description: 订单应用启动类
 * @date: 2023/10/11 19:12
 */
@Slf4j(topic = "OrderApplication")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.tiny"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.tiny.framework.starter.*")})
@EnableFeignClients(basePackages = "com.tiny.api")
public class OrderApplication {

    public static void main(String[] args) {
        System.setProperty("pagehelper.banner", "false");
        SpringApplication.run(OrderApplication.class, args);

        GlobalExceptionAdvice.setBusinessLog(true);
        log.warn("【order】模块，开启GlobalExceptionAdvice ==> BusinessException errorLog");

        //异步执行
        CompletableFuture.supplyAsync(IpUtils::getInternetIp).thenAccept(s -> {
            //公网ip 后两位初始化ResultVO
            ResResult.setInternetIp(s);
            log.warn("【order】模块启动成功，初始化公网ip：" + s + "，放入RespResult");
        });
    }
}
