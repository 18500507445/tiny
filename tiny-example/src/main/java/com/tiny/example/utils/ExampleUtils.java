package com.tiny.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author: wzh
 * @description 样例工具类
 * 强制：私有构造，所有方法进行静态处理，禁止new工具类使用
 * @date: 2023/11/29 15:34
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleUtils {

    public static String demo() {
        return "";
    }
}
