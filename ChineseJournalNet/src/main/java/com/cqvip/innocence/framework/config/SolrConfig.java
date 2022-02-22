package com.cqvip.innocence.framework.config;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

/**
 * @Author eternal
 * @Date 2022/2/8
 * @Version 1.0
 */
@Configuration
public class SolrConfig {

    @Autowired
    SolrClient solrClient;

    @Bean
    public SolrTemplate getSolrTemplate() {
        return new SolrTemplate(solrClient);
    }

}