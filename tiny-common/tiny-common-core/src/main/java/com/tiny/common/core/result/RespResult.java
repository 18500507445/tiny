package com.tiny.common.core.result;


import cn.hutool.extra.spring.SpringUtil;
import com.tiny.common.core.trace.Trace;
import com.tiny.common.core.trace.TraceContext;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author: wzh
 * @description 返回实体类
 * @date: 2023/09/21 14:33
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Setter
public class RespResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";

    public static final String MSG_TAG = "message";

    public static final String DATA_TAG = "data";

    public static final String TIME = "systemTime";

    public static final String ENV = "env";

    /**
     * 状态类型
     */
    public enum Type {
        /**
         * 成功
         */
        SUCCESS("0"),
        /**
         * 警告
         */
        WARN("301"),
        /**
         * 错误
         */
        ERROR("500");
        private final String value;


        Type(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    /**
     * 状态类型
     */
    private Type type;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回内容
     */
    private String msg;

    /**
     * 数据对象
     */
    private Object data;

    /**
     * 公网ip 最后2位，方便查找log
     */
    @Setter
    private static String ip = "00";

    /**
     * 私有构造
     */
    private RespResult() {

    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     * @param data 数据对象
     */
    public RespResult(Type type, String msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
        super.put(TIME, System.currentTimeMillis());
        super.put(Trace.TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(Trace.SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(ENV, SpringUtil.getActiveProfile());
        super.put("ip", IP);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态编码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public RespResult(Integer code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
        super.put(TIME, System.currentTimeMillis());
        super.put(Trace.TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(Trace.SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(ENV, SpringUtil.getActiveProfile());
        super.put("ip", IP);
    }

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public RespResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static RespResult success() {
        return RespResult.success("请求成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static RespResult success(Object data) {
        return RespResult.success("请求成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static RespResult success(String msg, Object data) {
        return new RespResult(Type.SUCCESS, msg, data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static RespResult success(Integer code, String msg) {
        return new RespResult(code, msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param code 返回码
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static RespResult success(Integer code, String msg, Object data) {
        return new RespResult(code, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param data 返回内容
     * @return 警告消息
     */
    public static RespResult warn(Object data) {
        return RespResult.warn("警告", data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static RespResult warn(String msg) {
        return RespResult.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static RespResult warn(String msg, Object data) {
        return new RespResult(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static RespResult error() {
        return RespResult.error("请求失败");
    }

    /**
     * 返回错误消息
     *
     * @param data 数据对象
     * @return 警告消息
     */
    public static RespResult error(Object data) {
        return RespResult.error("请求失败", data);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static RespResult error(String msg) {
        return RespResult.error(Type.ERROR.value, msg);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static RespResult error(String msg, Object data) {
        return new RespResult(Type.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 返回编码
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static RespResult error(Integer code, String msg, Object data) {
        return new RespResult(code, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code
     * @param msg
     * @return
     */
    public static RespResult error(Integer code, String msg) {
        return new RespResult(code, msg, null);
    }

}
