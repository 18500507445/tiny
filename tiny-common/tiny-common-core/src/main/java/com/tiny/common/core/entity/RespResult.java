package com.tiny.common.core.entity;


import java.util.HashMap;
import java.util.Objects;

/**
 * 返回实体类
 */
public class RespResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";

    public static final String MSG_TAG = "msg";

    public static final String DATA_TAG = "data";

    public static final String FUNCTION_TAG = "function";

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
    private String code;

    /**
     * 返回内容
     */
    private String msg;

    /**
     * 数据对象
     */
    private Object data;

    /**
     * 获取请求过来的参数，取出api中的function名称，添加到返回值中
     */
    protected String getApiRequestFunction() {
        return "";
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public RespResult() {

    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     */
    public RespResult(Type type, String msg) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        super.put(FUNCTION_TAG, getApiRequestFunction());
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param msg  返回内容
     * @param data 数据对象
     */
    public RespResult(String msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        super.put(FUNCTION_TAG, getApiRequestFunction());
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
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
        super.put(FUNCTION_TAG, getApiRequestFunction());
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
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
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态编码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public RespResult(String code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        super.put(FUNCTION_TAG, getApiRequestFunction());
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
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
     * @param msg 返回内容
     * @return 成功消息
     */
    public static RespResult success(String msg) {
        return RespResult.success(msg, null);
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
     * @param code 返回码
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static RespResult success(String code, String msg, Object data) {
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
    public static RespResult error(String code, String msg, Object data) {
        return new RespResult(code, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code
     * @param msg
     * @return
     */
    public static RespResult error(String code, String msg) {
        return new RespResult(code, msg, null);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
