package com.cqvip.innocence.project.controller.front;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.common.annotation.VisitLog;
import com.cqvip.innocence.common.constant.SolrCollections;
import com.cqvip.innocence.common.enums.LogicOperator;
import com.cqvip.innocence.common.enums.SearchField;
import com.cqvip.innocence.common.exception.QuerySyntaxErrorException;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.SearchModel;
import com.cqvip.innocence.project.model.dto.SearchParam;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.service.SearchService;
import com.cqvip.innocence.project.service.impl.SearchServiceImpl;
import com.cqvip.innocence.project.task.CountTask;
import com.finemi.support.model.dto.Page;
import com.finemi.support.model.dto.PageObject;
import com.finemi.support.solr.SolrSession;
import com.finemi.support.solr.dto.Sort;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author eternal
 * @Date 2021/12/9
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/search")
public class SearchController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SearchService searchService;


    @PostMapping("search")
    @VisitLog(content = "检索",plate = "文章检索")
    public JsonResult search(@RequestBody SearchParam searchParam, HttpServletRequest request) {
        UserInfo currentUser = (UserInfo) redisTemplate.opsForValue().get(request.getSession().getId());
        try {
            return searchService.search(searchParam,currentUser.getId());
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.Get("pageBean",new HashMap<>());
        }
    }

    @PostMapping("advanceSearch")
    @VisitLog(content = "检索",plate = "高级检索")
    public JsonResult advanceSearch(@RequestBody SearchParam searchParam,HttpServletRequest request) {
        return search(searchParam,request);
    }

    @GetMapping("autoComplete")
    public JsonResult autoComplete(String value) {
        List<String> suggests = new ArrayList<>();
        try {
            suggests = searchService.suggest(value);
        }catch (Exception e){}
        return JsonResult.Get("suggests", suggests);
    }

    @PostMapping("getFacet")
    public JsonResult getFacet(@RequestBody SearchParam searchParam) {
        try {
            return searchService.getFacet(searchParam);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.Get("facet",new HashMap<>());
        }
    }

    //分类信息二次聚类
    @PostMapping("getClassSubFacet")
    public JsonResult getClassSubFacet(@RequestBody SearchParam searchParam) {
        try {
            return searchService.getSubFacet(searchParam, "classids_g2", SolrCollections.CLASS, "classid", "classtypename");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return JsonResult.Get();
    }

    @PostMapping("getOrganSubFacet")
    public JsonResult getOrganSubFacet(@RequestBody SearchParam searchParam){
        try {
            return searchService.getSubFacet(searchParam, "organids_g2", SolrCollections.ORGAN, "organid", "organ");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return JsonResult.Get();
    }

    @PostMapping("getArticlesByIds")
    @ApiOperation("根据ids获取文献列表")
    public JsonResult getArticlesByIds(@RequestBody List<String> ids,HttpServletRequest request) throws IOException, SolrServerException {
        if (ids.isEmpty()){
            return JsonResult.Get(false);
        }
        UserInfo currentUser = (UserInfo) redisTemplate.opsForValue().get(request.getSession().getId());

        List<SearchModel> models = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            SearchModel model = new SearchModel();
            model.setExact(true);
//            if (i > 0){
                model.setLogicOperator(LogicOperator.OR);
//            }
            model.setSearchField(SearchField.LNGID);
            model.setValue(ids.get(i));
            models.add(model);
        }
        SearchParam searchParam = new SearchParam();
        searchParam.setSearchModels(models);
        searchParam.setPageNum(1);
        searchParam.setPageSize(ids.size());
        searchParam.setSearchByExpression(false);
        return searchService.search(searchParam,currentUser.getId());
    }

    @VisitLog(content = "访问首页",plate = "首页")
    @GetMapping("getArticleNumber")
    @NoLogin
    public JsonResult getArticleNumber(){
        return JsonResult.Get("number",CountTask.articleNumber);
    }

//    @PostMapping("test")
//    public JsonResult test(@RequestBody SearchParam searchParam) throws IOException, SolrServerException {
//        Page page = new Page(searchParam.getPageNum(), searchParam.getPageSize());
//
//        SolrSession session = SolrSession.Get(SolrCollections.TITLE);
//        JsonResult result = JsonResult.Get();
//        try {
//            searchParam = SearchServiceImpl.defineQueryString(searchParam);
//        }catch (QuerySyntaxErrorException e){
//            return JsonResult.Get(false,"检索式语法错误!");
//        }
//        String query = searchParam.getSearchExpression();
//        //排序
//        List<Sort> sorts = new ArrayList<>();
//        if (StringUtils.isNotBlank(searchParam.getSortField())) {
//            sorts.add(new Sort(searchParam.getSortField(), searchParam.isAsc()));
//        }
//        if (StringUtils.isBlank(query)) {
//            query = "*:*";
//        }
//        if(StringUtils.isNotBlank(searchParam.getIncludeRule())){
//            query+=" AND "+searchParam.getIncludeRule();
//        }
//        //从title_info查出文章数据
//        session.setQueryOperator("AND");
//        PageObject<Map<String, Object>> pageObject = new PageObject<>();
////        PageObject<Map<String, Object>> pageObject1 = session.getPageObject(query, getFq(), page, sorts);
//        SolrQuery solrQuery = new SolrQuery(query);
//        solrQuery.setStart((searchParam.getPageNum()-1)*searchParam.getPageSize());
//        solrQuery.setRows(searchParam.getPageSize());
//        solrQuery.addFacetField("gch","classids_g1", "subjectids_s", "writerids_s");
//        solrQuery.setFacetLimit(50);
////            solrQuery.setHighlight(true);
////        solrQuery.setHighlightSimplePost("title_c");
////        solrQuery.setHighlightSimplePre("title_c");
////            solrQuery.setFilterQueries("@region#rt=2:13","@region#rt=2:13");
////            solrQuery.setHighlightSimplePre("<font style='red'>");
////            solrQuery.setHighlightSimplePost("</font>");
//        QueryResponse response = solrClient.query("title_info", solrQuery);
////            List<PageObject> beans = response.getBeans(PageObject.class);
//        pageObject.setRows((List)response.getResults());
//        pageObject.setTotal((int)response.getResults().getNumFound());
//        List<FacetField> facetFields = response.getFacetFields();
//        JSONArray array = JSONArray.parseArray(JSON.toJSONString(facetFields));
//        Map map = new HashMap();
//        map.put("aaa",123);
//        map.put("vvv",111);
//        return JsonResult.Get("data",map).put("data2",facetFields.get(0).getValues().get(0).getCount());
//    }
}
