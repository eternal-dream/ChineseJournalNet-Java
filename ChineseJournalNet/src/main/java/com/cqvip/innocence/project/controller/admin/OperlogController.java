package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.Operlog;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.service.OperlogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

import java.time.LocalDate;

/**
 * <p>
 * 管理员操作日志 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-12-06
 */
@RestController
@RequestMapping("/${base-url.manager}/operlog")
public class OperlogController extends AbstractController {

 @Autowired
 private OperlogService operlogService;

 @GetMapping("getLogPage")
 @Log(title = "查看操作日志",operateType = OperateType.SEARCH,resourceName = "操作日志")
 public JsonResult getLogPage(String userName, OperateType operateType,
                              String startTime,String endTime,String resourceName){
  LambdaQueryWrapper<Operlog> wrapper = new LambdaQueryWrapper<>();
  wrapper.like(StringUtils.isNotBlank(userName),Operlog::getUserName,userName)
   .ge(StringUtils.isNotBlank(startTime), BaseModel::getCreateTime,startTime)
   .le(StringUtils.isNotBlank(endTime), BaseModel::getCreateTime,endTime)
   .eq(StringUtils.isNotBlank(resourceName),Operlog::getResourceName,resourceName)
   .eq(operateType!=null,Operlog::getOperateType,operateType)
   .orderByDesc(BaseModel::getCreateTime);
  Page page = operlogService.page(getPageParams(), wrapper);
  return JsonResult.Get("page",page);
 }

}

