package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.Admin;
import com.cqvip.innocence.project.model.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName RoleMapper
 * @Description RoleMapper一些需要自定义sql的接口
 * @Author Innocence
 * @Date 2020/7/14 13:20
 * @Version 1.0
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户获取其所有的角色
     * @author Innocence
     * @date 2020/7/14
     * @param admin
     * @return java.util.List<com.cqvip.innocence.model.entity.Role>
     */
    List<Role> getRolesByAdmin(Admin admin);

    List<Role> getRoles(@Param("name") String name,@Param("page") Page page);
}
