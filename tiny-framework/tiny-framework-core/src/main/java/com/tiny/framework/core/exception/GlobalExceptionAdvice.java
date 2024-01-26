package com.tiny.framework.core.exception;

import com.tiny.framework.core.result.ResResult;
import com.tiny.framework.core.result.ResultCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author: wzh
 * @description: 全局异常拦截器
 * @date: 2023/11/02 09:46
 */
@Slf4j(topic = "tiny-framework-starter ==> GlobalExceptionAdvice")
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @Setter
    private static boolean businessLog = false;

    @Setter
    private static boolean bindLog = false;

    /**
     * 拦截的validator异常
     */
    @ExceptionHandler(BindException.class)
    public ResResult<Void> handleBindException(BindException e) {
        if (bindLog) {
            log.error("handleBindException：{}", e.getMessage());
        }
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String message = fieldError.getDefaultMessage();
            msg.append(message);
        }
        return ResResult.failure(ResultCode.VALIDATE_FAILED, msg.toString());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResResult<Void> businessException(BusinessException e) {
        if (businessLog) {
            log.error("businessException :{}", e.getMessage(), e);
        }
        return ResResult.failure(ResultCode.FAILED, e.getMessage());
    }

}
