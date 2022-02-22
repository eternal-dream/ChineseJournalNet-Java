package com.cqvip.innocence.common.util.solr;

import com.finemi.support.model.constant.Config;
import com.finemi.support.solr.SolrSession;
import com.finemi.support.solr.dto.FacetFieldParam;
import com.finemi.support.solr.dto.FacetFieldParamList;
import com.finemi.support.solr.dto.FacetPivotParamList;
import com.finemi.support.solr.dto.SolrCriterion;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.common.StringUtils;
import org.apache.solr.common.util.NamedList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郝航
 * @desc solr聚类工具
 * @date 2020/3/27 15:45
 */
public class FacetUtil {
    public static Map<String, Map<String,String>> getFacet(String core, SolrCriterion criterion, int limit, int offset, String ... field) {
        Map<String, Map<String,String>> map = new LinkedHashMap<>();
        if (field.length==0) {
            return map;
        }
        limit = (limit == 0) ? Integer.parseInt(Config.get("solr.facet.field.limit")) : limit;
        offset = (offset == 0) ? Integer.parseInt(Config.get("solr.facet.field.offset")) : offset;
        FacetFieldParamList facetFieldParamList = new FacetFieldParamList();
        for(int i =0;i<field.length;i++){
            facetFieldParamList.add(field[i]);
        }
        facetFieldParamList.setLimit(limit);
        facetFieldParamList.setOffset(offset);
        facetFieldParamList.setSort(FacetFieldParam.Sort.COUNT);
        SolrSession session = SolrSession.Get(core);
        List<FacetField> facetFields = null;
        if (criterion==null) {
            facetFields = session.facetField(SolrCriterion.Get(), facetFieldParamList);
        } else {
            facetFields = session.facetField(criterion, facetFieldParamList);
        }
        if (!facetFields.isEmpty()) {
            for (FacetField facetField : facetFields) {
                List<FacetField.Count> counts = facetField.getValues();
                Map<String,String> map1 = new LinkedHashMap<>();
                for (FacetField.Count count : counts) {
                    if(count.getCount() == 0){
                        break;
                    }
                    map1.put(count.getName(),count.getCount() + "");
                }
                map.put(facetField.getName(), map1);
            }
        }
        return map;
    }

    public static Map<String, Map<String,String>> getFacet(String core, String query,String filterQuery, int limit, int offset, String ... field) {
        Map<String, Map<String,String>> map = new LinkedHashMap<>();
        if (field.length==0) {
            return map;
        }
        limit = (limit == 0) ? Integer.parseInt(Config.get("solr.facet.field.limit")) : limit;
        offset = (offset == 0) ? Integer.parseInt(Config.get("solr.facet.field.offset")) : offset;
        FacetFieldParamList facetFieldParamList = new FacetFieldParamList();
        for(int i =0;i<field.length;i++){
            facetFieldParamList.add(field[i]);
        }
        facetFieldParamList.setLimit(limit);
        facetFieldParamList.setOffset(offset);
        facetFieldParamList.setSort(FacetFieldParam.Sort.COUNT);
        facetFieldParamList.setMincount(1);
        SolrSession session = SolrSession.Get(core);
        List<FacetField> facetFields = null;
        if (StringUtils.isEmpty(query)) {
            facetFields = session.facetField("*:*",filterQuery, facetFieldParamList);
        } else {
            facetFields = session.facetField(query,filterQuery, facetFieldParamList);
        }
        if (!facetFields.isEmpty()) {
            for (FacetField facetField : facetFields) {
                List<FacetField.Count> counts = facetField.getValues();
                Map<String,String> map1 = new LinkedHashMap<>();
                for (FacetField.Count count : counts) {
                    map1.put(count.getName(),count.getCount() + "");
                }
                map.put(facetField.getName(), map1);
            }
        }
        return map;
    }

    public static Map<String, List<PivotField>> getFacetPivot(String core, SolrCriterion criterion, String ... field) {
        Map<String, List<PivotField>> map = new HashMap<>();
        if (field.length==0) {
            return map;
        }
        FacetPivotParamList pivotParamList = new FacetPivotParamList();
        pivotParamList.add(field);
        SolrSession session = SolrSession.Get(core);
        NamedList<List<PivotField>> facetFields = null;
        if (criterion==null) {
            facetFields = session.facetPivot(SolrCriterion.Get().buildQuery(),null, pivotParamList);
        } else {
            facetFields = session.facetPivot(criterion.buildQuery(),null, pivotParamList);
        }
        map = facetFields.asShallowMap();
        return map;
    }
}
