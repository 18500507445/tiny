package com.tiny.gateway.handler;

import cn.hutool.core.map.MapUtil;
import com.tiny.framework.core.result.ResResult;
import com.tiny.framework.core.result.ResultCode;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author: wzh
 * @description: 网关自定义空白页
 * @date: 2023/08/29 16:24
 */
@Component
@Order(-1)
public class GateWayErrorPage extends AbstractErrorWebExceptionHandler {

    public GateWayErrorPage(ErrorAttributes errorAttributes,
                            ApplicationContext applicationContext,
                            ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 拦截到例如超时、404、502这些异常，返回自定义的响应
     */
    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        // 获取异常信息
        Map<String, Object> map = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        // 构建响应，状态码
        return ServerResponse.status(MapUtil.getInt(map, "status"))
                // 以JSON格式显示响应
                .contentType(MediaType.APPLICATION_JSON)
                // 响应体(响应内容，包装RespResult)
                .body(BodyInserters.fromValue(ResResult.failure(ResultCode.FAILED, map, MapUtil.getStr(map, "error"))));
    }
}
