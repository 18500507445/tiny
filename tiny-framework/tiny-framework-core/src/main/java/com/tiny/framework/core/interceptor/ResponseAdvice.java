package com.tiny.framework.core.interceptor;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author: wzh
 * @description: 响应拦截器
 * @date: 2024/01/04 13:08
 */
@Slf4j(topic = "tiny-framework-starter ==> ResponseAdvice")
@RefreshScope
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    //是否开启响应log
    @Value("${spring.response.enableLog:false}")
    private boolean enableLog;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return enableLog;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.info("接口url：{}，响应结果：{}", request.getURI().getPath(), JSONObject.toJSONString(body));
        return body;
    }
}
