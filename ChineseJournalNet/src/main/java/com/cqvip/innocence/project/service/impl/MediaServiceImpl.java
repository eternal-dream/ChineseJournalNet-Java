package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.constant.SolrCollections;
import com.cqvip.innocence.common.exception.QuerySyntaxErrorException;
import com.cqvip.innocence.common.util.solr.FacetUtil;
import com.cqvip.innocence.project.mapper.DatabaseCollectKindMapper;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.SearchModel;
import com.cqvip.innocence.project.model.dto.SearchParam;
import com.cqvip.innocence.project.model.entity.DatabaseCollectKind;
import com.cqvip.innocence.project.model.entity.ShieldInfo;
import com.cqvip.innocence.project.service.MediaService;
import com.cqvip.innocence.project.service.ShieldInfoService;
import com.finemi.support.model.dto.PageObject;
import com.finemi.support.solr.dto.Sort;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.cqvip.innocence.common.enums.SearchField.MN;
import static com.cqvip.innocence.common.enums.SearchField.PB;
import static com.cqvip.innocence.project.service.impl.SearchServiceImpl.defineFacetResult;

/**
 * @ClassName FacetsServiceImpl
 * @Description TODO
 * @Author Innocence
 * @Date 2021/12/28 10:22
 * @Version 1.0
 */
