package com.cqvip.innocence.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cqvip.innocence.project.service.DetailService;
import com.finemi.support.model.dto.Page;
import com.finemi.support.model.dto.PageObject;
import com.finemi.support.solr.SolrSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 01
 * @date 2022/1/7 11:04
 */
@Service
public class DetailServiceImpl implements DetailService {
    @Override
    public Map<String, Object> getDetailInfo(String index, String query, Integer start, Integer rows, String fl, String wt) {
        Page page = new Page(start, rows);
        SolrSession session = SolrSession.Get(index);
        PageObject<Map<String, Object>> pageObject = session.getPageObject(query, "", page, null);
        if(pageObject.getRows().size()==0){
            return null;
        }
        JSONObject jsonObject = JSON.parseObject((String) pageObject.getRows().get(0).get("ndo_content"));
        return JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, Object>>() {}) ;
    }

    @Override
    public List<Map<String, Object>> getRelDetailInfo(String index,String zkreferidsReal,Integer start, Integer rows) {
        Page page = new Page(start, 6);
        String[] split = StringUtils.split(zkreferidsReal, ";");
        SolrSession session = SolrSession.Get(index);
        List<String> stringList = Arrays.stream(split).map(item -> " lngid: " + item).collect(Collectors.toList());
        String rule = StringUtils.join(stringList, " OR ");

        PageObject<Map<String, Object>> refObj = session.getPageObject(rule, "", page, null);
        return refObj.getRows();
    }
}
