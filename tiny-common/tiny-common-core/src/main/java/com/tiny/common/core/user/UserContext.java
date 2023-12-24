package com.tiny.common.core.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.NamedThreadLocal;

/**
 * @author: wzh
 * @description 用户上下文
 * @date: 2023/10/12 13:43
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserContext {

    //ThreadLocal是与线程绑定的，在并发情况下，使用ThreadLocal存储用户信息通常是安全的，不会发生数据串行的问题，因为每个线程都有独立的数据副本
    public static final ThreadLocal<UserToken> USER_CONTEXT = new NamedThreadLocal<>("User Context");

    @Data
    public static class UserToken {
        private String id;

        private String name;
    }

    public static void set(UserToken userToken) {
        USER_CONTEXT.set(userToken);
    }

    public static void remove() {
        USER_CONTEXT.remove();
    }

    public static UserToken get() {
        return USER_CONTEXT.get();
    }
}
