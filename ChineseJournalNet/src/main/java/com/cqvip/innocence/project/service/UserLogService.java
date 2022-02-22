package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.dto.CheckResult;
import com.cqvip.innocence.project.model.dto.StatisticsDTO;
import com.cqvip.innocence.project.model.entity.UserLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户日志表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2022-01-07
 */
public interface UserLogService extends IService<UserLog> {

    /**
     * 获取日志中存在的页面板块
     * @return
     */
    List<String> getAllPlates();

    /**
     * 访问量查询统计
     * @param statisticsDTO
     * @return
     */
    List<Map> analysis(StatisticsDTO statisticsDTO);

    /**
     * 用户下载限制(主要是验证包库信息和时段内下载次数限制)
     * @param id
     * @param lngid
     * @return true 超限制了， false没有限制
     */
    CheckResult downloadLimitCheck(Long id,String lngid);
    /**
     * 用户阅读限制(主要是验证包库信息)
     * @param id
     * @param lngid
     * @return true 超限制了， false没有限制
     */
    CheckResult readLimitCheck(Long id, String lngid);
}
