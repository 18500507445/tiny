package com.tiny.framework.core.exception;

import cn.hutool.core.util.StrUtil;
import com.tiny.framework.core.result.base.ResResult;
import com.tiny.framework.core.result.base.ResultCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author: wzh
 * @description: 全局异常拦截器
 * @date: 2023/11/02 09:46
 */
@Slf4j(topic = "tiny-framework-core ==> GlobalExceptionAdvice")
@RestControllerAdvice
public class GlobalExceptionAdvice {

    //参数校验异常log开关
    @Setter
    private static boolean businessLog = false;

    //业务异常log开关
    @Setter
    private static boolean bindLog = false;

    /**
     * 拦截的validator异常（场景：controller中请求的参数）
     */
    @ExceptionHandler({BindException.class})
    public ResResult<Void> handleBindException(BindException e) {
        if (bindLog) {
            log.warn("handleBindException：{}", e.getMessage());
        }
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String message = fieldError.getDefaultMessage();
            msg.append(message).append("，");
        }
        return ResResult.failure(ResultCode.VALIDATE_FAILED, StrUtil.subWithLength(msg.toString(), 0, msg.length() - 1));
    }

    /**
     * 拦截接口层validator（场景：接口类上添加@Validated注解，方法参数上添加正常注解）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResResult<Void> handleConstraintViolationException(ConstraintViolationException e) {
        if (bindLog) {
            log.warn("handleConstraintViolationException：{}", e.getMessage());
        }
        return ResResult.failure(ResultCode.VALIDATE_FAILED, e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResResult<Void> businessException(BusinessException e) {
        ResultCode resultCode = e.getResultCode();
        if (null == resultCode) {
            if (businessLog) {
                log.error("businessException :{}", e.getMessage(), e);
            }
            return ResResult.failure(ResultCode.FAILED, e.getMessage());
        } else {
            if (businessLog) {
                log.error("businessException :{}", resultCode.getBizMessage(), e);
            }
            //返回传入的枚举
            return ResResult.failure(resultCode);
        }
    }

    /**
     * 父类异常
     */
    @ExceptionHandler(Exception.class)
    public ResResult<Void> exception(Exception e) {
        log.error("exception :", e);
        return ResResult.failure(ResultCode.FAILED, e.getMessage());
    }

}
