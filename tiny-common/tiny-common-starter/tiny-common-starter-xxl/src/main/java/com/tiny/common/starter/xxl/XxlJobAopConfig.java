package com.tiny.common.starter.xxl;

import cn.hutool.core.util.StrUtil;
import com.tiny.common.core.trace.Trace;
import com.tiny.common.core.trace.TraceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description xxlJob切面
 * @date: 2023/11/30 19:12
 */
@Aspect
@Component
public class XxlJobAopConfig {

    @Before("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public void beforeMethod() {
        String traceId = TraceContext.getTraceId();
        //透传traceId，没有就生成一个
        if (StrUtil.isNotEmpty(traceId)) {
            MDC.put(Trace.TRACE_ID, traceId);
        } else {
            traceId = TraceContext.getCurrentTrace().getTraceId();
            MDC.put(Trace.TRACE_ID, traceId);
        }
    }

}
