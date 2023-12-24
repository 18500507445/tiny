package com.tiny.example.config;

import com.tiny.common.core.thread.MdcTaskDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: wzh
 * @description 异步注解配置类
 * @date: 2023/12/24 20:13
 */
@Configuration
public class AsyncConfig extends AsyncConfigurerSupport {

    /**
     * 使用spring的线程池
     */
    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setThreadNamePrefix("ThreadPoolTaskExecutor");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        //这里是重点，设置线程池的装饰器，将traceId进行透传
        threadPoolTaskExecutor.setTaskDecorator(new MdcTaskDecorator());
        return threadPoolTaskExecutor;
    }
}
