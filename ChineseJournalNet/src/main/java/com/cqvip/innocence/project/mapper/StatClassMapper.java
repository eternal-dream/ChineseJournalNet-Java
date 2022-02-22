package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.StatClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 分类限制专用表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
public interface StatClassMapper extends BaseMapper<StatClass> {

 List<StatClass> statByName();
}
