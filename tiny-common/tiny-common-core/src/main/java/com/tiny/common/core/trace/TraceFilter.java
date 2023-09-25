package com.tiny.common.core.trace;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.tiny.common.core.utils.common.RequestParamsUtil;
import com.tiny.common.core.context.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @description: web端链路过滤器处理(设置traceId, spanId)
 * @author: wzh
 * @date: 2023/09/20 11:16
 */
@Component
@Slf4j(topic = "TraceFilter")
public class TraceFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        try {
            long start = System.currentTimeMillis();
            HttpServletRequest request = (HttpServletRequest) req;
            String traceId = request.getHeader(Trace.TRACE_ID);
            //正常启动单服务可能拿不到，需要生成一个，如果网关进行设置了直接放入Trace对象
            if (StrUtil.isNotEmpty(traceId)) {
                TraceHelper.setCurrentTrace(traceId);
            } else {
                TraceHelper.getCurrentTrace();
            }
            RequestWrapper requestWrapper = printAccessLog(request);
            log.info("trace web filter-traceId:{}", MDC.get(Trace.TRACE_ID));
            filterChain.doFilter(requestWrapper, resp);
            //响应后置处理 MDC放入spanId
            MDC.put(Trace.SPAN_ID, TraceHelper.genSpanId());
            log.error("当前请求总耗时：{} ms", System.currentTimeMillis() - start);
        } finally {
            TraceHelper.removeTrace();
        }
    }

    /**
     * 打印访问日志
     */
    @SuppressWarnings({"unchecked"})
    private RequestWrapper printAccessLog(HttpServletRequest request) throws IOException {
        RequestWrapper requestWrapper = new RequestWrapper(request);
        String requestUrl = request.getRequestURI();
        SortedMap<String, Object> paramResult = new TreeMap<>(RequestParamsUtil.getUrlParams(request));
        try {
            //非get方式，处理body
            if (!StrUtil.equals(HttpMethod.GET.name(), request.getMethod())) {
                String contentType = request.getContentType();
                if (StrUtil.containsIgnoreCase(contentType, "json")) {
                    String body = getBody(requestWrapper);
                    if (StrUtil.isNotBlank(body)) {
                        paramResult.putAll(JSONObject.parseObject(body, Map.class));
                    }
                } else if (StrUtil.containsIgnoreCase(contentType, "form")) {
                    paramResult.putAll(RequestParamsUtil.getFormParams(request));
                }
            }
        } finally {
            log.info("请求apiName：{}，方式：{}，body：{}", requestUrl, request.getMethod(), JSONObject.toJSONString(paramResult));
        }
        return requestWrapper;
    }

    public static String getBody(HttpServletRequest request) {
        String body = "";
        try {
            ServletInputStream in = request.getInputStream();
            body = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return body;
    }
}