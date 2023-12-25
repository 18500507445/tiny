package com.tiny.example.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: wzh
 * @description 样例事件枚举类
 * @date: 2023/11/29 15:40
 */
@Getter
@AllArgsConstructor
public enum ExampleEventEnum {

    ONE(1, "【example】模块 - xxx干的事", "发送一个MQ消息"),

    ;

    //id
    private final Integer id;

    //名称
    private final String name;

    //描述
    private final String description;

    public static ExampleEventEnum getEvent(Integer id) {
        return EnumUtil.getBy(ExampleEventEnum::getId, id, ONE);
    }
}
