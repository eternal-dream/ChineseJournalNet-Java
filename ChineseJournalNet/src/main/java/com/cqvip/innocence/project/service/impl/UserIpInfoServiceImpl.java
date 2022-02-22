package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.model.entity.UserIpInfo;
import com.cqvip.innocence.project.mapper.UserIpInfoMapper;
import com.cqvip.innocence.project.model.vo.UserInfoVO;
import com.cqvip.innocence.project.service.UserInfoService;
import com.cqvip.innocence.project.service.UserIpInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户ip限制表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-13
 */
@Service
public class UserIpInfoServiceImpl extends ServiceImpl<UserIpInfoMapper, UserIpInfo> implements UserIpInfoService {

 @Autowired
 private UserInfoService userInfoService;

 @Override
 public JsonResult addOrModify(UserInfo userInfo) {
  for(UserIpInfo item :userInfo.getUserIpInfos()){
   String startIp = formatIp(item.getStartIp());
   String finishIp = formatIp(item.getFinishIp());
   startIp = startIp.substring(0,startIp.length()-1);
   finishIp = finishIp.substring(0,finishIp.length()-1);
   if(Long.valueOf(startIp.replaceAll("\\.",""))>Long.valueOf(finishIp.replaceAll("\\.",""))){
    return JsonResult.Get(false,("开始IP"+item.getStartIp()+"不能大于结束IP"+item.getFinishIp()));
   }
   //检查ip是否已被占用
//   UserInfo user1 = userInfoService.getUserByIp(startIp);
//   UserInfo user2 = userInfoService.getUserByIp(finishIp);
   List<UserInfo> users = userInfoService.getUserByIpRange(startIp,finishIp);
   if(users.size()>1){
    String userNames = users.stream().filter(i -> !i.getId().equals(userInfo.getId())).map(i -> i.getUserName()).collect(Collectors.joining(","));
    return JsonResult.Get(false,(item.getStartIp()+"与用户 "+userNames+" 绑定的IP冲突!"));
   }
   item.setStartIp(startIp);
   item.setFinishIp(finishIp);
   item.setUserId(userInfo.getId());
   CopyOnWriteArrayList<UserIpInfo> copy = CollectionUtil.newCopyOnWriteArrayList(userInfo.getUserIpInfos());
   copy.remove(item);
   List conflictIps = copy.stream().filter(i->{
    long ip1 = Long.valueOf(i.getStartIp().replaceAll("\\.", ""));
    long ip2 = Long.valueOf(i.getFinishIp().replaceAll("\\.", ""));
    long itemIp1 = Long.valueOf(item.getStartIp().replaceAll("\\.", ""));
    long itemIp2 = Long.valueOf(item.getFinishIp().replaceAll("\\.", ""));
    if((ip1<=itemIp1 && ip2>=itemIp1)||(ip1<=itemIp2 && ip2>=itemIp2)){
     return true;
    }
    return false;
   }).collect(Collectors.toList());
   if(conflictIps.size()>0){
    return JsonResult.Get(false,"输入的ip地址之间有冲突,操作失败!");
   }
  };
  List<Long> ids = userInfo.getUserIpInfos().stream().map(i -> i.getId()).collect(Collectors.toList());
  LambdaQueryWrapper<UserIpInfo> deleteWrapper = new LambdaQueryWrapper<>();
  deleteWrapper.notIn(ids.size()>0,UserIpInfo::getId,ids)
   .eq(UserIpInfo::getUserId,userInfo.getId());
  this.remove(deleteWrapper);
  this.saveOrUpdateBatch(userInfo.getUserIpInfos());
  return JsonResult.Get();
 }

 @Override
 public UserIpInfo selectByIp(String ip,Long userId) {
  ip = formatIp(ip);
  return baseMapper.selectByIp(ip,userId);
 }

 private String formatIp(String ip){
  String formatIp = "";
  for (String s : ip.split("\\.")) {
   String format = String.format("%0" + 3 + "d", Integer.parseInt(s));
   formatIp+=format+".";
  }
  return formatIp;
 }
}
