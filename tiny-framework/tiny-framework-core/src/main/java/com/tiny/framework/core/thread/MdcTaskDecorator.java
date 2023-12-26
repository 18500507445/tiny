package com.tiny.framework.core.thread;

import cn.hutool.core.map.MapUtil;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * @author: wzh
 * @description: spring线程池，MDC线程装饰类
 * @date: 2023/12/12 14:45
 */
@SuppressWarnings("NullableProblems")
public class MdcTaskDecorator implements TaskDecorator {


    /**
     * TaskDecorator接口定义了一个方法decorate(Runnable runnable)，它接受一个Runnable任务对象作为参数，
     * 并返回一个被装饰后的Runnable对象。在Spring框架中，当使用ThreadPoolTaskExecutor作为线程池实现时，
     * 可以通过实现TaskDecorator接口来对提交给线程池的任务进行装饰。
     * </p>
     * 装饰器模式可以用于在任务执行之前或之后执行一些额外的操作，比如修改任务的上下文、记录日志、计算执行时间等。
     * 通过使用TaskDecorator，可以在任务执行前后对线程池中的任务进行定制化的处理。
     */

    /**
     * 异步任务支持traceId
     *
     * @param runnable the original {@code Runnable}
     */
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (MapUtil.isNotEmpty(copyOfContextMap)) {
                    MDC.setContextMap(copyOfContextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    public static void main(String[] args) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        //这里是重点，设置线程池的装饰器，将traceId进行透传
        threadPoolTaskExecutor.setTaskDecorator(new MdcTaskDecorator());
    }
}
