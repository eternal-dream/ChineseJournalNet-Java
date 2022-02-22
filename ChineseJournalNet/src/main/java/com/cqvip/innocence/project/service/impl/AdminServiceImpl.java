package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.mapper.AdminMapper;
import com.cqvip.innocence.project.mapper.AdminRoleMapper;
import com.cqvip.innocence.project.model.vo.AdminRoleVO;
import com.cqvip.innocence.project.model.entity.Admin;
import com.cqvip.innocence.project.model.entity.AdminRole;
import com.cqvip.innocence.project.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName AdminServiceImplements
 * @Description
 * @Author Innocence
 * @Date 2020/7/11
 * @Version 1.0
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private AdminMapper adminMapper;

//    @Override
//    public IPage<AdminRoleView> getAdminsList(AdminRoleView adminRoleView) {
//        IPage<AdminRoleView> list = baseMapper.getAdminsList(adminRoleView);
//        List<AdminRoleView> records = list.getRecords();
//        Set<String> set = new HashSet<>();
//        List<AdminRoleView> newList = new ArrayList<>();
//        for (AdminRoleView arv:records) {
//            if (!set.contains(arv.getAdminName())){
//                set.add(arv.getAdminName());
//                newList.add(arv);
//            }
//        }
//        for (int i = 0; i < newList.size(); i++) {
//            List<Long> ids = new ArrayList<>();
//            List<String> names = new ArrayList<>();
//            List<String> groups = new ArrayList<>();
//            Long roleId = newList.get(i).getRoleId();
//            String roleName = newList.get(i).getRoleName();
//            if (roleId == null){continue;}
//            ids.add(roleId);
//            if (StringUtils.equals("role",newList.get(i).getType())) {
//                names.add(roleName);
//            } else if (StringUtils.equals("group",newList.get(i).getType())) {
//                groups.add(roleName);
//            }
//
//            for (int j = 0; j <records.size(); j++) {
//                Long id = records.get(j).getRoleId();
//                if (id==null){continue;}
//                if (newList.get(i).getAdminName().equals(records.get(j).getAdminName()) && !roleId
//                        .equals(id)){
//                    ids.add(records.get(j).getRoleId());
//                    if (StringUtils.equals("role",records.get(j).getType())) {
//                        names.add(records.get(j).getRoleName());
//                    } else if (StringUtils.equals("group",records.get(j).getType())) {
//                        groups.add(records.get(j).getRoleName());
//                    }
//
//                }
//            }
//            newList.get(i).setRoleIds(ids);
//            newList.get(i).setRoleNames(names);
//            newList.get(i).setGroupNames(groups);
//
//        }
//        list.setRecords(newList);
//        list.setTotal(newList.size());
//        return list;
//    }

    @Override
    public IPage<AdminRoleVO> getAdminsList(AdminRoleVO adminRoleView) {
        IPage<AdminRoleVO> list = baseMapper.getAdminsList(adminRoleView);
        List<AdminRoleVO> records = list.getRecords();
        records.forEach(item->{
            if (StringUtils.isNotBlank(item.getRoleName())) {
                String[] split = item.getRoleName().split(";");
                List<String> roleNames = Arrays.asList(split);
                item.setRoleNames(roleNames);
            }
        });

        list.setTotal(records.size());
        return list;
    }

    @Override
    public void deleteByIds(Object object) {
        JSONObject jsonObject = JSONObject.parseObject(object.toString());
        JSONArray deleteIds = jsonObject.getJSONArray("deleteIds");
        QueryWrapper<AdminRole> wrapper = new QueryWrapper<>();
        List<Long> list = new ArrayList<>();
        for (int i=0;i<deleteIds.size();i++ ) {
            list.add(deleteIds.getLong(i));
            wrapper.eq("admin_id",deleteIds.getLong(i));
            adminRoleMapper.delete(wrapper);
        }
        baseMapper.deleteBatchIds(list);
    }

    @Override
    public void deleteById(Long id) {
        QueryWrapper<AdminRole> wrapper = new QueryWrapper<>();
        wrapper.eq("admin_id",id);
        adminRoleMapper.delete(wrapper);
        baseMapper.deleteById(id);
    }

    @Override
    public Admin getByUsername(String username) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("name",username);
        return adminMapper.selectOne(wrapper);
    }

    @Override
    public List<Admin> getAdminsByRoleId(long roleId) {
        return adminMapper.getAdminsByRoleId(roleId);
    }

    @Override
    public boolean addOrModifyAdminUser(Admin adminUser) {
        adminUser.setIpLimit(getFormatedIpLimit(adminUser.getIpLimit()));
        this.saveOrUpdate(adminUser);
        QueryWrapper<AdminRole> wrapper = new QueryWrapper<>();
        wrapper.eq("ADMIN_ID",adminUser.getId());
        adminRoleMapper.delete(wrapper);
        String roleIds = adminUser.getRoleIds();
        if(StringUtils.isNotBlank(roleIds)){
            String[] roleIdArr = roleIds.split(",");
            for (String roleId : roleIdArr) {
                AdminRole adminUserRole = new AdminRole();
                adminUserRole.setRoleId(Long.valueOf(roleId));
                adminUserRole.setAdminId(adminUser.getId());
                adminRoleMapper.insert(adminUserRole);
            }
        }
        return true;
    }

    private String getFormatedIpLimit(String ipLimit){
        if(StringUtils.isBlank(ipLimit)){
            return "";
        }
        String[] ipArr = ipLimit.split(";");
        for (int i =0;i<ipArr.length;i++) {
            String ipPart = ipArr[i];
            List<String> ips = Arrays.asList(ipPart.split("-"));
            ips = ips.stream().map(ip-> formatIp(ip)).collect(Collectors.toList());
            String join = CollectionUtil.join(ips, "-");
            ipArr[i] = join;
        }
        return ArrayUtil.join(ipArr, ";");
    }

    private String formatIp(String ip){
        String formatIp = "";
        for (String s : ip.split("\\.")) {
            String format = String.format("%0" + 3 + "d", Integer.parseInt(s));
            formatIp+=format+".";
        }
        formatIp =formatIp.substring(0,formatIp.length()-1);
        return formatIp;
    }
}
