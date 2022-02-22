package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqvip.innocence.project.model.entity.Resource;
import com.cqvip.innocence.project.model.entity.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2020-07-13
 */
public interface ResourceMapper extends BaseMapper<Resource> {

 List<Resource> getRoleResources(Role role);
}
