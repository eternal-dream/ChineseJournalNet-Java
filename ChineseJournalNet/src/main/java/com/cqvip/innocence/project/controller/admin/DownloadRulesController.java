package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.DownloadRules;
import com.cqvip.innocence.project.service.DownloadRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

import java.util.List;

/**
 * <p>
 * 文献下载限制规则表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-12-14
 */
@RestController
@RequestMapping("/${base-url.manager}/downloadRules")
public class DownloadRulesController extends AbstractController {

 @Autowired
 private DownloadRulesService downloadRulesService;

 @GetMapping("getRules")
 public JsonResult getRules(Long userId,Long groupId){
  if(userId == null && groupId == null) {
   return JsonResult.Get(false);
  }
  LambdaQueryWrapper<DownloadRules> wrapper = new LambdaQueryWrapper<>();
  wrapper.eq(userId != null,DownloadRules::getUserId,userId)
   .eq(groupId != null,DownloadRules::getGroupId,groupId);
  List<DownloadRules> list = downloadRulesService.list(wrapper);
  return JsonResult.Get("downloadRules",list);
 }

}

