package com.tiny.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "gateway")
public class GateWayConfig {

    /**
     * 鉴权开关，默认打开
     */
    private Boolean authEnable = true;

    /**
     * 放行的url
     */
    private String[] releaseUrls;

    /**
     * 禁止的url
     */
    private String[] forbiddenUrls;

    /**
     * ip黑名单
     */
    private String[] ipBlackList;

    /**
     * 网关配置，慢接展示开关，默认打开
     */
    private Boolean slowEnable = true;

    /**
     * 网关配置，慢接口毫秒界限值，默认1000
     */
    private Long slowMillisecond = 1000L;

}
