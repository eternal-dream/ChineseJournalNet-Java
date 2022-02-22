package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.common.util.encryption.SM2Util;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.common.util.iverifycode.IverifyCodeGenUtil;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.model.enums.VerificationType;
import com.cqvip.innocence.project.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @Author eternal
 * @Date 2021/12/21
 * @Version 1.0
 */
@RestController("frontUserController")
@RequestMapping("/${base-url.front}/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping("tryIpLogin")
    @NoLogin
    public JsonResult tryIpLogin(HttpServletRequest request) {
        String ip = IpUtils.getIpAddr(request);
        UserInfo userInfo = userInfoService.getUserByIp(ip, VerificationType.Ip);
        if (userInfo == null) {
            return JsonResult.Get(false,"登录失败,您的IP地址不在许可范围之内，您当前IP为"+ip);
        }
        if (!userInfo.isStatus()) {
            return JsonResult.Get(false, "账号暂时无法使用,请联系管理员!");
        }
        redisTemplate.opsForValue().set(request.getSession().getId(), userInfo, 60 * 60 * 24, TimeUnit.SECONDS);
        return JsonResult.Get("user", userInfo);
    }

    @PostMapping("logout")
    public JsonResult logout(HttpServletRequest request) {
        redisTemplate.delete(request.getSession().getId());
        return JsonResult.Get();
    }

    @PostMapping("login")
    @NoLogin
    public JsonResult login(String userName, String password, String code, HttpServletRequest request) {
        //验证码
        IverifyCodeGenUtil codeGenUtil = IverifyCodeGenUtil.newInstance();
        String codeErrorMsg = codeGenUtil.checkCode(request.getSession(), code);
        if (StringUtils.isNoneBlank(codeErrorMsg)) {
            return JsonResult.Get(false, codeErrorMsg);
        }
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserName, userName);
        UserInfo userInfo = userInfoService.getOne(wrapper);
        if(userInfo == null){
            return JsonResult.Get(false, "用户名或密码错误!");
        }else if(userInfo.getVerificationType() == VerificationType.Ip){
            return JsonResult.Get(false, "此用户仅适用于IP方式登录!");
        }
        SM2Util sm2Util = new SM2Util();
        String decrypt = sm2Util.decrypt(password, SM2Util.PRIAVTEKEY);
        String savedPass = sm2Util.decrypt(userInfo.getPassword(), SM2Util.PRIAVTEKEY);
        if (!decrypt.equals(savedPass)) {
            return JsonResult.Get(false, "用户名或密码错误!");
        }
        if (!userInfo.isStatus()) {
            return JsonResult.Get(false, "账号暂时无法使用,请联系管理员!");
        }
        userInfo.setPassword(null);
        redisTemplate.opsForValue().set(request.getSession().getId(), userInfo, 60 * 60, TimeUnit.SECONDS);
        return JsonResult.Get("user", userInfo);
    }

    @GetMapping("getLoginUser")
    public JsonResult getLoginUser(HttpServletRequest request) {
        UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(request.getSession().getId());
        return JsonResult.Get("user", userInfo);
    }

    @GetMapping("getLoginUserFromDb")
    public JsonResult getLoginUserFromDb(HttpServletRequest request) {
        UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(request.getSession().getId());
        if(userInfo!=null){
            userInfo = userInfoService.getById(userInfo.getId());
        }
        return JsonResult.Get().put("data", userInfo);
    }
}
