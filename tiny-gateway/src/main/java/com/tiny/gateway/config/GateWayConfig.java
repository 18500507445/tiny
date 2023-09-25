package com.tiny.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wzh
 * @description 网关配置类
 * @date: 2023/09/25 13:42
 */

@Data
@RefreshScope
@Configuration
public class GateWayConfig {

    /**
     * 鉴权开关，默认打开
     */
    @Value("${auth.enable:true}")
    private Boolean authEnable;

    /**
     * 放行的url
     */
    @Value("${auth.release.urls:''}")
    private String[] releaseUrls;

    /**
     * 禁止的url
     */
    @Value("${auth.forbidden.urls:''}")
    private String[] forbiddenUrls;

    /**
     * 网关配置，慢接口展示
     */
    @Value("${gateway.slow.enable:true}")
    private Boolean slowEnable;
}
