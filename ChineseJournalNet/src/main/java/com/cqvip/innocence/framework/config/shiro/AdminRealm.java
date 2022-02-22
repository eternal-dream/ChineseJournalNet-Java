package com.cqvip.innocence.framework.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.common.util.encryption.Md5Util;
import com.cqvip.innocence.project.model.entity.Admin;
import com.cqvip.innocence.project.model.entity.Resource;
import com.cqvip.innocence.project.model.entity.Role;
import com.cqvip.innocence.project.service.AdminService;
import com.cqvip.innocence.project.service.ResourceService;
import com.cqvip.innocence.project.service.RoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName AdminRealm
 * @Description 自定义shrio的Realm
 * @Author Innocence
 * @Date 2020/7/14 11:00
 * @Version 1.0
 */
public class AdminRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Lazy
    @Autowired
    private AdminService adminService;

    @Lazy
    @Autowired
    private RoleService roleService;

    @Lazy
    @Autowired
    private ResourceService resourceService;

    /**
     * 执行授权逻辑
     * @author Innocence
     * @date 2020/7/14
     * @param principalCollection
     * @return org.apache.shiro.authz.AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Admin admin = (Admin)principalCollection.getPrimaryPrincipal();
        if (null != admin){
            List<String> roleList = new ArrayList<>();
            List<String> permissionLists = new ArrayList<>();
            List<Role> roles = roleService.getRolesByAdmin(admin);
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for (Role role:roles) {
                roleList.add(role.getName());
                List<Resource> resourcesByRoleId = resourceService.getRoleResources(role);
                for (Resource resource:resourcesByRoleId) {
                    permissionLists.add(resource.getPermission());
                }
            }
            info.addRoles(roleList);
            info.addStringPermissions(permissionLists);
            return info;
        }
        return null;
    }

    /**
     * 执行认证逻辑
     * @author Innocence
     * @date 2020/7/14
     * @param authenticationToken
     * @return org.apache.shiro.authc.AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String adminName = token.getUsername();
        String password =String.valueOf(token.getPassword());
        Admin admin = new Admin();
        admin.setUserName(adminName);
        Md5Util md5Util = Md5Util.newInstance();
        admin.setPassword(md5Util.encodePassword(adminName,password));
        LambdaQueryWrapper<Admin> lambda = new QueryWrapper<Admin>().lambda();
        lambda.eq(Admin::getUserName,adminName).eq(Admin::getPassword,
         md5Util.encodePassword(adminName,password));
        Admin one = adminService.getOne(lambda);
        if (null == one){
            //没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常
            return null;
        }else if (!validate(one)){
            throw new LockedAccountException();
        }
        SimpleAuthenticationInfo authoticationInfo = new SimpleAuthenticationInfo(
                //这里根据实际需要，可以放对象，也可以放用户名
                one,
                one.getPassword(),
                ByteSource.Util.bytes(one.getUserName()+"salt"),
                getName()
        );
        return authoticationInfo;
    }

    private boolean validate(Admin one) {
        boolean effect = true;
        if(one.getEffectBeginDate() != null && one.getEffectBeginDate().after(new Date())){
            effect = false;
        }else if(one.getEffectEndDate() != null && one.getEffectEndDate().before(new Date())){
            effect = false;
        }

        return effect;
    }
}
