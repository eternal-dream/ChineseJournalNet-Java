package com.cqvip.innocence.project.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.router.Meta;
import com.cqvip.innocence.project.model.dto.router.Router;
import com.cqvip.innocence.project.model.entity.Admin;
import com.cqvip.innocence.project.model.entity.Resource;
import com.cqvip.innocence.project.model.entity.Role;
import com.cqvip.innocence.project.service.ResourceService;
import com.cqvip.innocence.project.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author eternal
 * @Date 2021/12/2
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.manager}/resource")
public class ResourceController extends AbstractController {
 @Autowired
 private ResourceService resourceService;

 @Autowired
 private RoleService roleService;


 @GetMapping("getAllMenu")
 public JsonResult getAllMenu(){
  LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
  wrapper.eq(Resource::getMenuType,"C")
   .eq(Resource::getIsHidden,false)
   .orderByAsc(Resource::getSort);
  List<Resource> list = resourceService.list(wrapper);
  return JsonResult.Get("resources",list);
 }

 /**
  * 获取所有菜单资源
  *
  * @return
  */
 @GetMapping("getAllResources")
 public JsonResult getTotalResources() {
  List<Resource> resources = resourceService.list().stream().sorted(Comparator.comparing(Resource::getSort)).collect(Collectors.toList());
  List<Router> routers = resourceService.processRouter(resources);
//  List<Router> routers = new ArrayList<>();
//  Router router = new Router();
//  router.setTitle("测试");
//  router.setName("test");
//  router.setPath("/demo/components/container/full");
//  router.setIcon("");
//  router.setId(111L);
//
//  Router router1 = new Router();
//  router1.setTitle("测试2");
//  router1.setPath("/demo/test1");
//  router1.setIcon("");
//  router1.setComponent("demo/test1.vue");
//  Meta meta = new Meta();
//  meta.setNoCache(false);
//  meta.setTitle("测试1111");
//  router1.setMeta(meta);
//  router1.setName("test1");
//  router1.setParentId(111L);
//
//  List<Router> children = new ArrayList<>();
//  children.add(router1);
//  router.setChildren(children);
//  routers.add(router);

  return JsonResult.Get("resources", routers);
 }

 /**
  * 根据当前登录用户获取菜单
  */
 @GetMapping("getUserResource")
 public JsonResult getParentResource() {
  Subject subject = SecurityUtils.getSubject();
  Admin loginAdmin = (Admin) subject.getPrincipal();
  if (loginAdmin == null) {
   return JsonResult.Get();
  }

  List<Role> roles = roleService.getRolesByAdmin(loginAdmin);
  List<Resource> resources = new ArrayList<>();
  for (Role role : roles) {
   List<Resource> roleResources = resourceService.getRoleResources(role);
   roleResources.forEach(resource -> {
    if (!"F".equals(resource.getMenuType())) {
     resources.add(resource);
    }
   });
  }

  List<Router> routers = resourceService.processRouter(resources);
  return JsonResult.Get("resources", routers);

 }



 @PostMapping("saveOrEditResource")
 @ApiOperation("新增或者编辑resource")
 @RequiresPermissions("admin:resource:addOrEdit")
 public JsonResult saveOrEditResource(Resource resource) {
  try {
   resourceService.saveOrUpdate(resource);
  } catch (Exception ex) {
   return JsonResult.Get(false, "操作失败");
  }
  return JsonResult.Get(true, "操作成功");
 }

 @PostMapping("deleteResourceByIds")
 @ApiOperation("删除一个或多个resource")
 //@RequiresPermissions("admin:resource:delete")
 public JsonResult deleteResourceByIds(@RequestBody Long[] ids) {
  try {
   resourceService.removeByIds(Arrays.asList(ids));
  } catch (Exception ex) {
   ex.printStackTrace();
   return JsonResult.Get(false, "删除失败");
  }
  return JsonResult.Get(true, "删除成功");
 }

}