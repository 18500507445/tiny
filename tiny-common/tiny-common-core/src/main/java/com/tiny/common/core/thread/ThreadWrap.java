package com.tiny.common.core.thread;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: wzh
 * @description: 普通线程包装类
 * @date: 2023/12/24 22:36
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadWrap {

    public static <T> Callable<T> callableWrap(final Callable<T> callable) {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (null != copyOfContextMap) {
                    MDC.setContextMap(copyOfContextMap);
                }
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable runnableWrap(final Runnable runnable) {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (null != copyOfContextMap) {
                    MDC.setContextMap(copyOfContextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    public static void main(String[] args) {
        log.error("main");
        new Thread(ThreadWrap.runnableWrap(() -> log.error("sub"))).start();

        ThreadPoolExecutor poolExecutor = ExecutorBuilder.create().setCorePoolSize(200).setMaxPoolSize(200).setHandler(RejectPolicy.CALLER_RUNS.getValue()).build();
        poolExecutor.execute(ThreadWrap.runnableWrap(() -> log.error("pool-sub")));
    }
}
