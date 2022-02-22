package com.cqvip.innocence.project.controller;

import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.common.util.enums.EnumUtil;
import com.cqvip.innocence.common.util.file.FileUtil;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.VerifyCode;
import com.cqvip.innocence.project.service.IverifyCodeGen;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @ClassName CommonController
 * @Description 一些本项目通用接口
 * @Author Innocence
 * @Date 2021/8/26 14:34
 * @Version 1.0
 */
@RestController
@RequestMapping("/common/")
@Api(tags = "一些前后台通用接口")
public class CommonController {

    private String commonEnumsPackageName = "com.cqvip.innocence.common.enums";
    private String projectEnumsPackageName = "com.cqvip.innocence.project.model.enums";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    private IverifyCodeGen iverifyCodeGen;

    @GetMapping("verifyCode")
    @ApiOperation(value = "验证码")
    public JsonResult verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置长宽
        VerifyCode verifyCode = iverifyCodeGen.generate(100, 35);
        String code = verifyCode.getCode();
        //将VerifyCode绑定session
        HttpSession session = request.getSession();
        session.setAttribute(SessionKeys.VcodeKeys.CODE, code);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date startTime=null;
        try {
            startTime = format.parse(format.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        session.setAttribute(SessionKeys.VcodeKeys.CREAT_TIME,startTime);
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(verifyCode.getImgBytes());
        JsonResult get = JsonResult.Get();
        get.put("img",encode);
        get.put("sessionId",session.getId());
        return get;
    }

    @GetMapping("getCommonEnums")
    @ApiOperation(value = "获取枚举值")
    public JsonResult getEnum() throws Exception {
        Map<String, List<EnumUtil.EnumView>> enums = EnumUtil.getEnums(commonEnumsPackageName);
        Map<String, List<EnumUtil.EnumView>> projectEnums = EnumUtil.getEnums(projectEnumsPackageName);
        enums.putAll(projectEnums);
        return JsonResult.Get("enums",enums);
    }

}
