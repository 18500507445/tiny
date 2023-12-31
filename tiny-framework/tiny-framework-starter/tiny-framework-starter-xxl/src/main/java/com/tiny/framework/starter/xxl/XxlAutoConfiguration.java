package com.tiny.framework.starter.xxl;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: xxl-job自动装配配置类
 * @author: wzh
 * @date: 2023/8/23 22:12
 */
@Slf4j(topic = "tiny-framework-starter ==> XxlAutoConfiguration")
@Configuration
@EnableConfigurationProperties({XxlProperties.class})
public class XxlAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public XxlJobExecutor xxlJobExecutor(XxlProperties properties) {
        XxlProperties.AdminProperties admin = properties.getAdmin();
        XxlProperties.ExecutorProperties executor = properties.getExecutor();
        if (null != admin && null != executor) {
            log.warn("装配【XxlJobExecutor】");
            // 初始化执行器
            XxlJobExecutor xxlJobExecutor = new XxlJobSpringExecutor();
            xxlJobExecutor.setIp(executor.getIp());
            xxlJobExecutor.setPort(executor.getPort());
            xxlJobExecutor.setAppname(executor.getAppName());
            xxlJobExecutor.setLogPath(executor.getLogPath());
            xxlJobExecutor.setLogRetentionDays(executor.getLogRetentionDays());
            xxlJobExecutor.setAdminAddresses(admin.getAddresses());
            xxlJobExecutor.setAccessToken(properties.getAccessToken());
            return xxlJobExecutor;
        }
        return null;
    }
}
