package com.tiny.common.core.user;

import lombok.Data;
import org.springframework.core.NamedThreadLocal;

/**
 * @author: wzh
 * @description 用户上下文
 * @date: 2023/10/12 13:43
 */
public class UserContext {

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
