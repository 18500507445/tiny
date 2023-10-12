package com.tiny.common.core.user;

import com.alibaba.fastjson2.JSONObject;
import com.tiny.common.core.Constants;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author: wzh
 * @description 用户过滤器
 * @date: 2023/10/12 13:59
 */
@Component
public class UserFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) req;
        String userContext = request.getHeader(Constants.USER_CONTEXT);
        UserContext.UserToken userToken = JSONObject.parseObject(userContext, UserContext.UserToken.class);
        try {
            UserContext.set(userToken);
            filterChain.doFilter(req, resp);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            UserContext.remove();
        }
    }

}
