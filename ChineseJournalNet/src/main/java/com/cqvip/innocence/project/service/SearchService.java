package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.SearchParam;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * @Author eternal
 * @Date 2021/12/24
 * @Version 1.0
 */
public interface SearchService {

    JsonResult search(SearchParam searchParam,Long userId) throws IOException, SolrServerException;

    List<String> suggest(String value);

    /**
     * 根据查询参数获取聚类结果(由于查询和聚类同时进行会导致速度过慢，因此拆分开)
     *
     * @param searchParam
     * @return
     */
    JsonResult getFacet(SearchParam searchParam) throws IOException, SolrServerException;

    JsonResult getSubFacet(SearchParam searchParam, String field, String solrCollection, String queryKey, String valueKey) throws IOException, SolrServerException;
}
