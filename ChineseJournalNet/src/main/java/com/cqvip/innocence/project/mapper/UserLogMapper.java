package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.dto.StatisticsDTO;
import com.cqvip.innocence.project.model.entity.UserLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqvip.innocence.project.model.enums.DownloadRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户日志表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2022-01-07
 */
public interface UserLogMapper extends BaseMapper<UserLog> {

    List<String> getAllPlates();

    List<Map> analysis(StatisticsDTO statisticsDTO);

    /**
     * 获取这个时间段内，当前用户的下载次数
     * @param userId
     * @param rule
     * @return
     */
    int getDownloadCountInDateScope(@Param("userId") Long userId, @Param("rule")String rule);
}
