package com.tiny.common.starter.xxl;

import cn.hutool.core.util.StrUtil;
import com.tiny.common.core.trace.TraceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description xxlJob切面
 * @date: 2023/11/30 19:12
 */
@Aspect
@Component
public class XxlJobAopConfig {

    @Pointcut("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        //获取MDC中的traceId，没有就生成一个
        String traceId = TraceContext.getTraceId();
        try {
            if (StrUtil.isBlank(traceId)) {
                TraceContext.getCurrentTrace();
            }
            return point.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            TraceContext.removeTrace();
        }
    }

}
