package com.cqvip.innocence.project.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.entity.Admin;
import com.cqvip.innocence.project.model.entity.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Innocence
 * @since 2020-07-13
 */
public interface RoleService extends IService<Role> {
    /**
     *  根据管理员获取角色
     * @author Innocence
     * @date 2020/9/3
     * @param admin
     * @return java.util.List<com.cqvip.innocence.model.entity.Role>
     */
    List<Role> getRolesByAdmin(Admin admin);

   /**
    * 分页获取角色列表
    * @param name
    * @param pageParams
    * @return
    */
   List<Role> getRoles(String name, Page pageParams);

 boolean addOrModifyRole(String name, Long roleId, String description, JSONArray resourceIds);

 boolean deleteRole(Long[] ids);
}
