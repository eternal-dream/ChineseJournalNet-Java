package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.UserLog;
import com.cqvip.innocence.project.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

import java.util.List;

/**
 * <p>
 * 用户日志表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2022-01-07
 */
@RestController
@RequestMapping("/${base-url.manager}/userLog")
public class UserLogController extends AbstractController {

    @Autowired
    private UserLogService userLogService;

    //获取所有页面板块
    @GetMapping("getAllPlates")
    public JsonResult getAllPlates(){
        List<String> plates = userLogService.getAllPlates();
        return JsonResult.Get("plates",plates);
    }
}

