package com.cqvip.innocence.project.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.common.util.encryption.Md5Util;
import com.cqvip.innocence.common.util.encryption.SM2Util;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.common.util.iverifycode.IverifyCodeGenUtil;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.Admin;
import com.cqvip.innocence.project.model.entity.AdminRole;
import com.cqvip.innocence.project.model.entity.Resource;
import com.cqvip.innocence.project.model.entity.Role;
import com.cqvip.innocence.project.service.AdminRoleService;
import com.cqvip.innocence.project.service.AdminService;
import com.cqvip.innocence.project.service.ResourceService;
import com.cqvip.innocence.project.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @Author eternal
 * @Date 2021/12/2
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.manager}/admin")
@Api(tags = "?????????????????????")
public class AdminController extends AbstractController {

 @Autowired
 private AdminService adminService;

 @Autowired
 private RoleService roleService;

 @Autowired
 private ResourceService resourceService;

 @Autowired
 private AdminRoleService adminRoleService;

 @PostMapping("login")
 @NoLogin
 @ApiOperation("???????????????")
 @Log(title = "???????????????",operateType = OperateType.LOGIN,resourceName = "???????????????")
 public JsonResult login(String userName, String password, String code, HttpServletRequest request){
  //?????????
  IverifyCodeGenUtil codeGenUtil = IverifyCodeGenUtil.newInstance();
  String codeErrorMsg = codeGenUtil.checkCode(request.getSession(), code);
  if (StringUtils.isNoneBlank(codeErrorMsg)) {
   return JsonResult.Get(false, codeErrorMsg);
  }
  if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
   return JsonResult.Get(false, "??????????????????????????????");
  }
  Subject subject = SecurityUtils.getSubject();
//  if (subject.isAuthenticated()) {
//   return JsonResult.Get(false, "?????????????????????????????????");
//  }
  SM2Util sm2Util = new SM2Util();
  String decrypt = sm2Util.decrypt(password, SM2Util.PRIAVTEKEY);
  UsernamePasswordToken passwordToken = new UsernamePasswordToken(userName, decrypt);
  JsonResult res = JsonResult.Get();

  try {
   subject.login(passwordToken);
   //???AdminRealm????????????SimpleAuthenticationInfo????????????????????????????????????
   //subject.getPrincipal()????????????SimpleAuthenticationInfo????????????????????????
   Admin loginAdmin = (Admin) subject.getPrincipal();
   //??????ip????????????????????????
   boolean validateIp = validateIp(loginAdmin.getIpLimit(),request);
   if(!validateIp){
    subject.logout();
    return JsonResult.Get(false,"??????????????????"+loginAdmin.getIpLimit()+"IP?????????\n??????IP???:"+IpUtils.getIpAddr(request));
   }
   Session session = subject.getSession();
   loginAdmin.setPassword("");
   session.setAttribute(SessionKeys.LOGIN_ADMIN_KEY, loginAdmin);
   //????????????????????????session????????????????????????token?????????????????????????????????????????????????????????

   res.put("token", session.getId());
   res.put("admin", loginAdmin);
  } catch (LockedAccountException e) {
   res.set(false, "??????????????????");
  } catch (UnknownAccountException e) {
   res.set(false, "???????????????????????????");
  }

  return res;
 }

 @GetMapping("getAdminInfo")
 @Log(title = "?????????????????????",operateType = OperateType.SEARCH,resourceName = "???????????????")
 public JsonResult getAdminInfo(){
  Subject subject = SecurityUtils.getSubject();
  Admin principal =(Admin) subject.getPrincipal();
  //???????????????????????????????????????????????????
  Admin byId = adminService.getById(principal.getId());
  byId.setPassword(null);
  //?????????????????????????????????????????????????????????
  List<Role> rolesByAdmin = roleService.getRolesByAdmin(byId);
  HashSet<String> roleSet = new HashSet<>();
  HashSet<String> permissionSet = new HashSet<>();
  rolesByAdmin.forEach(item -> {
   roleSet.add(item.getName());
   List<Resource> list = resourceService.getRoleResources(item);
   list.forEach(reduce -> permissionSet.add(reduce.getPermission()));
  });
  JsonResult res = JsonResult.Get();
  res.put("admin",byId);
  res.put("roles",roleSet);
  res.put("per",permissionSet);
  return res;
 }

 @GetMapping("getAdminUserList")
 @ApiOperation("???????????????????????????")
 @Log(title = "?????????????????????",operateType = OperateType.SEARCH,resourceName = "???????????????")
 public JsonResult getAdminUserList(String userName,String realName) {
  LambdaQueryWrapper<Admin> wrapper = new QueryWrapper<Admin>().lambda();
  wrapper.like(StringUtils.isNotBlank(userName), Admin::getUserName, userName)
   .like(StringUtils.isNotBlank(realName), Admin::getRealName,realName);
  Page<Admin> userPage = adminService.page(getPageParams(), wrapper);
  userPage.getRecords().forEach(user -> user.setPassword(null));
  return JsonResult.Get("userPage", userPage);
 }

 @PostMapping("addOrModifyAdminUser")
 @ApiOperation("???????????????????????????")
 @Log(title = "??????/?????????????????????",operateType = OperateType.SAVE_OR_UPDATE,resourceName = "???????????????")
 public JsonResult addOrModifyAdminUser(Admin adminUser,HttpServletRequest request) {
  if(StrUtil.isNotBlank(adminUser.getPassword())){
   SM2Util sm2Util = new SM2Util();
   String decryptPassword = sm2Util.decrypt(adminUser.getPassword(), SM2Util.PRIAVTEKEY);
   Md5Util md5Util = Md5Util.newInstance();
   adminUser.setPassword(md5Util.encodePassword(adminUser.getUserName(), decryptPassword));
  }else{
   adminUser.setPassword(null);
  }
  adminService.addOrModifyAdminUser(adminUser);
  return JsonResult.Get();
 }

 @PostMapping("deleteAdminUser")
 @Log(title = "?????????????????????",operateType = OperateType.DELETE,resourceName = "???????????????")
 public JsonResult deleteAdminUser(@RequestBody Long[] ids) {
  LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
  wrapper.in(AdminRole::getAdminId,ids);
  adminRoleService.remove(wrapper);
  adminService.removeByIds(Arrays.asList(ids));
  return JsonResult.Get();
 }

 @PostMapping("logout")
 @ApiOperation("?????????????????????")
 public JsonResult adminLogout() {
  Subject subject = SecurityUtils.getSubject();
  subject.logout();
  subject.getSession().removeAttribute(SessionKeys.LOGIN_ADMIN_KEY);
  return JsonResult.Get();
 }

 private boolean validateIp(String ipLimit,HttpServletRequest request){
  if(StringUtils.isBlank(ipLimit)){
   return true;
  }
  String ipAddr = IpUtils.getIpAddr(request);
  String ip = formatIp(ipAddr);
  String[] split = ipLimit.split(";");
  for (int i = 0; i < split.length; i++) {
   String ipPart = split[i];
   String startIp = "999999999";
   String finishIp = "0";
   String targetIp = "0";
   if(!ipPart.contains("-")){
    targetIp = ipPart;
   }else{
    startIp = ipPart.split("-")[0];
    finishIp = ipPart.split("-")[1];
   }
   if(targetIp.equals(ip) || (Long.valueOf(startIp.replaceAll("\\.",""))<=Long.valueOf(ip.replaceAll("\\.","")) && Long.valueOf(finishIp.replaceAll("\\.",""))>=Long.valueOf(ip.replaceAll("\\.","")))){
    return true;
   }
  }
  return false;
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