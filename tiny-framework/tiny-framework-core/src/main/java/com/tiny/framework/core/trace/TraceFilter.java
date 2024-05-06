package com.tiny.framework.core.trace;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.tiny.framework.core.request.RequestWrapper;
import com.tiny.framework.core.result.base.ResResult;
import com.tiny.framework.core.result.base.ResultCode;
import com.tiny.framework.core.utils.common.RequestParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@Slf4j(topic = "tiny-framework-core ==> TraceFilter")
public class TraceFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException {
        long start = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) req;
        try {
            String traceId = request.getHeader(Trace.TRACE_ID);
            /**
             * （1）正常启动单服务可能拿不到，需要生成一个，如果网关进行设置了直接放入Trace对象
             * （2）OpenFeign调用，需要设置traceId（FeignRequestInterceptor），设置到header里，这里能拿到继续透传
             * （3）todo 有就复用，没有就生成，这里traceId是null还是有值，不需要管，哈哈
             */
            TraceContext.setCurrentTrace(traceId);
            RequestWrapper requestWrapper = printAccessLog(request);
            filterChain.doFilter(requestWrapper != null ? requestWrapper : request, resp);
        } catch (Exception e) {
            if (e instanceof JSONException) {
                this.errorResponse(resp, "POST请求，后端开启@RequestBody注解，请传入参数进行json对象进行字符串处理，例如h5的JSON.stringify");
            }
        } finally {
            log.info("request：{}：耗时：{} ms", request.getRequestURI(), System.currentTimeMillis() - start);
            TraceContext.removeTrace();
        }
    }

    /**
     * 打印访问日志
     * 经过测试，get方式也可以传from表单（接口用实体类接收，不用添加@RequestBody）和body-json
     * 因为无法控制编程规范问题，所以都需要打印
     */
    @SuppressWarnings({"unchecked"})
    private RequestWrapper printAccessLog(HttpServletRequest request) throws IOException {
        RequestWrapper requestWrapper = null;
        String requestUrl = request.getRequestURI();
        SortedMap<String, Object> paramResult = new TreeMap<>(RequestParamsUtil.getUrlParams(request));
        String contentType = request.getContentType();
        try {
            //get请求，少判断一次，直接return
            if (StrUtil.isNotBlank(contentType)) {
                //json
                if (StrUtil.containsIgnoreCase(contentType, "json")) {
                    String body = getBody(requestWrapper = new RequestWrapper(request));
                    if (StrUtil.isNotBlank(body)) {
                        paramResult.putAll(JSONObject.parseObject(body, Map.class));
                    }
                }
                //from
                if (StrUtil.containsIgnoreCase(contentType, "form")) {
                    paramResult.putAll(RequestParamsUtil.getFormParams(request));
                }
            }
        } finally {
            log.info("request：{}，方式：{}，body：{}", requestUrl, request.getMethod(), JSONObject.toJSONString(paramResult));
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

    /**
     * 响应错误输出
     */
    private void errorResponse(ServletResponse resp, String msg) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) resp;
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setContentType("application/json");
        // 设置响应的字符编码为 UTF-8
        httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        ResResult<Object> failure = ResResult.failure(ResultCode.FAILED, msg);
        httpResponse.getWriter().write(JSON.toJSONString(failure));
        httpResponse.getWriter().flush();
    }
}