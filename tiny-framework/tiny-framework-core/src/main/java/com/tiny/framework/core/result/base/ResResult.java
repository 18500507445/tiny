package com.tiny.framework.core.result.base;


import cn.hutool.extra.spring.SpringUtil;
import com.tiny.framework.core.trace.TraceContext;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: wzh
 * @description: 返回实体类
 * @date: 2023/09/21 14:33
 */
@Data
public class ResResult<T> implements Serializable {

    /**
     * 状态码，比如000000代表响应成功
     */
    private String bizCode;
    /**
     * 响应信息，用来说明响应情况
     */
    private String bizMessage;
    /**
     * 响应的具体数据
     */
    private T data;

    private Boolean success;

    /**
     * 分布式链路id
     */
    private String traceId;

    private String spanId;

    /**
     * 系统时间
     */
    private Long systemTime;

    /**
     * 当前环境
     */
    private String env;

    private String ip;

    /**
     * 公网ip 最后2位，方便查找log
     */
    @Setter
    private static String internetIp = "00";

    public ResResult() {

    }

    public ResResult(IResultCode resultCode, T data, Boolean success) {
        this.bizCode = resultCode.getBizCode();
        this.bizMessage = resultCode.getBizMessage();
        this.data = data;
        this.success = success;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.spanId = TraceContext.getCurrentTrace().getSpanId();
        this.env = SpringUtil.getActiveProfile();
        this.systemTime = System.currentTimeMillis();
        this.ip = internetIp;
    }

    public ResResult(IResultCode resultCode, T data, Boolean success, String bizMessage) {
        this.bizCode = resultCode.getBizCode();
        this.bizMessage = bizMessage;
        this.data = data;
        this.success = success;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.spanId = TraceContext.getCurrentTrace().getSpanId();
        this.env = SpringUtil.getActiveProfile();
        this.systemTime = System.currentTimeMillis();
        this.ip = internetIp;
    }

    public ResResult(IResultCode resultCode, Boolean success) {
        this.bizCode = resultCode.getBizCode();
        this.bizMessage = resultCode.getBizMessage();
        this.success = success;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.spanId = TraceContext.getCurrentTrace().getSpanId();
        this.env = SpringUtil.getActiveProfile();
        this.systemTime = System.currentTimeMillis();
        this.ip = internetIp;
    }

    public static <T> ResResult<T> success() {
        return new ResResult<>(ResultCode.SUCCESS, true);
    }

    public static <T> ResResult<T> success(T data) {
        return new ResResult<>(ResultCode.SUCCESS, data, true);
    }

    public static <T> ResResult<T> success(IResultCode resultCode) {
        return new ResResult<>(resultCode, true);
    }

    public static <T> ResResult<T> success(IResultCode resultCode, T data) {
        return new ResResult<>(resultCode, data, true);
    }

    public static <T> ResResult<T> success(IResultCode resultCode, String bizMessage) {
        return new ResResult<>(resultCode, null, true, bizMessage);
    }

    public static <T> ResResult<T> success(IResultCode resultCode, T data, String bizMessage) {
        return new ResResult<>(resultCode, data, true, bizMessage);
    }

    public static <T> ResResult<T> failure() {
        return new ResResult<>(ResultCode.FAILED, true);
    }

    public static <T> ResResult<T> failure(String bizMessage) {
        return new ResResult<>(ResultCode.FAILED, null, false, bizMessage);
    }

    public static <T> ResResult<T> failure(IResultCode resultCode) {
        return new ResResult<>(resultCode, false);
    }

    public static <T> ResResult<T> failure(IResultCode resultCode, String bizMessage) {
        return new ResResult<>(resultCode, null, false, bizMessage);
    }

    public static <T> ResResult<T> failure(IResultCode resultCode, T data) {
        return new ResResult<>(resultCode, data, false);
    }

    public static <T> ResResult<T> failure(IResultCode resultCode, T data, String bizMessage) {
        return new ResResult<>(resultCode, data, false, bizMessage);
    }

}