@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private DatabaseCollectKindMapper collectKindMapper;

    @Value("${rule.media_info.shield}")
    private String shield;

    @Autowired
    ShieldInfoService shieldInfoService;

    @Autowired
    private SolrClient solrClient;

    @Override
    public JsonResult getMediaFacet(SearchParam param) {
        Map<String, Map<String, String>> facet = new HashMap<>();
        //组装检索条件
        try {
            SearchParam searchParam = SearchServiceImpl.defineQueryString(param);
            String query = searchParam.getSearchExpression();
            if (StringUtils.isBlank(query)) {
                query = "*:*";
            }
            if (StringUtils.isNotBlank(searchParam.getIncludeRule())) {
                query += " AND " + searchParam.getIncludeRule();
            }
            SolrQuery solrQuery = new SolrQuery(query);
            solrQuery.addFilterQuery(getFq().toArray(new String[getFq().size()]));
            solrQuery.addFacetField("myclassid_g1", "subjectids_s", "lngrangeid", "areaids_g1");
            solrQuery.setFacetLimit(60);
            QueryResponse response = solrClient.query(SolrCollections.MEDIA, solrQuery);
            response.getFacetFields().forEach(item->{
                HashMap<String,String> facetMap = new HashMap<>();
                item.getValues().stream().forEach(i -> {
                    if(i.getCount() > 0){
                        facetMap.put(i.getName(), i.getCount() + "");
                    }
                });
                facet.put(item.getName(),facetMap);
            });
            // 学科分类将父子级全部聚类出来
            List<Map> classFacetsList = new ArrayList<>();
            List<Map<String, String>> classFacet = defineFacetResult(facet.get("myclassid_g1"), SolrCollections.CLASS, "classid", "classtypename");
            if (null != classFacet) {
                classFacetsList.addAll(classFacet);
                classFacetsList.forEach(item -> {
                    searchParam.setFq("classid:" + item.get("key"));
                    List<Map<String, String>> subFacet = getSubFacet(searchParam, "myclassid_g2", SolrCollections.CLASS, "classid", "classtypename");
                    if (null != subFacet && !subFacet.isEmpty()) {
                        item.put("children", subFacet);
                    }
                });
            }
            //主题聚类
            List<Map<String, String>> subjectFacet = defineFacetResult(facet.get("subjectids_s"), SolrCollections.SUBJECT, "subjectid", "subject");
            //地区聚类将父子集合聚类出来
            List<Map<String, String>> areaFacet = defineFacetResult(facet.get("areaids_g1"), SolrCollections.AREA, "areaid", "areaname");
            List<Map> areaMaps = new ArrayList<>();
            if (null != areaFacet) {
                areaMaps.addAll(areaFacet);
                areaMaps.forEach(item -> {
                    searchParam.setFq("areaids_g1:" + item.get("key"));
                    List<Map<String, String>> subFacet = getSubFacet(searchParam, "areaids_g2", SolrCollections.AREA, "areaid", "areaname");
                    if (null != subFacet && !subFacet.isEmpty()) {
                        item.put("children", subFacet);
                    }
                });
            }

            //数据库收录聚类
            Map<String, List<Map<String, Object>>> lngrangeName = getLngrangeName(facet.get("lngrangeid"));
            //国内数据库
            List<Map<String, Object>> cMap = lngrangeName.get("cMap");
            //国外数据库
            List<Map<String, Object>> oMap = lngrangeName.get("oMap");

            return JsonResult.Get().put("subjectFacet", subjectFacet)
                    .put("classFacet", classFacetsList)
                    .put("areaFacet", areaMaps)
                    .put("cMap", cMap)
                    .put("oMap", oMap);
        } catch (QuerySyntaxErrorException e) {
            return JsonResult.Get(false, "检索式语法错误!");
        } catch (SolrServerException e) {
            return JsonResult.Get(false, "检索服务错误!");
        } catch (IOException e) {
            return JsonResult.Get(false, "检索服务错误!");
        }
    }

    @Override
    public JsonResult getMediaPage(SearchParam searchParam) throws IOException, SolrServerException {
        JsonResult result = JsonResult.Get();
        //组装检索条件
        searchParam = SearchServiceImpl.defineQueryString(searchParam);
        //排序
        List<Sort> sorts = new ArrayList<>();
        if (StringUtils.isNotBlank(searchParam.getSortField())) {
            sorts.add(new Sort(searchParam.getSortField(), searchParam.isAsc()));
        }
        //从title_info查出文章数据
        String query = searchParam.getSearchExpression();
        if (StringUtils.isBlank(query)) {
            query = "*:*";
        }
        if (StringUtils.isNotBlank(searchParam.getIncludeRule())) {
            query += " AND " + searchParam.getIncludeRule();
        }
        PageObject<Map<String, Object>> pageObject = new PageObject<>();
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setStart((searchParam.getPageNum() - 1) * searchParam.getPageSize());
        solrQuery.setRows(searchParam.getPageSize());
        if (StringUtils.isNotBlank(searchParam.getSortField())) {
            solrQuery.setSort(searchParam.getSortField(), searchParam.isAsc() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
        }
        solrQuery.addFilterQuery(getFq().toArray(new String[getFq().size()]));
        QueryResponse response = solrClient.query(SolrCollections.MEDIA, solrQuery);
        pageObject.setRows((List) response.getResults());
        pageObject.setTotal((int) response.getResults().getNumFound());
        pageObject.setPage(searchParam.getPageNum());
        //期刊的前端屏蔽
        List<String> gch5List = pageObject.getRows().stream().map(i -> JSONObject.parseObject(i.get("ndo_content").toString()).get("gch5").toString()).collect(Collectors.toList());
        LambdaQueryWrapper<ShieldInfo> wrapper = new LambdaQueryWrapper<>();
        if(!gch5List.isEmpty()){
            wrapper.in(ShieldInfo::getGch5, gch5List);
        }
        List<ShieldInfo> list = shieldInfoService.list(wrapper);
        pageObject.setRows(pageObject.getRows().stream().filter(i -> {
            String gch5 = JSONObject.parseObject(i.get("ndo_content").toString()).get("gch5").toString();
            boolean flag = list.stream().filter(j -> j.getGch5().equals(gch5)).collect(Collectors.toList()).size() > 0;
            return !flag;
        }).collect(Collectors.toList()));
        // 文章主体内容ndo_content为字符串，此处转为json对象
        // 根据输入框输入的内容，判断后组装高亮
        List<SearchModel> models = searchParam.getSearchModels();
        Map<String, String> highLightMap = new HashMap<>();
        if (models != null && models.size() > 0) {
            models.forEach(item -> {
                if (item.getSearchField().equals(MN) && StrUtil.isNotBlank(item.getValue())) {
                    highLightMap.put("media", item.getValue());
                } else if (item.getSearchField().equals(PB) && StrUtil.isNotBlank(item.getValue())) {
                    highLightMap.put("publisher", item.getValue());
                }
            });
        }
        pageObject.getRows().forEach(item -> {
            JSONObject jsonObject = JSONObject.parseObject(item.get("ndo_content").toString());
            if (!highLightMap.isEmpty()) {
                Set<Map.Entry<String, String>> entries = highLightMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String string = jsonObject.getString(entry.getKey());
                    String replace = string.replace(entry.getValue(), "<span style=\"color:red\">" + entry.getValue() + "</span>");
                    jsonObject.put(entry.getKey(), replace);
                }
            }
            item.put("ndo_content", jsonObject);
        });
        result.put("pageBean", pageObject);
        return result;
    }

    /**
     * 针对期刊的数据库收录聚类，要区分本国和国外数据库收录
     *
     * @param facet
     * @return java.util.Map<java.lang.String, java.util.List < java.util.Map < java.lang.String, java.lang.String>>>
     * @author Innocence
     * @date 2021/12/29
     */
    public Map<String, List<Map<String, Object>>> getLngrangeName(Map<String, String> facet) {
        List<DatabaseCollectKind> kinds = collectKindMapper.selectList(null);
        HashMap<String, List<Map<String, Object>>> map = new HashMap<>();
        List<Map<String, Object>> cMaps = new ArrayList<>();
        List<Map<String, Object>> oMaps = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = facet.entrySet();
        String regex = "[\u4E00-\u9FA5]";
        String regexEnglish = "[[a-zA-Z]]";
        Pattern compile = Pattern.compile(regex);
        Pattern compileEnglish = Pattern.compile(regexEnglish);
        for (Map.Entry<String, String> entry : entries) {
            Matcher m = compile.matcher(entry.getKey());
            if (!m.matches()) {
                DatabaseCollectKind kind = kinds.stream().filter(item -> item.getEnglishAbbr().toLowerCase()
                        .equals(entry.getKey().toLowerCase())).findAny().orElse(null);
                if (null != kind) {
                    String englishAddr = kind.getEnglishAbbr();
                    Matcher matcher = compileEnglish.matcher(englishAddr.indexOf("_") != -1 ?
                            (englishAddr.split("_"))[0] : englishAddr);
                    String hexinStr = "bdhx,rwskhx,zgkjhx,cscd,cssci";
                    String ketStr = "";
                    while (matcher.find()) {
                        ketStr += matcher.group().toLowerCase();
                    }
                    if (StrUtil.isNotBlank(ketStr)) {
                        if (hexinStr.indexOf(ketStr) != -1) { //国内数据库
                            Map<String, Object> cMap = new HashMap<>();
                            cMap.put("key", entry.getKey());
                            cMap.put("label", kind.getClusterShowName());
                            cMap.put("value", entry.getValue());
                            cMaps.add(cMap);
                        } else {        //国外数据库
                            Map<String, Object> oMap = new HashMap<>();
                            oMap.put("key", entry.getKey());
                            oMap.put("label", kind.getClusterShowName());
                            oMap.put("value", entry.getValue());
                            oMaps.add(oMap);
                        }
                    }

                }
            }
        }
        Collections.sort(cMaps, (o1, o2) -> {
            Integer value1 = Integer.valueOf(o1.get("value").toString());
            Integer value2 = Integer.valueOf(o2.get("value").toString());
            return value2.compareTo(value1);
        });
        Collections.sort(oMaps, (o1, o2) -> {
            Integer value1 = Integer.valueOf(o1.get("value").toString());
            Integer value2 = Integer.valueOf(o2.get("value").toString());
            return value2.compareTo(value1);
        });
        map.put("cMap", cMaps);
        map.put("oMap", oMaps);
        List<Map<String, Object>> allChildren = new ArrayList<>();
        map.forEach((k, v) -> {
            Iterator<Map<String, Object>> iterator = map.get(k).iterator();
            while (iterator.hasNext()) {
                Map<String, Object> item = iterator.next();
                List<Map<String, Object>> children = map.get(k).stream().filter(i -> {
                    String key = i.get("key").toString();
                    return !key.equals(item.get("key").toString()) && key.contains(item.get("key").toString()) && "CSCD,BDHX,CSSCI".contains(item.get("key").toString().toUpperCase());
                }).collect(Collectors.toList());
                item.put("children", children);
                allChildren.addAll(children);
                if (allChildren.contains(item)) {
                    iterator.remove();
                }
            }
        });
        return map;
    }

    /**
     * 组装学科分类的子集聚类数据
     *
     * @param searchParam
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.String>>
     * @author Innocence
     * @date 2021/12/29
     */
    public List<Map<String, String>> getSubFacet(SearchParam searchParam, String field, String solrCollections,
                                                 String queryKey, String queryValue) {
        Map<String, Map<String, String>> facet = null;
//        AtomicReference<String> criterion = new AtomicReference<>("");
        //组装检索条件
//        if (null != searchParam) {
        SearchParam param = SearchServiceImpl.defineQueryString(searchParam);
//        }
        String query = param.getSearchExpression();
        if (StringUtils.isBlank(query)) {
            query = "*:*";
        }
        query += " AND (" + param.getFq() + ")";
        int limit = 60;
        if (solrCollections.equals(SolrCollections.AREA)) {
            limit = 1000;
        }
        facet = FacetUtil.getFacet(SolrCollections.MEDIA, query, "@region#rt=2:14", limit, 0, field);
        Map<String, String> subFacet = facet.get(field);
        //因为原聚类结果是21_596这样的key，无法使用，此处转换为二级id的map
        Map<String, String> newFacet = new TreeMap<>();
        subFacet.forEach((k, v) -> {
            //去掉不为父子关系的结果
            if (!param.getFq().split(":")[1].equals(k.split("_")[0])) {
                return;
            }
            newFacet.put(k.split("_")[1], v);
        });
        if (newFacet.size() == 0) {
            return null;
        }
        return defineFacetResult(newFacet, solrCollections, queryKey, queryValue);
    }

    private List<String> getFq() {
        List<String> fqs = new ArrayList<>();
        String[] shieldArr = this.shield.split(",");
        for (String rule : shieldArr) {
            if (StringUtils.isBlank(rule)) {
                continue;
            }
            fqs.add("@region#rt=2:" + rule);
        }
        return fqs;
    }
}
