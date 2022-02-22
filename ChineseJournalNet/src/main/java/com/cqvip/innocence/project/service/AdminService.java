package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.vo.AdminRoleVO;
import com.cqvip.innocence.project.model.entity.Admin;

import java.util.List;

/**
 * @author Innocence
 */
public interface AdminService extends IService<Admin> {

    /**
     * 根据用户名或者角色名获取管理员列表
     * @param adminRoleView
     * @return IPage<AdminRoleView>
     */
    IPage<AdminRoleVO> getAdminsList(AdminRoleVO adminRoleView);

    /**
     * 根据ID批量删除管理员和用户角色关系
     * @param object
     */
    void deleteByIds(Object object);

    /**
     * 根据id删除 管理员和用户角色关系
     * @author Innocence
     * @date 2020/11/24
     * @param id
     * @return void
     */
    void deleteById(Long id);
    /**
     * 通过Admin用户名获取Admin对象
     *
     * @param username 用户名
     * @return {@link Admin}
     * @author 01
     * @date 2021/2/1 14:15
     */
    Admin getByUsername(String username);

    /**
     * 通过角色id查询用户
     *
     * @param roleId 角色id
     * @return  {@link List<  Admin  >}
     */
    List<Admin> getAdminsByRoleId(long roleId);

 boolean addOrModifyAdminUser(Admin adminUser);
}
