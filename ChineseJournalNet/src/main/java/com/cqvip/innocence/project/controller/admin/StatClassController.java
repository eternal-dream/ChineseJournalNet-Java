package com.cqvip.innocence.project.controller.admin;


import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.StatClass;
import com.cqvip.innocence.project.service.StatClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

import java.util.List;

/**
 * <p>
 * 分类限制专用表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
@RestController
@RequestMapping("/${base-url.manager}/statClass")
public class StatClassController extends AbstractController {

 @Autowired
 private StatClassService statClassService;

 @GetMapping("getStatClasses")
 public JsonResult getStatClasses(){
  List<StatClass> list = statClassService.statByName();
  return JsonResult.Get("statClasses",list);
 }
}

