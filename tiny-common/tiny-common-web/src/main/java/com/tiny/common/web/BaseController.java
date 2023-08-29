package com.tiny.common.web;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tiny.common.tool.common.IpUtils;
import com.tiny.common.tool.common.ServletUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: wzh
 * @description 通用Controller
 * @date: 2023/08/29 10:56
 */
@RefreshScope
public class BaseController {

    /**
     * 获取request
     */
    public HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    /**
     * 获取请求连接参数
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        String value = "";
        HttpServletRequest request = getRequest();
        if (ObjectUtil.isNotNull(request)) {
            value = request.getParameter(key);
            if (ObjectUtil.isEmpty(value)) {
                value = request.getHeader(key);
            }
        }
        return value;
    }

    /**
     * 获取用户请求IP地址
     */
    public String getIp() {
        String result = "";
        String createIp = IpUtils.getIpAddr(getRequest());
        if (StrUtil.isNotBlank(createIp)) {
            String[] split = createIp.split(",");
            if (split.length > 0) {
                if (StrUtil.isNotBlank(split[0])) {
                    result = split[0];
                }
            }
        }
        return result.trim();
    }

    /**
     * 获取机器IP地址
     */
    public String getHostIp() {
        String createIp = IpUtils.getIpAddr(getRequest());
        if (StrUtil.isNotBlank(createIp)) {
            String[] split = createIp.split(",");
            if (split.length > 1) {
                if (StrUtil.isNotBlank(split[1])) {
                    return split[1];
                }
            }
        }
        return "";
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }
}
