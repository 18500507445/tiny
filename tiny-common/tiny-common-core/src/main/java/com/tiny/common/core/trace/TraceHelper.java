package com.tiny.common.core.trace;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.slf4j.MDC;
import org.springframework.core.NamedThreadLocal;

/**
 * @description: traceId和spanId工具类
 * @author: wzh
 * @date: 2023/09/20 11:16
 */
public class TraceHelper {

    /**
     * trace对象上下文
     */
    public static final ThreadLocal<Trace> TRACE_CONTEXT = new NamedThreadLocal<>("TraceId Context");

    /**
     * 禁止 new SnowflakeGenerator().next()生成的id有重复的
     */
    public static String genTraceId() {
        return IdUtil.getSnowflake().nextIdStr();
    }

    public static String genSpanId() {
        return SecureUtil.md5(UUID.randomUUID().toString()).substring(2, 16);
    }

    /**
     * 设置traceId
     */
    public static void setCurrentTrace(String traceId) {
        if (StrUtil.isBlank(traceId)) {
            traceId = genTraceId();
        }
        Trace trace = new Trace();
        trace.setTraceId(traceId);
        trace.setSpanId(genSpanId());
        MDC.put(Trace.TRACE_ID, trace.getTraceId());
        MDC.put(Trace.SPAN_ID, trace.getSpanId());
        TRACE_CONTEXT.set(trace);
    }

    /**
     * 获取traceId
     */
    public static Trace getCurrentTrace() {
        Trace trace = TRACE_CONTEXT.get();
        if (trace == null) {
            trace = new Trace();
            trace.setTraceId(genTraceId());
            MDC.put(Trace.TRACE_ID, trace.getTraceId());
        }
        // spanId每次不一样，重新生成，放到MDC中
        trace.setSpanId(genSpanId());
        MDC.put(Trace.SPAN_ID, trace.getSpanId());
        TRACE_CONTEXT.set(trace);
        return trace;
    }

    /**
     * 清空trace对象
     */
    public static void removeTrace() {
        MDC.remove(Trace.TRACE_ID);
        MDC.remove(Trace.SPAN_ID);
        TRACE_CONTEXT.remove();
    }
}