package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.SearchParam;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;


/**
 * @ClassName MediaFacetsService
 * @Description 期刊聚类service
 * @Author Innocence
 * @Date 2021/12/28 10:17
 * @Version 1.0
 */
public interface MediaService {

    /**
     * 获取期刊的聚类数据(第一级聚类，包括：核心期刊，国内外数据库收录，地区，主题)
     * @author Innocence
     * @date 2021/12/28
     * @param param
     * @return java.lang.String
     */
    JsonResult getMediaFacet(SearchParam param);

    /**
     * 期刊检索
     * @author Innocence
     * @date 2021/12/31
     * @param param
     * @return com.cqvip.innocence.project.model.dto.JsonResult
     */
    JsonResult getMediaPage(SearchParam param) throws IOException, SolrServerException;
}
