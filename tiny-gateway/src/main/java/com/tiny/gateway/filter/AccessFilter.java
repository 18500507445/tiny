package com.tiny.gateway.filter;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.tiny.framework.core.Constants;
import com.tiny.framework.core.result.base.ResResult;
import com.tiny.framework.core.result.base.ResultCode;
import com.tiny.framework.core.trace.Trace;
import com.tiny.framework.core.trace.TraceContext;
import com.tiny.framework.core.utils.common.IpUtils;
import com.tiny.framework.core.utils.common.JwtUtils;
import com.tiny.gateway.config.GateWayConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author: wzh
 * @description: 网关全局过滤器，例如token处理，白名单处理
 * @date: 2023/08/29 14:20
 */
@Component
@Order(-99)
@Slf4j(topic = "AccessFilter")
@Data
public class AccessFilter implements GlobalFilter {

    @Resource
    private GateWayConfig gatewayConfig;

    /**
     * 可以放入nacos中，jwt的key
     */
    private String key = JwtUtils.SECRET;

    /**
     * 路径匹配器
     */
    private final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 过滤器
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //放入参数startTime，计算耗时
        exchange.getAttributes().put(Constants.START_TIME, System.currentTimeMillis());

        //构建trace
        ServerWebExchange build = buildGlobalTraceId(exchange);

        //获取ip和请求url
        ServerHttpRequest request = exchange.getRequest();
        String ip = IpUtils.getClientIp(request);
        String url = request.getURI().getPath();
        String method = request.getMethodValue();
        HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(request.getHeaders());
        log.info("request：{}，method：{}，header：{}，ip：{}", url, method, JSONObject.toJSONString(httpHeaders), ip);

        //判断url是否禁止访问
        if (checkForbiddenUrls(url)) {
            log.error("网关配置禁止访问的url：{}", url);
            return responseErrorMsg(exchange, ResultCode.FORBIDDEN);
        }

        //禁止的ip访问
        if (checkIpBlackList(ip)) {
            log.error("网关配置禁止访问的ip：{}", ip);
            return responseErrorMsg(exchange, ResultCode.FORBIDDEN);
        }

        //请求url是否开启鉴权
        if (!gatewayConfig.getAuthEnable()) {
            log.info("网关不启用鉴权，所有请求直接放行");
            return filter(chain, build, exchange, url);
        }

        //放行的url
        if (checkReleaseUrls(url)) {
            log.info("网关配置放行的url:{}", url);
            return filter(chain, build, exchange, url);
        }

        // 从header Authorization中获取
        String authorization = httpHeaders.getFirst(Constants.AUTHORIZATION);
        if (StrUtil.isEmpty(authorization)) {
            log.error("invalid token or not login! authorization：{}", authorization);
            return responseErrorMsg(exchange, ResultCode.TOKEN_EMPTY);
        }

        //判断是否过期
        boolean tokenExpired = JwtUtils.isTokenExpired(authorization);
        if (tokenExpired) {
            return responseErrorMsg(exchange, ResultCode.TOKEN_EXPIRATION);
        }

