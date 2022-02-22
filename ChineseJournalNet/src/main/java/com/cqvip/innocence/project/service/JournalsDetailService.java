package com.cqvip.innocence.project.service;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author 01
 * @date 2021-12-28 14:36
 */
public interface JournalsDetailService {
    /**
     * 获取期刊详情信息
     *
     * @param index 索引
     * @param query 检索式
     * @param start 起始页
     * @param rows
     * @param fl
     * @param wt    json
     * @return {@link JSONObject}
     * @throws
     * @author 01
     * @date 2021/12/28 15:05
     */
    Map getJournalsDetail(String index, String query, Integer start, Integer rows, String fl, String wt);


    Map getSummary(String index, String query, Integer start, Integer rows, String fl, String wt);
}
