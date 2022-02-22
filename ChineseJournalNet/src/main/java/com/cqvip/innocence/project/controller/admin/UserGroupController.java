package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.DownloadRules;
import com.cqvip.innocence.project.model.entity.UserGroup;
import com.cqvip.innocence.project.model.enums.DownloadRule;
import com.cqvip.innocence.project.service.DownloadRulesService;
import com.cqvip.innocence.project.service.UserGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 用户组表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-12-13
 */
@RestController
@RequestMapping("/${base-url.manager}/userGroup")
public class UserGroupController extends AbstractController {

 @Autowired
 private UserGroupService userGroupService;

 @Autowired
 private DownloadRulesService downloadRulesService;

 @GetMapping("getGroupPage")
 @Log(title = "查看用户组",operateType = OperateType.SEARCH,resourceName = "用户组管理")
 public JsonResult getGroupPage(Long id,String name){
  LambdaQueryWrapper<UserGroup> wrapper = new LambdaQueryWrapper<>();
  wrapper.like(StringUtils.isNotBlank(name), UserGroup::getName,name);
  Page page = userGroupService.page(getPageParams(), wrapper);
  return JsonResult.Get("groupPage",page);
 }

 @PostMapping("addOrModify")
 @Log(title = "新增/修改用户组",operateType = OperateType.SAVE_OR_UPDATE,resourceName = "用户组管理")
 public JsonResult addOrModify(@RequestBody UserGroup group){
  List<DownloadRules> downloadRules = group.getDownloadRules();
  List<DownloadRule> rules = new ArrayList<>();
  downloadRules.forEach(item->{
   rules.add(item.getRule());
   item.setGroupId(group.getId());
  });
  if(rules.size()>new HashSet<>(rules).size()) {
   return JsonResult.Get(false,"存在类型重复的规则,请检查!");
  }
  userGroupService.addOrModify(group);

  return JsonResult.Get();
 }

 @GetMapping("getAllGroups")
 public JsonResult getAllGroups(){
  List<UserGroup> groups = userGroupService.list();
  return JsonResult.Get("allGroups",groups);
 }

 @PostMapping("deleteGroup")
 @Log(title = "删除用户组",operateType = OperateType.DELETE,resourceName = "用户组管理")
 public JsonResult deleteRole(@RequestBody Long[] deleteIds) {
  if (deleteIds == null || deleteIds.length == 0) {
   return JsonResult.Get(false, "请选择要删除的用户组!");
  }
  boolean flag = userGroupService.removeByIds(Arrays.asList(deleteIds));
  return JsonResult.Get();

 }
}

