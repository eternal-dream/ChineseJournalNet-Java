package com.cqvip.innocence.project.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.AdminRole;
import com.cqvip.innocence.project.model.entity.Role;
import com.cqvip.innocence.project.model.entity.RoleResource;
import com.cqvip.innocence.project.service.AdminRoleService;
import com.cqvip.innocence.project.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author eternal
 * @Date 2021/12/3
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.manager}/role")
public class RoleController extends AbstractController {

 @Autowired
 private RoleService roleService;

 @Autowired
 private AdminRoleService adminRoleService;

 @GetMapping("getTotalRoles")
 public JsonResult getTotalRoles() {
  return JsonResult.Get("totalRoles", roleService.list());
 }

 @GetMapping("getAdminRoles")
 public JsonResult getAdminRoles(Long id){
  if(id == null ){
   return JsonResult.Get();
  }
  LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
  wrapper.eq(AdminRole::getAdminId,id);
  List<AdminRole> list = adminRoleService.list(wrapper);
  if(list.size() == 0){
   return JsonResult.Get("roles",null);
  }
  List<Long> roleIds = new ArrayList<>();
  list.forEach(item->roleIds.add(item.getRoleId()));
  List<Role> roles = roleService.listByIds(roleIds);
  return JsonResult.Get("roles",roles);
 }

 @GetMapping("getRoles")
 @Log(title = "查看操作员组",operateType = OperateType.SEARCH,resourceName = "操作员组管理")
 public JsonResult getRoles(String name){
  LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
  wrapper.like(StringUtils.isNotBlank(name),Role::getName,name);
  List<Role> roles = roleService.getRoles(name,getPageParams());
  roles.forEach(role-> {
   if(role.getResourceStr()==null){
    return;
   }
   String[] split = role.getResourceStr().split(",");
   List<Long> ids = new ArrayList<>();
   for (String id : split) {
    ids.add(Long.parseLong(id));
   }
   role.setResourceIds(ids);
  });
  int count = roleService.count(wrapper);
  return JsonResult.Get("roles",roles).put("total",count);
 }


 @PostMapping("addOrModifyRole")
 @Log(title = "新增/修改操作员组",operateType = OperateType.SAVE_OR_UPDATE,resourceName = "操作员组管理")
 public JsonResult addOrModifyRole(@RequestBody Object object) {
  JSONObject jsonObject = JSONObject.parseObject(object.toString());
  String name = jsonObject.getString("name");
  Long roleId = jsonObject.getLong("id");
  String description = jsonObject.getString("description");
  JSONArray resourceIds = jsonObject.getJSONArray("resourceIds");
  if (StringUtils.isBlank(name)) {
   return JsonResult.Get(false, "角色名不能为空!");
  }
  roleService.addOrModifyRole(name,roleId,description,resourceIds);
  return JsonResult.Get();
 }

 @PostMapping("deleteRole")
 @Log(title = "删除操作员组",operateType = OperateType.DELETE,resourceName = "操作员组管理")
 public JsonResult deleteRole(@RequestBody Long[] deleteIds) {
  if (deleteIds == null || deleteIds.length == 0) {
   return JsonResult.Get(false, "请提交要删除的角色id");
  }
  boolean flag = roleService.deleteRole(deleteIds);
  return JsonResult.Get();

 }
}