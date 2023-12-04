package com.tiny.common.core.exception;

import lombok.NoArgsConstructor;

/**
 * @author: wzh
 * @description 参数校验异常
 * @date: 2023/08/29 14:33
 */
@NoArgsConstructor
public class ParamException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ParamException(String message) {
        super(message);
    }

    public ParamException(int code, String message) {
        super(message);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
