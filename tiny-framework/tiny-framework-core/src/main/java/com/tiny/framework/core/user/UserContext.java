package com.tiny.framework.core.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.tiny.framework.core.exception.BusinessException;
import com.tiny.framework.core.utils.common.JwtUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.NamedThreadLocal;

import java.util.Map;

/**
 * @author: wzh
 * @description: 用户上下文
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


    /**
     * @description: 从threadLocal获取UserToken
     */
    public static UserToken get() {
        return USER_CONTEXT.get();
    }

    /**
     * @description: 手动获取UserToken
     */
    public static UserToken getUserToken(String authorization) {
        boolean tokenExpired = JwtUtils.isTokenExpired(authorization);
        if (tokenExpired) {
            throw new BusinessException("token过期");
        }
        //解析token
        Map<String, Claim> parse = JwtUtils.parse(authorization);
        String userContext = parse.get("json").asString();
        if (StrUtil.isNotBlank(userContext)) {
            return JSONObject.parseObject(userContext, UserContext.UserToken.class);
        }
        return null;
    }
}
