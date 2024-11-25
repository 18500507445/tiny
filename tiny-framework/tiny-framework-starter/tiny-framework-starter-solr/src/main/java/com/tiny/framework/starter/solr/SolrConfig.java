package com.tiny.framework.starter.solr;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author: wzh
 * @description: solr配置类
 * @date: 2024/11/22 10:04
 */
@RefreshScope
@Getter
@Configuration
public class SolrConfig {

    /**
     * @description: 示例 collectionMap: '{"mdse_base":"mdse_base"}'
     */
    @Value("#{${spring.data.solr.collectionMap:null}}")
    private Map<String, String> collectionMap;
}
