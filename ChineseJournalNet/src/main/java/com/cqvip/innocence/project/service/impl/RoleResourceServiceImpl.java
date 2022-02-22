package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.mapper.RoleResourceMapper;
import com.cqvip.innocence.project.model.entity.RoleResource;
import com.cqvip.innocence.project.service.RoleResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2020-07-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {

}
