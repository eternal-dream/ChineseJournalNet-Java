package com.cqvip.innocence.project.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.util.encryption.SM2Util;
import com.cqvip.innocence.common.util.enums.EnumUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.DownloadRules;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.mapper.UserInfoMapper;
import com.cqvip.innocence.project.model.entity.UserIpInfo;
import com.cqvip.innocence.project.model.enums.*;
import com.cqvip.innocence.project.model.vo.UserInfoVO;
import com.cqvip.innocence.project.service.DownloadRulesService;
import com.cqvip.innocence.project.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.service.UserIpInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户基础信息表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-10
 */
@Service
@Transactional
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

 @Autowired
 @Lazy
 private UserIpInfoService userIpInfoService;

 @Autowired
 @Lazy
 private DownloadRulesService downloadRulesService;

 @Value("${isWeakPwdEnable}")
 private boolean isWeakPwdEnable;

 @Override
 public Page<UserInfoVO> getUserInfoPage(boolean filterByIP, String userName, String unit, String linkMan, String email, UserType userType, Boolean status, PaymentType paymentType, Long userGroupId, String remark, String ip, String ipUnit, String ipRemark, Page page) {
  return baseMapper.getUserInfoPage(filterByIP, userName, unit, linkMan, email, userType, status, paymentType, userGroupId, remark, ip, ipUnit, ipRemark, page);
 }

 @Override
 public JsonResult addOrModify(UserInfo userInfo) {
  JsonResult result = userIpInfoService.addOrModify(userInfo);
  if(!result.getSuccess()){
   return result;
  }
  userInfo.insertOrUpdate();
  List<Long> ids = new ArrayList<>();
  userInfo.getDownloadRules().forEach(item->{
   item.setUserId(userInfo.getId());
   ids.add(item.getId());
  });
  LambdaQueryWrapper<DownloadRules> deleteWrapper = new LambdaQueryWrapper<>();
  deleteWrapper.notIn(ids.size()>0,DownloadRules::getId,ids)
   .eq(DownloadRules::getUserId,userInfo.getId());
  downloadRulesService.remove(deleteWrapper);
  downloadRulesService.saveOrUpdateBatch(userInfo.getDownloadRules());
  return result;
 }

 @Override
 public UserInfo getUserByIp(String ip) {
  return getUserByIp(ip,null);
 }

 @Override
 public UserInfo getUserByIp(String ip, VerificationType verificationType) {
  String formatIp = "";
  for (String s : ip.split("\\.")) {
   String format = String.format("%0" + 3 + "d", Integer.parseInt(s));
   formatIp+=format+".";
  }
  log.info("ip:"+formatIp);
  return baseMapper.getUserByIp(formatIp,verificationType);
 }

 @Override
 public List<UserInfo> getUserByIpRange(String startIp, String finishIp) {
  return baseMapper.getUserByIpRange(startIp,finishIp);
 }

 @Override
 public JsonResult importUser(MultipartFile file) {
  String regex = "/^.*(?=.{8,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$/";
  String regex2 = "^(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";
  Pattern compile = Pattern.compile(regex2);
  InputStream inputStream = null;
  try {
   inputStream = file.getInputStream();
  } catch (IOException e) {
   e.printStackTrace();
  }
  ExcelReader reader = new ExcelReader(inputStream, 0);
  List<List<Object>> read = reader.read();
  read.remove(0);
  List<UserInfo> userList = new ArrayList<>();
  Map<Integer, Integer> limitMap = new HashMap<>();
  for (int i = 0; i < read.size(); i++) {
   List<Object> item = read.get(i);
   if(item.size()<19){
    return JsonResult.Get(false, "第" + (i + 1) + "条用户信息数据不完整,导入失败!");
   }
   UserInfo userInfo = new UserInfo();
   //用户名
   Object name = item.get(0);
   if (name == null) {
    return JsonResult.Get(false, "第" + (i + 1) + "条用户信息用户名为空,导入失败!");
   }
   LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
   wrapper.eq(UserInfo::getUserName,name.toString());
   int count = count(wrapper);
   if(count > 0 || userList.stream().filter(u->u.getUserName().equals(name.toString())).count() > 0){
    return JsonResult.Get(false, "第" + (i + 1) + "条用户信息用户名已被使用,导入失败!");
   }
   userInfo.setUserName(name.toString());
   //密码
   Object password = item.get(1);
   if (password == null) {
    return JsonResult.Get(false, "第" + (i + 1) + "条用户信息密码为空,导入失败!");
   }
   if(!isWeakPwdEnable){
    Matcher matcher = compile.matcher(password.toString());
    if(!matcher.find()){
     return JsonResult.Get(false, "第" + (i + 1) + "条用户信息密码为弱密码,导入失败!");
    }
   }
   SM2Util sm2Util = new SM2Util();
   userInfo.setPassword(sm2Util.encrypt(password.toString(), SM2Util.PUBLICKEY));
   //用户类型
   Object userType = item.get(2);
   if (userType == null) {
    return JsonResult.Get(false, "第" + (i + 1) + "条用户信息未选择用户类型,导入失败!");
   }
   userInfo.setUserType(EnumUtil.getByAlias(UserType.class, userType.toString()));
   //用户状态
   Object status = item.get(3);
   if (status == null) {
    userInfo.setStatus(false);
   } else if ("有效".equals(status.toString())) {
    userInfo.setStatus(true);
   } else if ("无效".equals(status.toString())) {
    userInfo.setStatus(false);
   }
   //用户付费方式
   Object paymentType = item.get(4);
   if (paymentType == null) {
    return JsonResult.Get(false, "第" + (i + 1) + "条用户信息未选择用户付费方式,导入失败!");
   }
   userInfo.setPaymentType(EnumUtil.getByAlias(PaymentType.class, paymentType.toString()));
   //用户所属区域
   Object area = item.get(5);
   if (area == null) {
    userInfo.setArea("其它");
   } else {
    userInfo.setArea(area.toString());
   }
   //地区（省份）
   Object province = item.get(6);
   userInfo.setProvince(province == null ? null : province.toString());
   //用户行业
   Object industry = item.get(7);
   userInfo.setIndustry(industry == null ? null : industry.toString());
   //用户单位类型
   Object unitType = item.get(8);
   if (unitType != null) {
    userInfo.setUnitType(EnumUtil.getByAlias(UnitType.class, unitType.toString()));
   }
   //邮政编码
   Object postCode = item.get(9);
   userInfo.setPostCode(postCode == null ? null : postCode.toString());
   //单位名称
   Object unit = item.get(10);
   userInfo.setUnit(unit == null ? null : unit.toString());
   //通讯地址
   Object address = item.get(11);
   userInfo.setAddress(address == null ? null : address.toString());
   //电话
   Object phone = item.get(12);
   userInfo.setPhone(phone == null ? null : phone.toString());
   //传真
   Object fax = item.get(13);
   userInfo.setFax(fax == null ? null : fax.toString());
   //邮箱
   Object email = item.get(14);
   userInfo.setEmail(email == null ? null : email.toString());
   //联系人
   Object linkMan = item.get(15);
   userInfo.setLinkMan(linkMan == null ? null : linkMan.toString());
   //备注
   Object remark = item.get(16);
   userInfo.setRemark(remark == null ? null : remark.toString());
   //限制每月流量
   Object limit = item.get(17);
   if (limit != null) {
    limitMap.put(i, Integer.valueOf(limit.toString()));
   }
   //登录验证方式
   Object verificationType = item.get(18);
   if (verificationType != null) {
    userInfo.setVerificationType(EnumUtil.getByAlias(VerificationType.class, verificationType.toString()));
   }
   userList.add(userInfo);
  }
  this.saveBatch(userList);
  List<DownloadRules> downloadRulesList = new ArrayList<>();
  limitMap.forEach((k, v) -> {
   DownloadRules downloadRules = new DownloadRules();
   downloadRules.setUserId(userList.get(k).getId());
   downloadRules.setRule(DownloadRule.Per_month);
   downloadRules.setLimit(v);
   downloadRulesList.add(downloadRules);
  });
  downloadRulesService.saveBatch(downloadRulesList);
  return JsonResult.Get();
 }
}
