package com.cqvip.innocence.project.task;

import com.cqvip.innocence.common.constant.SolrCollections;
import com.cqvip.innocence.project.service.SearchService;
import com.finemi.support.solr.SolrSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 统计总数量定时任务
 * @Author eternal
 * @Date 2022/1/25
 * @Version 1.0
 */
@Component
public class CountTask {

    public static long articleNumber = 0L;

    @PostConstruct
    @Scheduled(cron = "0 0 0/1 * * ?") //测试
    public void count(){
        SolrSession session = SolrSession.Get(SolrCollections.TITLE);
        int count = session.count("*:*", "@region#rt=2:13");
        articleNumber = count;
    }
}