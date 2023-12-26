package com.tiny.pay;

import com.tiny.framework.core.result.RespResult;
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
 * @description 支付应用启动类
 * @date: 2023/10/11 19:13
 */
@Slf4j(topic = "PayApplication")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.tiny"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.tiny.framework.starter.*")})
@EnableFeignClients(basePackages = "com.tiny.api")
public class PayApplication {

    public static void main(String[] args) {
        System.setProperty("pagehelper.banner", "false");
        SpringApplication.run(PayApplication.class, args);

        //异步执行
        CompletableFuture.supplyAsync(IpUtils::getInternetIp).thenAccept(s -> {
            //公网ip 后两位初始化ResultVO
            RespResult.setIp(s);
            log.warn("【order】模块启动成功，初始化公网ip：" + s + "，放入RespResult");
        });
    }
}