        //解析authorization
        try {
            Map<String, Claim> parse = JwtUtils.parse(authorization);
            String userContext = parse.get("json").asString();
            if (StrUtil.isNotBlank(userContext)) {
                //todo redis缓存失效判断、用户状态判断，最后都没问题再放行


                //写到header里 userContext
                setHeaderInfo(build, request, userContext, httpHeaders);
            }
        } catch (Exception e) {
            return responseErrorMsg(exchange, ResultCode.TOKEN_INVALID);
        }
        return filter(chain, build, exchange, url);
    }

    /**
     * 功能: 全局的traceId处理，生成traceId和spanId放入header
     */
    private ServerWebExchange buildGlobalTraceId(ServerWebExchange exchange) {
        HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(exchange.getRequest().getHeaders());
        //先清除，防止有残留的trace对象，再从新生成一个
        TraceContext.removeTrace();
        Trace trace = TraceContext.getCurrentTrace();
        //放入header
        Consumer<HttpHeaders> httpHeadersConsumer = x -> {
            httpHeaders.set(Trace.TRACE_ID, trace.getTraceId());
            httpHeaders.set(Trace.SPAN_ID, trace.getSpanId());
        };
        ServerHttpRequest req = exchange.getRequest().mutate().headers(httpHeadersConsumer).build();
        return exchange.mutate().request(req).build();
    }

    /**
     * 处理请求
     * （1）放行的url，如果有authorization但是没有userContext，那么也进行解析后放入header里
     * （2）开启慢日志，并且超过1000ms，从header里面取出来放入当前的MDC中，方便日志打印的时候展示traceId、spanId
     *
     * @param chain
     * @param build
     * @param exchange
     * @param url
     */
    private Mono<Void> filter(GatewayFilterChain chain, ServerWebExchange build, ServerWebExchange exchange, String url) {
        //1.放行的url，header进行特殊处理
        ServerHttpRequest request = exchange.getRequest();
        String authorization = request.getHeaders().getFirst("Authorization");
        String userContext = request.getHeaders().getFirst(Constants.USER_CONTEXT);

        //2.有token但是没有进行解析进行处理，注意：token别过期
        if (StrUtil.isNotBlank(authorization) && StrUtil.isBlank(userContext)) {
            if (!JwtUtils.isTokenExpired(authorization)) {
                Map<String, Claim> parse = JwtUtils.parse(authorization);
                try {
                    parse = JwtUtils.parse(authorization);
                } finally {
                    //3.放行进来的url，不用判断用户状态什么的，直接放行，将userContext写到header里
                    if (ObjectUtil.isNotNull(parse)) {
                        userContext = parse.get("json").asString();
                        if (StrUtil.isNotBlank(userContext)) {
                            setHeaderInfo(build, request, userContext, HttpHeaders.writableHttpHeaders(request.getHeaders()));
                        }
                    }
                }
            }
        }

        //4.判断是否开启慢log打印，超过界限值默认1000毫秒
        return chain.filter(build).then(Mono.fromRunnable(() -> {
            if (gatewayConfig.getSlowEnable()) {
                Long startTime = exchange.getAttribute(Constants.START_TIME);
                if (null != startTime) {
                    Long executeTime = (System.currentTimeMillis() - startTime);
                    if (executeTime > gatewayConfig.getSlowMillisecond()) {
                        HttpHeaders headers = exchange.getRequest().getHeaders();
                        MDC.put(Trace.TRACE_ID, headers.getFirst(Trace.TRACE_ID));
                        MDC.put(Trace.SPAN_ID, headers.getFirst(Trace.SPAN_ID));
                        log.error("响应url：{}，耗时：{} ms", url, executeTime);
                    }
                }
            }
        }));
    }

    /**
     * 配置中心检查被禁止的url
     */
    private boolean checkForbiddenUrls(String url) {
        String[] forbiddenUrls = gatewayConfig.getForbiddenUrls();
        if (ArrayUtil.isNotEmpty(forbiddenUrls)) {
            for (String forbiddenUrl : forbiddenUrls) {
                if (PATH_MATCHER.match(forbiddenUrl, url)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从配置中心检查放行的url
     */
    private boolean checkReleaseUrls(String url) {
        String[] releaseUrls = gatewayConfig.getReleaseUrls();
        if (ArrayUtil.isNotEmpty(releaseUrls)) {
            for (String releaseUrl : releaseUrls) {
                if (PATH_MATCHER.match(releaseUrl, url)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从配置中心检查ip黑名单
     */
    private boolean checkIpBlackList(String ip) {
        String[] ipBlackList = gatewayConfig.getIpBlackList();
        if (ArrayUtil.isNotEmpty(ipBlackList)) {
            for (String match : ipBlackList) {
                if (PATH_MATCHER.match(match, ip)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回错误的信息
     */
    private Mono<Void> responseErrorMsg(ServerWebExchange exchange, ResultCode resultCode) {
        DataBuffer buffer = responseMsg(exchange, ResResult.failure(resultCode));
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    /**
     * 设置返回信息
     * 备注：如果需要返回 Mono<Void>
     * （1）DataBuffer wrap = resp.bufferFactory().wrap(bytes);
     * （2）resp.writeWith(Flux.just(wrap));
     */
    private DataBuffer responseMsg(ServerWebExchange exchange, ResResult<Void> resResult) {
        ServerHttpResponse resp = exchange.getResponse();
        resp.setStatusCode(HttpStatus.OK);
        resp.getHeaders().add(HttpHeaders.CONTENT_TYPE, Constants.JSON);
        String jsonString = JSONObject.toJSONString(resResult);
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
        return resp.bufferFactory().wrap(bytes);
    }

    /**
     * 设置header信息
     */
    private void setHeaderInfo(ServerWebExchange build, ServerHttpRequest request, String userContext, HttpHeaders headers) {
        Consumer<HttpHeaders> httpHeadersConsumer = x -> {
            if (StrUtil.isNotEmpty(userContext)) {
                headers.set(Constants.USER_CONTEXT, URLUtil.encode(userContext));
            }
        };
        build.mutate().request(request.mutate().headers(httpHeadersConsumer).build()).build();
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
        ResResult<Void> error = ResResult.failure(ResultCode.FAILED);
        String returnStr = "";
        try {
            returnStr = JSON.toJSONString(error);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }

}
