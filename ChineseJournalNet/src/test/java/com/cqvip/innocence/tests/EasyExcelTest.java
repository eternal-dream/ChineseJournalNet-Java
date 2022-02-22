//package com.cqvip.innocence.tests;
//
//import com.alibaba.excel.EasyExcel;
//import com.cqvip.innocence.project.model.Article;
//import com.finemi.support.model.dto.Page;
//import com.finemi.support.model.dto.PageBean;
//import com.finemi.support.solr.SolrSession;
//import com.finemi.support.solr.dto.SolrCriterion;
//import com.finemi.support.solr.enums.SolrOperator;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @ClassName EasyExcelTest
// * @Description TODO
// * @Author Innocence
// * @Date 2020/9/15 17:23
// * @Version 1.0
// */
//@SpringBootTest
//@Service
//public class EasyExcelTest {
//
//    @Test
//    public void test1(){
//
//        SolrCriterion get = SolrCriterion.Get();
//        get.addFilter(SolrOperator.LIKE,"title","cad");
//        SolrSession allObj = SolrSession.Get("allObj");
//        PageBean<Article> pageBean = allObj.getPageBean(Article.class, get, Page.Get(0, 10));
//        System.out.println("123");
//    }
//
//}
