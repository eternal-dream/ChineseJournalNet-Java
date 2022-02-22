package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.common.util.reflection.ReflectionUtils;
import com.cqvip.innocence.project.mapper.DatabaseCollectKindMapper;
import com.cqvip.innocence.project.model.entity.DatabaseCollectKind;
import com.cqvip.innocence.project.model.vo.JournalSummaryVO;
import com.cqvip.innocence.project.model.vo.JournalSummaryYearVO;
import com.cqvip.innocence.project.model.vo.Summary;
import com.cqvip.innocence.project.service.JournalsDetailService;
import com.finemi.support.model.dto.Page;
import com.finemi.support.model.dto.PageObject;
import com.finemi.support.solr.SolrSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 01
 * @date 2021-12-28 14:38
 */
@Service
public class JournalsDetailServiceImpl implements JournalsDetailService {

    @Autowired
    DatabaseCollectKindMapper databaseCollectKindMapper;

    @Override
    public Map getJournalsDetail(String index, String query, Integer start, Integer rows, String fl, String wt) {
        Page page = new Page(start, rows);
        SolrSession session = SolrSession.Get(index);
        PageObject<Map<String, Object>> pageObject = session.getPageObject(query, "", page, null);
        if (!pageObject.getRows().isEmpty()) {
            JSONObject jsonObject = JSON.parseObject((String) pageObject.getRows().get(0).get("ndo_content"));
            //收录年份  lngrangeid
            String gchyearsnum = (String) jsonObject.get("gchyearsnum");
            String lngrangeid = (jsonObject.get("lngrangeid")!=null?(String)jsonObject.get("lngrangeid"):null);
            if(StringUtils.isNotBlank(lngrangeid)){
                //查询收录情况
                String[] ids = lngrangeid.split(";");
                QueryWrapper<DatabaseCollectKind> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("ENGLISH_ABBR", Arrays.asList(ids));
                queryWrapper.orderByDesc("ENGLISH_ABBR");
                List<DatabaseCollectKind> databaseCollectKinds = databaseCollectKindMapper.selectList(queryWrapper);
                jsonObject.put("lngrange", databaseCollectKinds);
            }
            //若有此字段 gchyearsnum
            if (StringUtils.isNotBlank(gchyearsnum)) {
                List<JournalSummaryYearVO> journalSummaryYearVOList = new ArrayList<>();
                Document document = XmlUtil.parseXml(gchyearsnum);
                Element rootElement = XmlUtil.getRootElement(document);
                NodeList childNodesYears = rootElement.getChildNodes();
                for (int i = 0; i < childNodesYears.getLength(); i++) {
                    String nodeName = childNodesYears.item(i).getNodeName();
                    if ("years".equals(nodeName)) {
                        String value = childNodesYears.item(i).getAttributes().getNamedItem("value").getNodeValue();
                        NodeList childNodes = childNodesYears.item(i).getChildNodes();
                        List<String> tempList = new ArrayList<>();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            String textContent = childNodes.item(j).getTextContent();
                            if (StringUtils.isNotBlank(textContent)) {
                                tempList.add(textContent);
                            }
                        }
                        JournalSummaryYearVO journalSummaryYearVO = new JournalSummaryYearVO();
                        journalSummaryYearVO.setYear(value);
                        journalSummaryYearVO.setNum(tempList);
                        journalSummaryYearVOList.add(journalSummaryYearVO);
                    }
                }
                //排序
                journalSummaryYearVOList = journalSummaryYearVOList.stream().sorted(Comparator.comparing(JournalSummaryYearVO::getYear).reversed()).collect(Collectors.toList());
                jsonObject.put("gchyearsnum", journalSummaryYearVOList);
            }
            return jsonObject;
        } else {
            return null;
        }
    }

    @Override
    public Map getSummary(String index, String query, Integer start, Integer rows, String fl, String wt) {
        Page page = new Page(start, rows);
        SolrSession session = SolrSession.Get(index);
        PageObject<Map<String, Object>> pageObject = session.getPageObject(query, "", page, null);
        if (!pageObject.getRows().isEmpty()) {
            JSONObject jsonObject = JSON.parseObject((String) pageObject.getRows().get(0).get("ndo_content"));

            //收录年份
            String xml = (String) jsonObject.get("xml");
            if (StringUtils.isNotBlank(xml)) {
                List<Summary> summaryList = new ArrayList<>();
                Document document = XmlUtil.parseXml(xml);
                Element rootElement = XmlUtil.getRootElement(document);
                NodeList childNodesYears = rootElement.getChildNodes();
                for (int i = 0; i < childNodesYears.getLength(); i++) {
                    String nodeName = childNodesYears.item(i).getNodeName();
                    if ("article".equals(nodeName)) {
                        NodeList childNodes = childNodesYears.item(i).getChildNodes();

                        Summary summary = new Summary();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            String textContent = childNodes.item(j).getTextContent();
                            if (StringUtils.isNotBlank(textContent)) {
                                String subNodeName = childNodes.item(j).getNodeName();
                                ReflectionUtils.setFieldValue(summary,subNodeName,textContent);
                            }
                        }
                        summaryList.add(summary);
                    }
                }
                List<String> orderInfo = summaryList.stream().map(Summary::getMuinfo).distinct().collect(Collectors.toList());
                Map<String, List<Summary>> collect = summaryList.stream().collect(Collectors.groupingBy(Summary::getMuinfo));
                List<JournalSummaryVO> data = new ArrayList<>();
                for (String val : orderInfo) {
                    JournalSummaryVO journalSummaryVO = new JournalSummaryVO();
                    journalSummaryVO.setMuiInfo(val);
                    journalSummaryVO.setSummarys(collect.get(val));
                    data.add(journalSummaryVO);
                }
                jsonObject.put("xml", data);
            }
            return jsonObject;
        } else {
            return null;
        }
    }


}
