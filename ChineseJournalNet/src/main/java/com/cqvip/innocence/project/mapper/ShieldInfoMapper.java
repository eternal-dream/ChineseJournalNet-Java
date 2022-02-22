package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ShieldInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 屏蔽数据表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2022-02-10
 */
public interface ShieldInfoMapper extends BaseMapper<ShieldInfo> {

    int checkArticleDetail(String lngid);

    int checkJournalDetail(String gch5);

}
