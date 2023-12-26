package com.tiny.framework.core.trace;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: Trace对象
 * @author: wzh
 * @date: 2023/09/20 11:16
 */
@Data
public class Trace implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * （1）log日志配置文件也需要取出来 [%X{TraceId}]
     * （2）http请求发送traceId放入header中
     */
    public static final String TRACE_ID = "traceId";

    public static final String SPAN_ID = "spanId";

    /**
     * 分布式traceId，当前请求整个链路都是一个id号，通过header进行透传，注意网关、过滤器、feign、多线程、http
     */
    private String traceId;
    /**
     * 分布式spanId，当前服务当前一次请求(当前线程)分配一个id号
     */
    private String spanId;
}


