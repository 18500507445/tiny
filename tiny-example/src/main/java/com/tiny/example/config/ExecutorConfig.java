package com.tiny.example.config;

import com.tiny.framework.core.thread.MdcTaskDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: wzh
 * @description: 全局线程池配置类
 * @date: 2023/12/24 20:17
 */
@Configuration
public class ExecutorConfig {

    /**
     * Spring类型线程池
     */
    @Bean(name = "springExecutor")
    public Executor springExecutor() {
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

    /**
     * jdk线程池
     * todo java中自带的线程池(ThreadPoolExecutor)没有提供TaskDecorator这个方法
     * todo 如果使用想进行TraceId透传，需要进行包装，使用ThreadWrap进行处理，poolExecutor.execute(ThreadWrap.runnableWrap(() -> log.error("pool-sub")))
     */
    @Bean(name = "jdkExecutor")
    public Executor jdkExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20));
        threadPoolExecutor.setThreadFactory(new CustomizableThreadFactory("ThreadPoolExecutor"));
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolExecutor;
    }
}
