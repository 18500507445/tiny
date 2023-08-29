package com.tiny.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: wzh
 * @description 网关全局过滤器
 * @date: 2023/08/29 14:20
 */
@Component
public class AccessFilter implements GlobalFilter, Ordered {

    /**
     * order值越小执行越早，如果全局order和单个order一样的话，全局优先
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 过滤器
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String requestPath = request.getPath().value();

        //header获取请求key
        final String key = request.getHeaders().getFirst("key");

        //不满足条件的禁止访问
        if (requestPath.equals("")) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}
