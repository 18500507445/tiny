package com.tiny.common.core.exception;

import com.tiny.common.core.result.RespResult;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author: wzh
 * @description 全局异常拦截器
 * @date: 2023/11/02 09:46
 */
@Slf4j(topic = "GlobalExceptionAdvice")
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @Setter
    private static boolean runtimeLog = false;

    @Setter
    private static boolean paramLog = false;

    @Setter
    private static boolean bindLog = false;


    /**
     * 拦截的validator异常
     *
     * @param e
     */
    @ExceptionHandler(BindException.class)
    public RespResult handleBindException(BindException e) {
        if (bindLog) {
            log.error("handleBindException：{}", e.getMessage());
        }
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String message = fieldError.getDefaultMessage();
            msg.append(message);
        }
        return RespResult.error(msg.toString());
    }

    /**
     * 自定义参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ParamException.class)
    public RespResult paramException(ParamException e) {
        if (paramLog) {
            log.error("paramException :{}", e.getMessage(), e);
        }
        return RespResult.error(e.getMessage());
    }

    /**
     * 运行异常
     */
    @ExceptionHandler(RuntimeException.class)
    public RespResult runtimeException(RuntimeException e) {
        if (runtimeLog) {
            log.error("runtimeException :{}", e.getMessage(), e);
        }
        return RespResult.error(e.getMessage());
    }
}
