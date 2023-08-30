package com.tiny.gateway.filter;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.tiny.common.core.Constants;
import com.tiny.common.core.entity.RespResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author: wzh
 * @description 网关全局过滤器，例如token处理，白名单处理
 * @date: 2023/08/29 14:20
 */
@Component
@Order(0)
@Slf4j(topic = "AccessFilter")
@Data
public class AccessFilter implements GlobalFilter {

    /**
     * 放入nacos中，url白名单
     */
    private String[] skipAuthUrls;

    /**
     * 放入nacos中，jwt的key
     */
    private String key = "key";

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
        String url = request.getURI().getPath();
        ServerHttpResponse resp = exchange.getResponse();
        if (null != skipAuthUrls && Arrays.asList(skipAuthUrls).contains(url)) {
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst("Authorization");
        if (StrUtil.isBlank(token)) {
            //验证失败错误输出
            return authError(resp);
        } else {
            //解析token
            JwtParser build = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8))).build();
            Jws<Claims> jws = build.parseClaimsJws(token);
            String userContext = jws.getBody().getSubject();

            if (StrUtil.isBlank(userContext)) {
                return authError(resp);
            } else {
                //todo redis缓存失效判断

                //todo 用户状态判断

                //todo 成功后放行
                //写到header里 userContext
                request.mutate().headers(httpHeaders -> httpHeaders.add("userContext", URLEncodeUtil.encode(userContext))).build();
                return chain.filter(exchange);
            }
        }
    }

    /**
     * 认证错误输出
     *
     * @param resp 响应对象
     * @return 错误结果
     */
    private Mono<Void> authError(ServerHttpResponse resp) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type", Constants.JSON);
        String returnStr = JSON.toJSONString(RespResult.error("token验证错误"));
        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }


}
