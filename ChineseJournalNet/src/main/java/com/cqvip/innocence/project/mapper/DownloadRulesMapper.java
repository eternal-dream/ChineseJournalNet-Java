package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.DownloadRules;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 文献下载限制规则表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-12-14
 */
public interface DownloadRulesMapper extends BaseMapper<DownloadRules> {
    /**
     * 通过用户id获取相关规则（个人、组）
     * @param id
     * @return
     */
    List<DownloadRules> getRulesByUserId(Long id);
}
