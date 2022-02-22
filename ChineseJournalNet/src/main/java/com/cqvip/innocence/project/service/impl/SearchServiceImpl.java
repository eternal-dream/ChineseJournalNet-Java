package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.constant.SolrCollections;
import com.cqvip.innocence.common.enums.LogicOperator;
import com.cqvip.innocence.common.enums.SearchField;
import com.cqvip.innocence.common.exception.QuerySyntaxErrorException;
import com.cqvip.innocence.common.util.solr.FacetUtil;
import com.cqvip.innocence.project.model.Article;
import com.cqvip.innocence.project.model.dto.*;
import com.cqvip.innocence.project.model.entity.PeriodInfo;
import com.cqvip.innocence.project.model.entity.ShieldInfo;
import com.cqvip.innocence.project.service.PeriodInfoService;
import com.cqvip.innocence.project.service.SearchService;
import com.cqvip.innocence.project.service.ShieldInfoService;
import com.cqvip.innocence.project.service.UserLogService;
import com.finemi.support.model.dto.Entry;
import com.finemi.support.model.dto.Page;
import com.finemi.support.model.dto.PageBean;
import com.finemi.support.model.dto.PageObject;
import com.finemi.support.model.dto.jpa.criterion.Criterion;
import com.finemi.support.solr.SolrSession;
import com.finemi.support.solr.dto.HighlightParam;
import com.finemi.support.solr.dto.Sort;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author eternal
 * @Date 2021/12/24
 * @Version 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    UserLogService userLogService;

    @Autowired
    private PeriodInfoService periodInfoService;

    @Autowired
    private MediaServiceImpl mediaFacetsService;

    @Value("${rule.title_info.allow}")
    private String allowRule;

    @Value("${rule.title_info.shield}")
    private String shield;

    @Autowired
    private SolrClient solrClient;

    @Autowired
    ShieldInfoService shieldInfoService;

    @Override
    public JsonResult search(SearchParam searchParam,Long userId) throws IOException, SolrServerException {
        Page page = new Page(searchParam.getPageNum(), searchParam.getPageSize());

        SolrSession session = SolrSession.Get(SolrCollections.TITLE);
        JsonResult result = JsonResult.Get();
        try {
            searchParam = defineQueryString(searchParam);
        } catch (QuerySyntaxErrorException e) {
            return JsonResult.Get(false, "检索式语法错误!");
        }
        String query = searchParam.getSearchExpression();
        //排序
//        List<Sort> sorts = new ArrayList<>();
//        if (StringUtils.isNotBlank(searchParam.getSortField())) {
//            sorts.add(new Sort(searchParam.getSortField(), searchParam.isAsc()));
//        }
        if (StringUtils.isBlank(query)) {
            query = "*:*";
        }
        if (StringUtils.isNotBlank(searchParam.getIncludeRule())) {
            query += " AND " + searchParam.getIncludeRule();
        }
        //从title_info查出文章数据
        session.setQueryOperator("AND");
        PageObject<Map<String, Object>> pageObject = new PageObject<>();
//        PageObject<Map<String, Object>> pageObject1 = session.getPageObject(query, getFq(), page, sorts);
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setStart((searchParam.getPageNum() - 1) * searchParam.getPageSize());
        solrQuery.setRows(searchParam.getPageSize());
        if (StringUtils.isNotBlank(searchParam.getSortField())) {
            solrQuery.setSort(searchParam.getSortField(), searchParam.isAsc() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
        }
        solrQuery.addFilterQuery(getFq().toArray(new String[getFq().size()]));
        solrQuery.addFacetField("gch", "classids_g1", "subjectids_s", "writerids_s");
        solrQuery.setFacetLimit(50);
        QueryResponse response = solrClient.query(SolrCollections.TITLE, solrQuery);
        pageObject.setRows((List) response.getResults());
        pageObject.setTotal((int) response.getResults().getNumFound());
        pageObject.setPage(searchParam.getPageNum());
        List<FacetField> facetFields = response.getFacetFields();
//  List<FacetField> facetFields = session.facetField(query, null, null);
        // 文章主体内容ndo_content为字符串，此处转为json对象
        for (Map<String, Object> item : pageObject.getRows()) {
//   Doc doc = BeanUtil.mapToBean(item, Doc.class, true, CopyOptions.create());
            String ndo_content = item.get("ndo_content").toString();
            JSONObject jsonObject = JSONObject.parseObject(ndo_content);
            if (!searchParam.getSearchByExpression()) {
                String value = searchParam.getSearchModels().get(0).getValue();
                jsonObject.forEach((k, v) -> {
                    v = StrUtil.replaceIgnoreCase(v.toString(), value, "<span style='color:#dd0000'>" + value + "</span>");
                });
//                ndo_content = StrUtil.replaceIgnoreCase(ndo_content,searchParam.getSearchModels().get(0).getValue(),"<span style='color:#dd0000'>"+searchParam.getSearchModels().get(0).getValue().toUpperCase()+"</span>");
            }
            CheckResult checkResult = userLogService.readLimitCheck(userId, (String)jsonObject.get("lngid"));
            jsonObject.put("userDatabaseInfoInvalid", checkResult.getLimited());
            Set<String> highlightValues = searchParam.getHighlightValues();
            if (highlightValues != null) {
                for (String value : highlightValues) {
                    jsonObject.forEach((k, v) -> {
                        if ("title_c;title_e;keyword_c;keyword_e;remark_c;writer_text;author_e;showwriter;organ_text;organ_e;showorgan;media_c;media_e;wname_s;imburse".contains(k)) {
                            v = StrUtil.replaceIgnoreCase(v.toString(), value, "<span style='color:#dd0000'>" + value.toLowerCase() + "</span>");
                        }
                        jsonObject.put(k, v);

                    });
//                    ndo_content = StrUtil.replaceIgnoreCase(ndo_content, value, "<span style='color:#dd0000'>" + value + "</span>");
                }
            }
            item.put("ndo_content", jsonObject);
        }
        ;
        List<String> idList = pageObject.getRows().stream().map(i -> i.get("id").toString().split("-")[0]).collect(Collectors.toList());
        LambdaQueryWrapper<ShieldInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ShieldInfo::getLngid, idList);
        List<ShieldInfo> list = shieldInfoService.list(wrapper);
        pageObject.setRows(pageObject.getRows().stream().filter(i -> {
            String id = i.get("id").toString().split("-")[0];
            boolean flag = list.stream().filter(j -> j.getLngid().equals(id)).collect(Collectors.toList()).size() > 0;
            return !flag;
        }).collect(Collectors.toList()));
        result.put("pageBean", pageObject);
        return result;
    }

    @Override
    public List<String> suggest(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        SolrSession session = SolrSession.Get(SolrCollections.TITLE);
        List<String> suggest = session.suggest(value, 10, false);
        return suggest;
    }

    @Override
    public JsonResult getFacet(SearchParam searchParam) throws IOException, SolrServerException {
        Map<String, Map<String, String>> facet = new HashMap<>();
        String query = null;
        try {
            query = defineQueryString(searchParam).getSearchExpression();
        } catch (QuerySyntaxErrorException e) {
//            return JsonResult.Get(false,"检索式语法错误!");
            return JsonResult.Get();
        }
        if (StringUtils.isBlank(query)) {
            query = "*:*";
        }
        if (StringUtils.isNotBlank(searchParam.getIncludeRule())) {
            query += " AND " + searchParam.getIncludeRule();
        }
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setStart(0);
        solrQuery.setRows(0);
        solrQuery.addFilterQuery(getFq().toArray(new String[getFq().size()]));
        solrQuery.addFacetField("gch", "classids_g1", "subjectids_s", "writerids_s","mediaid", "organids_g1","years","range");
        solrQuery.setFacetLimit(80);
        QueryResponse response = solrClient.query(SolrCollections.TITLE, solrQuery);
        response.getFacetFields().forEach(item->{
            HashMap<String,String> facetMap = new HashMap<>();
            item.getValues().stream().forEach(i -> {
                if(i.getCount() != 0){
                    facetMap.put(i.getName(), i.getCount() + "");
                }
            });
            facet.put(item.getName(),facetMap);
        });
//        facet = FacetUtil.getFacet(SolrCollections.TITLE, query, getFq().get(0), 35, 0, "gch", "classids_g1", "subjectids_s", "writerids_s", "mediaid", "organids_g1");
//        facet.putAll(FacetUtil.getFacet(SolrCollections.TITLE, query, getFq().get(0), 80, 0, "years"));
//        facet.putAll(FacetUtil.getFacet(SolrCollections.TITLE, query, getFq().get(0), 100, 0, "range"));
        List<Map<String, String>> classFacet = defineFacetResult(facet.get("classids_g1"), SolrCollections.CLASS, "classid", "classtypename");
        List<Map<String, String>> subjectFacet = defineFacetResult(facet.get("subjectids_s"), SolrCollections.SUBJECT, "subjectid", "subject");
        List<Map<String, String>> writerFacet = defineFacetResult(facet.get("writerids_s"), SolrCollections.WRITER, "writerid", "writer");
        //此处从solr中查出的gch为小写= =
        List<Map<String, String>> mediaFacet = defineMediaFacetResult(facet.get("gch"));
        List<Map<String, String>> organFacet = defineFacetResult(facet.get("organids_g1"), SolrCollections.ORGAN, "organid", "organ");
        Map<String, List<Map<String, Object>>> range = null;
        if (facet.get("range") != null) {
            range = mediaFacetsService.getLngrangeName(facet.get("range"));
        }
        List<Map<String, Object>> rangeFacet = range.get("cMap");
        rangeFacet.addAll(range.get("oMap"));
        Map<String, List<FacetItem>> result = new HashMap();

        if (classFacet != null) {
            result.put("classFacet", classFacet.stream().map(e -> BeanUtil.toBean(e, FacetItem.class)).collect(Collectors.toList()));
        }
        if (subjectFacet != null) {
            result.put("subjectFacet", subjectFacet.stream().map(e -> BeanUtil.toBean(e, FacetItem.class)).collect(Collectors.toList()));
        }
        if (writerFacet != null) {
            result.put("writerFacet", writerFacet.stream().map(e -> BeanUtil.toBean(e, FacetItem.class)).collect(Collectors.toList()));
        }
        if (mediaFacet != null) {
            result.put("mediaFacet", mediaFacet.stream().map(e -> BeanUtil.toBean(e, FacetItem.class)).collect(Collectors.toList()));
        }
        if (organFacet != null) {
            result.put("organFacet", organFacet.stream().map(e -> BeanUtil.toBean(e, FacetItem.class)).collect(Collectors.toList()));
        }
        if (rangeFacet != null) {
            result.put("rangeFacet", rangeFacet.stream().map(e -> BeanUtil.toBean(e, FacetItem.class)).collect(Collectors.toList()));
        }
        //将years聚类结果转换为key:xxx,value:xxx的形式
        List<FacetItem> list = new ArrayList<>();
        facet.get("years").forEach((k, v) -> {
            FacetItem item = new FacetItem();
            item.setKey(k);
            item.setValue(Integer.parseInt(v));
            item.setLabel(k);
            list.add(item);
        });
        result.put("yearFacet", list);
        result.forEach((k, v) -> {
            if (CollectionUtil.isEmpty(v)) {
                return;
            }
            if (k.equals("yearFacet")) {
                v = v.stream().sorted((o1, o2) -> Integer.parseInt(o2.getKey()) - Integer.parseInt(o1.getKey())).collect(Collectors.toList());
            } else {
                v = v.stream().sorted((o1, o2) -> o2.getValue() - o1.getValue()).collect(Collectors.toList());
            }
            result.put(k, v);
        });
        return JsonResult.Get("facet", result);
    }

    //由于从solr查询media的刊名速度过慢，此处使用数据库查询
    private List<Map<String, String>> defineMediaFacetResult(Map<String, String> facet) {
        if (CollectionUtil.isEmpty(facet)) {
            return null;
        }
        List<String> gchList = new ArrayList<>();
        for (String key : facet.keySet()) {
            gchList.add(key.toUpperCase());
        }
        LambdaQueryWrapper<PeriodInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(PeriodInfo::getGch, gchList);
        List<PeriodInfo> periodInfos = periodInfoService.list(wrapper);
        List<Map<String, String>> definedFacet = new ArrayList<>();
        for (String key : facet.keySet()) {
            Map map = new TreeMap();
            periodInfos.forEach(item -> {
                if (item.getGch().toLowerCase().contains(key)) {
//                    definedFacet.put(ndoContent.get(valueKey).toString(), facet.get(key));
                    map.put("key", key.toUpperCase());
                    map.put("value", facet.get(key));
                    map.put("label", item.getName().toString());
                    definedFacet.add(map);
                    return;
                }
            });
        }
        return definedFacet;
    }

    @Override
    public JsonResult getSubFacet(SearchParam searchParam, String field, String solrCollection, String queryKey, String valueKey) throws IOException, SolrServerException {
        Map<String, Map<String, String>> facet = new HashMap<>();
        if (searchParam.getSearchByExpression()) {
            searchParam = transExpression(searchParam);
            searchParam.setSearchExpression(searchParam.getSearchExpression() + " AND " + searchParam.getFq());
//            facet = FacetUtil.getFacet(SolrCollections.TITLE, searchParam.getSearchExpression(), getFq().get(0), 10, 0, field);
            SolrQuery solrQuery = new SolrQuery(searchParam.getSearchExpression());
            solrQuery.addFilterQuery(getFq().toArray(new String[getFq().size()]));
            solrQuery.addFacetField(field);
            solrQuery.setFacetLimit(80);
            QueryResponse response = solrClient.query(SolrCollections.TITLE, solrQuery);
            for (FacetField item : response.getFacetFields()) {
                HashMap<String,String> facetMap = new HashMap<>();
                item.getValues().stream().forEach(i -> {
                    facetMap.put(i.getName(), i.getCount() + "");
                });
                facet.put(item.getName(),facetMap);
            }
        } else {
            String query = null;
            //组装检索条件
//            List<SearchModel> searchModels = searchParam.getSearchModels();
            query = defineQueryString(searchParam).getSearchExpression();
            if (StringUtils.isBlank(query)) {
                query = "*:*";
            }
            query += " AND (" + searchParam.getFq() + ")";
            if (StringUtils.isNotBlank(searchParam.getIncludeRule())) {
                query += " AND " + searchParam.getIncludeRule();
            }
//            facet = FacetUtil.getFacet(SolrCollections.TITLE, query, getFq().get(0), 30, 0, field);
            SolrQuery solrQuery = new SolrQuery(query);
            solrQuery.addFilterQuery(getFq().toArray(new String[getFq().size()]));
            solrQuery.addFacetField(field);
            solrQuery.setFacetLimit(30);
            QueryResponse response = solrClient.query(SolrCollections.TITLE, solrQuery);
            for (FacetField item : response.getFacetFields()) {
                HashMap<String,String> facetMap = new HashMap<>();
                item.getValues().stream().forEach(i -> {
                    facetMap.put(i.getName(), i.getCount() + "");
                });
                facet.put(item.getName(),facetMap);
            }
        }
        Map<String, String> subFacet = facet.get(field);
        //因为原聚类结果是21_596这样的key，无法使用，此处转换为二级id的map
        Map<String, String> newFacet = new TreeMap<>();
        SearchParam finalSearchParam = searchParam;
        subFacet.forEach((k, v) -> {
            //去掉不为父子关系的结果
            if (!finalSearchParam.getFq().split(":")[1].equals(k.split("_")[0])) {
//                subFacet.remove(k);
                return;
            }
            newFacet.put(k.split("_")[1], v);
        });
        List<Map<String, String>> list = defineFacetResult(newFacet, solrCollection, queryKey, valueKey);

        //将聚类结果转换为key:xxx,value:xxx的形式
//        List<Map<String, String>> list = new ArrayList<>();
//        facetResult.forEach((k, v) -> {
//            Map map = new HashMap();
//            map.put("key", k);
//            map.put("value", v);
//            list.add(map);
//        });
        if (CollectionUtil.isNotEmpty(list)) {
            List<Map<String, String>> collect = list.stream().sorted((o1, o2) -> Integer.parseInt(o2.get("value")) - Integer.parseInt(o1.get("value"))).collect(Collectors.toList());
            return JsonResult.Get("facet", collect);
        } else {
            return JsonResult.Get("facet", null);
        }

    }

    public static List<Map<String, String>> defineFacetResult(Map<String, String> facet, String solrCollection, String queryKey, String valueKey) {
        if (CollectionUtil.isEmpty(facet)) {
            return null;
        }
        SolrSession session = SolrSession.Get(solrCollection);
        String query = "";
        for (String key : facet.keySet()) {
            query += queryKey + ":" + key + " OR ";
        }
        if (StringUtils.isNotBlank(query)) {
            query = query.substring(0, query.lastIndexOf("OR"));
        }
        List<SolrDocument> result = session.query(query, null, new Page(1, 60), null);
        List<Map<String, String>> definedFacet = new ArrayList<>();
        for (String key : facet.keySet()) {
            Map map = new TreeMap();
            result.forEach(item -> {
                JSONObject ndoContent = JSONObject.parseObject(item.get("ndo_content").toString());
                if (key.equals(ndoContent.get(queryKey).toString())) {
//                    definedFacet.put(ndoContent.get(valueKey).toString(), facet.get(key));
                    map.put("key", key);
                    map.put("value", facet.get(key));
                    map.put("label", ndoContent.get(valueKey).toString());
                    definedFacet.add(map);
                    return;
                }
            });
        }
        Collections.sort(definedFacet, (o1, o2) -> {
            Integer value1 = Integer.valueOf(o1.get("value"));
            Integer value2 = Integer.valueOf(o2.get("value"));
            return value2.compareTo(value1);
        });
        return definedFacet;
    }

    //根据searchModel创建检索条件，并添加到criterion中
    public static String addConditions(SearchParam searchParam) {
        String criterion = "(";
        for (SearchModel item : searchParam.getSearchModels()) {
            if (item == null || StringUtils.isBlank(item.getValue())) {
                continue;
            }
            String value = item.getSearchField().getValue();
            String queries = addSearchFields(value, item, searchParam.isSynonymExtension(), searchParam.isEnglishExtension());
            if (StringUtils.isBlank(criterion)) {
                criterion = queries;
                continue;
            }
            switch (item.getLogicOperator()) {
                case AND:
                    criterion += " AND (" + queries + ")";
                    break;
                case OR:
                    criterion += " OR (" + queries + ")";
                    break;
                case NOT:
                    criterion += " AND (NOT" + queries + ")";
                    break;
                default:
                    break;
            }
        }
        ;
        criterion += ")";
        criterion = criterion.replaceAll("\\(\\)", "");
        return criterion;
    }

    //添加类似于  (author:张三 OR first_author:张三) 的条件
    public static String addSearchFields(String value, SearchModel item, boolean isSynonymExtension, boolean isEnglishExtension) {
        if (StringUtils.isBlank(item.getValue())) {
            return "";
        }
        String query = " ";
        if (item.getSearchField() == SearchField.F) {//第一作者统一按精确检索
            query += "@exact#f=" + value;
        } else if (item.getSearchField() == SearchField.A) {//作者统一按精确检索
            query += value + "#omit=0#over=1.0#useSE=0";
        } else if (item.isExact() && item.getSearchField().getValue().contains("@paper")) {//精确
            query += value + "#omit=0#over=1.0#useSE=0";
        } else if (item.isExact()) {
            query += value;
        } else if (!item.isExact() && !item.getSearchField().getValue().contains("@paper")) {
            // 由于solr3.5模糊检索时冒号后面直接加*导致异常，此处用引号包裹，例:title_c:"*cad*"
            query += value + ":" + item.getValue().toLowerCase() + "*";
            // 由于solr3.5模糊检索时分类号,gch等字段无法使用前后*的模糊检索，因此额外添加之后后面为*的模糊检索
            query += " OR " + value + ":\"*" + item.getValue().toLowerCase() + "*\"";
        } else {
            query += value;
        }
        if (isEnglishExtension && (SearchField.U == item.getSearchField() || SearchField.T == item.getSearchField() || SearchField.M == item.getSearchField() || SearchField.K == item.getSearchField())) {
            query += "#useSE=1#useSELibName=synonyms";
        } else if (isSynonymExtension) {
            query += "#useSE=1#useSELibName=synonyms2";
        }
        String regEx = ".*[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        Pattern p = Pattern.compile(regEx);

        if (!query.contains(item.getValue().toLowerCase())) {
            String content = "";
            String[] split = item.getValue().toLowerCase().split("");
            for (String s : split) {
                Matcher m = p.matcher(s);
                if (m.matches()) {
                    content += "\\" + s;
                } else {
                    content += s;
                }
            }
            query += ":" + content + " ";
        }
        return query;
    }

    public static SearchParam defineQueryString(SearchParam searchParam) {
        String query = null;
        if (searchParam.getSearchByExpression() != null && searchParam.getSearchByExpression()) {//通过检索式检索
            searchParam = transExpression(searchParam);
            query = searchParam.getSearchExpression();
        } else {
            //组装检索条件
            List<SearchModel> searchModels = searchParam.getSearchModels();
            HashSet<String> highlightValues = new HashSet<>();
            searchModels.forEach(item -> highlightValues.add(item.getValue()));
            searchParam.setHighlightValues(highlightValues);
            query = addConditions(searchParam);
        }

//        if(searchParam.isEnglishExtension()){//中英文扩展
//
//        }
//        if(searchParam.isSynonymExtension()){// 同义词扩展
//
//        }
        if (searchParam.getBeginYear() != null) {
            query += " AND " + SearchField.Y.getValue() + ":[" + searchParam.getBeginYear() + " TO *]";
        }
        if (searchParam.getEndYear() != null) {
            query += " AND " + SearchField.Y.getValue() + ":[* TO " + searchParam.getEndYear() + "]";
        }
        if (searchParam.getPubDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            query += " AND processdate:[" + sdf.format(searchParam.getPubDate()) + " TO *]";
        }
        if (CollectionUtil.isNotEmpty(searchParam.getSubjects())) {//学科限定
            query += " AND (";
            for (String subject : searchParam.getSubjects()) {
                query += SearchField.CID.getValue() + ":" + subject + " OR ";
            }
            query = query.substring(0, query.lastIndexOf("OR")) + ")";
        }
        if (CollectionUtil.isNotEmpty(searchParam.getJournalRange())) {//期刊范围
            query += " AND (";
            for (String range : searchParam.getJournalRange()) {
                query += SearchField.RN.getValue() + ":" + range + " OR ";
            }
            query = query.substring(0, query.lastIndexOf("OR")) + ")";
        }
        query = query.trim();
        if (query.indexOf("( AND") == 0) {
            query = query.replaceFirst("\\( AND ", "(");
        } else if (query.indexOf("( OR") == 0) {
            query = query.replaceFirst("\\( OR ", "(");
        } else if (query.indexOf("AND") == 0) {
            query = query.replaceFirst("AND", "");
        } else if (query.indexOf("OR") == 0) {
            query = query.replaceFirst("OR", "");
        }
        searchParam.setSearchExpression(query);
        return searchParam;
    }

    //将检索表达式转换为程序可识别的语句
    private static SearchParam transExpression(SearchParam searchParam) throws QuerySyntaxErrorException {
        String expression = searchParam.getSearchExpression();
        expression = expression.replaceAll("\\s+=\\s+", "=");
        if (StringUtils.isBlank(expression)) {
            return searchParam;
        }
        String finalStr = null;
        Field[] declaredFields = SearchField.class.getDeclaredFields();

        List<String> finalArr = new ArrayList<>();
        List<String> secondArr = new ArrayList<>();
        List<String> firstArr = Arrays.asList(expression.split(" "));
        Set<String> highlightValues = new HashSet<>();
        for (int i = 0; i < firstArr.size(); i++) {
            String item = firstArr.get(i);
            if (StringUtils.isBlank(item)) {
                continue;
            }
            if ("*".equals(item)) {
                item = "AND";
            } else if ("+".equals(item)) {
                item = "OR";
            } else if ("-".equals(item)) {
                item = "NOT";
            }
            if (LogicOperator.NOT.getName().equals(item) && (i == 0 || !"(".equals(firstArr.get(i - 1)))) {
                item = "AND (NOT";
                firstArr.set(i + 1, firstArr.get(i + 1) + ")");
            }

            if (item.indexOf('=') != -1) {
                String value = null;
                try {
                    value = item.split("=")[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new QuerySyntaxErrorException("检索式语法错误!");
                }
                highlightValues.add(value.replaceAll("\\)", ""));
                String replaced = "";
                String key = item.substring(0, item.indexOf('=')).trim();
                for (Field declaredField : declaredFields) {
//                    if(declaredField.getName().equals(key)) {
                    if (key.replaceAll("\\(", "").equals(declaredField.getName())) {
                        SearchField searchField = SearchField.valueOf(declaredField.getName());
//                        replaced = searchField.getValue();
                        replaced = key.replace(declaredField.getName(), searchField.getValue());
                    }
                }
                String itemStr = null;
                if (replaced.equals(SearchField.C.getValue())) {
                    //分类号需在最后加上*号
                    itemStr = replaced + ":" + item.substring(item.indexOf('=') + 1).toLowerCase() + "*";
                } else {
                    itemStr = replaced + ":" + item.substring(item.indexOf('=') + 1).toLowerCase();
                }
                secondArr.add(itemStr);
            } else {
                secondArr.add(item);
            }
        }
        ;
        searchParam.setHighlightValues(highlightValues);
//        secondArr.forEach(item -> {
//            if (StringUtils.isBlank(item)) {
//                return;
//            }
//            if (item.indexOf('=') != -1) {
//                String[] itemArr = item.split("=");
//                String[] keyArr = itemArr[0].split(";");
//                String itemStr = "(";
//                for (String s : keyArr) {
//                    itemStr += s +":"+itemArr[1]+ " OR ";
//                }
//                itemStr = itemStr.substring(0,itemStr.lastIndexOf("OR"))+")";
//                finalArr.add(itemStr);
//            } else {
//                finalArr.add(item.replaceFirst("=",":"));
//            }
//
//        });
        finalStr = CollectionUtil.join(secondArr, " ");
        searchParam.setSearchExpression(finalStr);
        return searchParam;
    }

    private List<String> getFq() {
        List<String> fqs = new ArrayList<>();
        String[] allowRuleArr = this.allowRule.split(",");
        for (String rule : allowRuleArr) {
            if (StringUtils.isBlank(rule)) {
                continue;
            }
            fqs.add("@region#rt=1:" + rule);
        }
        String[] shieldArr = this.shield.split(",");
        for (String rule : shieldArr) {
            if (StringUtils.isBlank(rule)) {
                continue;
            }
            fqs.add("@region#rt=2:" + rule);
        }
        return fqs;
    }

    private org.apache.solr.client.solrj.SolrQuery getDefaultQuery() {
        org.apache.solr.client.solrj.SolrQuery query = new SolrQuery();
        query.setParam("q.op", new String[]{"AND"});
        return query;
    }
}
