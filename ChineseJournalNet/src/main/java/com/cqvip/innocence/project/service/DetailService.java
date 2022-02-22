package com.cqvip.innocence.project.service;

import java.util.List;
import java.util.Map;

/**
 * @author 01
 * @date 2022-01-07 11:03
 */
public interface DetailService {
    /**
     * 获取文献详情
     * @param index
     * @param query
     * @param start
     * @param rows
     * @param fl
     * @param wt
     * @return
     */
    Map<String, Object> getDetailInfo(String index, String query, Integer start, Integer rows, String fl, String wt);

    /**
     * 获取文献关联引用文献
     * @param zkreferidsReal
     * @param index
     * @return
     */
    List<Map<String, Object>> getRelDetailInfo(String index,String zkreferidsReal,Integer start,Integer rows);
}
