package com.tiny.common.core.thread;

import cn.hutool.core.map.MapUtil;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author: wzh
 * @description MDC线程包装类，线程池taskDecorator可以使用
 * @date: 2023/12/12 14:45
 */
@SuppressWarnings("NullableProblems")
public class MdcTaskDecorator implements TaskDecorator {

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
}
