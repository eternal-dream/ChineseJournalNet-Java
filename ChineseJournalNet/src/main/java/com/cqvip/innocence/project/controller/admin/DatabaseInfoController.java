package com.cqvip.innocence.project.controller.admin;


import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.service.DatabaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

/**
 * <p>
 * 数据库表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
@RestController
@RequestMapping("/${base-url.manager}/databaseInfo")
public class DatabaseInfoController extends AbstractController {

 @Autowired
 private DatabaseInfoService databaseInfoService;

 @GetMapping("getAllDatabase")
 public JsonResult getAllDatabase(){
  return JsonResult.Get("databaseList",databaseInfoService.list());
 }
}

