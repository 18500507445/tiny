package com.tiny.example;

import com.tiny.framework.core.exception.GlobalExceptionAdvice;
import com.tiny.framework.core.result.base.ResResult;
import com.tiny.framework.core.utils.common.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.CompletableFuture;

/**
 * @author: wzh
 * @description: 样例工程启动类
 * @date: 2023/08/29 14:45
 */
@Slf4j(topic = "ExampleApplication")
//springboot启动类注解，排除掉数据库自动配置
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//组件扫描因为模块的原因需要手动设置，并且把starter已经装配的排除掉
@ComponentScan(basePackages = {"com.tiny"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.tiny.framework.starter.*")})
//开启feign客户端支持
@EnableFeignClients(basePackages = "com.tiny.api")
//开启spring重试
@EnableRetry
//开启spring异步支持
@EnableAsync
//实现配置的动态刷新功能
@RefreshScope
//mapper组件扫描，自定义路径，等价于@Mapper注解
@MapperScan(basePackages = {"com.tiny.example"})
public class ExampleApplication {

    /**
     * ComponentScan: 组件扫描指定路径，因为例如SpringUtils打上组件注解，但是跨包扩这项目了，不会自己注册为bean
     * 除非starter这种自动装配的，所以还需要把starter路径下已经装配手动排除掉
     * 经过测试，如果spring.factories里面配置了，那么不需要扫描就装配进来了，可以观察启动，并且@ComponentScan排除也不管用
     * tips：有一个场景如果本模块和common模块的包路径不一致，需要都扫进来例如（"com.tiny.common","com.tiny.example"），但是你可以偷懒（"com.tiny"）这样就都扫描到了😁
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

        log.warn("profile :{}，name :{}，serverAddr :{}，redis :{}，mongo :{}", profile, name, serverAddr, redis, mongo);

        //开启参数校验warn、全局业务运行异常error日志
        GlobalExceptionAdvice.setBindLog(true);
        GlobalExceptionAdvice.setBusinessLog(true);
        log.warn("【example】模块，开启GlobalExceptionAdvice ==> BusinessException errorLog、BindException warnLog");

        //异步执行
        CompletableFuture.supplyAsync(IpUtils::getInternetIp).thenAccept(s -> {
            //公网ip 后两位初始化ResultVO
            ResResult.setInternetIp(s);
            log.warn("【example】模块启动成功，初始化公网ip：{}，放入ResultVO", s);
            log.warn("【nacos地址 =====> http://localhost:8848/nacos/#】");
        });
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
