package com.cqvip.innocence.project.controller.front;

import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.Article;
import com.cqvip.innocence.project.model.dto.JsonResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author eternal
 * @Date 2021/12/22
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/search2")
public class SearchTestController extends AbstractController {

 @Autowired
 @Lazy
 private SolrClient client;

 @PostMapping("search")
 public JsonResult search() throws IOException, SolrServerException {
  SimpleQuery simpleQuery = new SimpleQuery("title:cad");
  SolrQuery query = new SolrQuery();
  query.setQuery("keyword_c:cad");
  QueryResponse query1 = client.query( "title_info",query);
  return JsonResult.Get();
 }
}