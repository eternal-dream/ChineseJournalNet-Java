package com.cqvip.innocence.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.project.mapper.RoleMapper;
import com.cqvip.innocence.project.model.entity.Admin;
import com.cqvip.innocence.project.model.entity.AdminRole;
import com.cqvip.innocence.project.model.entity.Role;
import com.cqvip.innocence.project.model.entity.RoleResource;
import com.cqvip.innocence.project.service.AdminRoleService;
import com.cqvip.innocence.project.service.RoleResourceService;
import com.cqvip.innocence.project.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Override
    public List<Role> getRolesByAdmin(Admin admin) {
        return baseMapper.getRolesByAdmin(admin);
    }

    @Override
    public List<Role> getRoles(String name, Page pageParams) {
        return baseMapper.getRoles(name,pageParams);
    }

    @Override
    public boolean addOrModifyRole(String name, Long roleId, String description, JSONArray resourceIds) {
        Role role;
        if (roleId == null) {//新增
            role = new Role();
        } else {//编辑
            role = this.getById(roleId);
        }
        role.setName(name);
        if (!StringUtils.isBlank(description)) {
            role.setDescription(description);
        }
        this.saveOrUpdate(role);
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < resourceIds.size(); i++) {
            list.add(resourceIds.getLong(i));
        }
        if (!list.isEmpty() && list.size() > 0) {
            roleResourceService.removeById(role.getId());
            List<RoleResource> roleResources = new ArrayList<>();
            for (Long id : list) {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(role.getId());
                roleResource.setResourceId(id);
                roleResources.add(roleResource);
            }
            roleResourceService.saveBatch(roleResources);
        } else {
            QueryWrapper<RoleResource> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id", roleId);
            roleResourceService.remove(queryWrapper);
        }
        return true;
    }

    @Override
    public boolean deleteRole(Long[] ids) {
        QueryWrapper<RoleResource> roleResourceWrapper = new QueryWrapper<>();
        QueryWrapper<AdminRole> adminRoleWrapper = new QueryWrapper<>();
        roleResourceWrapper.in("ROLE_ID", ids);
        adminRoleWrapper.in("ROLE_ID", ids);
        roleResourceService.remove(roleResourceWrapper);
        adminRoleService.remove(adminRoleWrapper);
        boolean bool = this.removeByIds(Arrays.asList(ids));
        return bool;
    }
}
