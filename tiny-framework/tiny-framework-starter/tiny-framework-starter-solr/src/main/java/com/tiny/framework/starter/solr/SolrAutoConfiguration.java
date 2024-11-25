package com.tiny.framework.starter.solr;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.autoconfigure.solr.SolrProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description: solr自动配置类
 * @date: 2024/11/22 09:42
 */
@Slf4j(topic = "tiny-framework-starter ==> SolrAutoConfiguration")
@Configuration
@EnableConfigurationProperties({SolrProperties.class})
public class SolrAutoConfiguration {

    @Resource
    private SolrProperties solrProperties;

    @Resource
    private SolrConfig solrConfig;

    @Bean
    public SolrClient solrClient() {
        if (ObjectUtil.hasEmpty(solrProperties, solrProperties.getHost(), solrConfig.getCollectionMap())) {
            return null;
        }
        log.warn("装配【SolrClient】当前文档集合：{}", JSONObject.toJSONString(solrConfig.getCollectionMap()));
        return new HttpSolrClient.Builder(solrProperties.getHost()).build();
    }
}
