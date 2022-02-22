package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqvip.innocence.project.model.vo.AdminRoleVO;
import com.cqvip.innocence.project.model.entity.Admin;

import java.util.List;

/**
 * @author Innocence
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据用户名或者角色名获取管理员列表
     * @param adminRoleView
     * @return IPage<AdminRoleView>
     */
    IPage<AdminRoleVO> getAdminsList(AdminRoleVO adminRoleView);

    /**
     * 通过角色id查询用户list
     * @param roleId 角色id
     */
    List<Admin> getAdminsByRoleId(long roleId);
}
