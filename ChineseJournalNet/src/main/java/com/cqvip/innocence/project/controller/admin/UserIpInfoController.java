package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.model.entity.UserIpInfo;
import com.cqvip.innocence.project.model.enums.PaymentType;
import com.cqvip.innocence.project.model.enums.UserType;
import com.cqvip.innocence.project.model.vo.UserInfoVO;
import com.cqvip.innocence.project.service.UserIpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户ip限制表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-12-13
 */
@RestController
@RequestMapping("/${base-url.manager}/userIpInfo")
public class UserIpInfoController extends AbstractController {

 @Autowired
 private UserIpInfoService userIpInfoService;

 @PostMapping("addOrModify")
 @Log(title = "新增/修改用户IP绑定信息",operateType = OperateType.SAVE_OR_UPDATE,resourceName = "用户IP绑定")
 public JsonResult addOrModify(@RequestBody UserInfo userInfo){
  JsonResult result = userIpInfoService.addOrModify(userInfo);
  return result;
 }

 @GetMapping("getUserIpInfo")
 @Log(title = "查看用户IP绑定信息",operateType = OperateType.SEARCH,resourceName = "用户IP绑定")
 public JsonResult getUserIpInfo(Long userId){
  LambdaQueryWrapper<UserIpInfo> wrapper = new LambdaQueryWrapper<>();
  wrapper.eq(UserIpInfo::getUserId,userId);
  List<UserIpInfo> list = userIpInfoService.list(wrapper);
  return JsonResult.Get("userIpInfos",list);
 }

}

