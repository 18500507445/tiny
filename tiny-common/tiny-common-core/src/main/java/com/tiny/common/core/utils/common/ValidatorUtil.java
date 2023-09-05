package com.tiny.common.core.utils.common;


import com.tiny.common.core.exception.ParamException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate-validator 自定义校验工具类
 *
 * @author wzh
 * @date 2023/6/22 21:13
 */
public class ValidatorUtil {

    private ValidatorUtil() {

    }

    private static final Validator VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static void validate(Object object, Class<?>... groups) throws ParamException {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                msg.append(constraint.getMessage()).append(";");
            }
            throw new ParamException(1000, msg.toString());
        }
    }


}