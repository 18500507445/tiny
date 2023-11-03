package com.tiny.common.core.user;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.tiny.common.core.Constants;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserFilter implements Filter {


    /**
     * 无论是否userContext有没有值，都放入用户上下文里
     * 禁止判空然后再放入，然后filterChain.doFilter，否则不会进行传递了，那么进不到其它服务了
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) req;
        String userContext = URLUtil.decode(request.getHeader(Constants.USER_CONTEXT));
        UserContext.UserToken userToken = JSONUtil.toBean(userContext, UserContext.UserToken.class);
        try {
            UserContext.set(userToken);
            filterChain.doFilter(req, resp);
        } catch (ServletException | IOException e) {
            log.error("UserFilter异常", e);
        } finally {
            UserContext.remove();
        }
    }

}
