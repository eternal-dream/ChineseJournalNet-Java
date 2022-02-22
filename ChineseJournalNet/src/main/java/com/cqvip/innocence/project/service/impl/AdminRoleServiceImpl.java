package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.mapper.AdminRoleMapper;
import com.cqvip.innocence.project.model.entity.AdminRole;
import com.cqvip.innocence.project.service.AdminRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService {

    @Override
    public void saveBatch(List<AdminRole> list) {
        for (AdminRole item:list) {
            baseMapper.insert(item);
        }
    }
}
