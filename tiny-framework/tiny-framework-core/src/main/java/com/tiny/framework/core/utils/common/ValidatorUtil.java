package com.tiny.framework.core.utils.common;


import com.tiny.framework.core.exception.BusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author: wzh
 * @date: 2023/6/22 21:13
 * @description: hibernate-validator 自定义校验工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatorUtil {

    private static final Validator VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static void validate(Object object, Class<?>... groups) throws BusinessException {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                msg.append(constraint.getMessage()).append(";");
            }
            throw new BusinessException(1000, msg.toString());
        }
    }


}