package com.tiny.framework.cloud;

import cn.hutool.core.util.StrUtil;
import com.tiny.framework.core.trace.Trace;
import com.tiny.framework.core.trace.TraceContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author: wzh
 * @description: 全局openFeign拦截器，处理traceId
 * @date: 2023/09/25 17:10
 */
@Slf4j(topic = "tiny-framework-cloud ==> FeignRequestInterceptor")
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //MDC获取traceId，没有就生成一个
        String traceId = TraceContext.getTraceId();
        if (StrUtil.isEmpty(traceId)) {
            traceId = TraceContext.getCurrentTrace().getTraceId();
        }
        // 传递traceId
        requestTemplate.header(Trace.TRACE_ID, traceId);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        try {
            if (headerNames != null) {
                Map<String, Collection<String>> headers = requestTemplate.headers();
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    if (!headers.containsKey(name) &&
                            !"content-length".equalsIgnoreCase(name) &&
                            !"content-type".equalsIgnoreCase(name)) {
                        requestTemplate.header(name, values);
                    }
                }
            }
        } catch (Exception e) {
            log.error("执行FeignRequestInterceptor系统异常！errMsg: {}", e.getMessage());
        }
    }
}
