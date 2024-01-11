package com.tiny.common.annotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: wzh
 * @description 只能提交指定的值, 自定义校验
 * @date: 2023/01/03 10:15
 */
@Documented
@Constraint(validatedBy = {ListValue.ListValueConstraintValidator.class, ListValue.ListStringValueConstraintValidator.class})
// 指定校验器，这里不指定时，就需要在初始化时指定
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListValue {

    // 默认的提示内容
    String message() default "必须提交指定的值哦";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] intValues() default {}; //数值数组，提交的值只能是数组里面

    String[] strValues() default {}; //字符串数组，提交的值只能是数组里面

    class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

        private final Set<Integer> set = new HashSet<>();

        /**
         * 初始化方法
         */
        @Override
        public void initialize(ListValue constraintAnnotation) {
            int[] values = constraintAnnotation.intValues();
            for (int val : values) {
                set.add(val);
            }
        }

        /**
         * 判断是否校验成功
         */
        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {
            return set.contains(value);
        }
    }

    class ListStringValueConstraintValidator implements ConstraintValidator<ListValue, String> {

        private final Set<String> set = new HashSet<>();

        /**
         * 初始化方法
         */
        @Override
        public void initialize(ListValue constraintAnnotation) {
            String[] values = constraintAnnotation.strValues();
            Collections.addAll(set, values);
        }

        /**
         * 判断是否校验成功
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return set.contains(value);
        }
    }
}