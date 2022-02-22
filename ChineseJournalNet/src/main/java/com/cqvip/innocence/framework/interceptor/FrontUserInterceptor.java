package com.cqvip.innocence.framework.interceptor;

import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.common.bean.RequestWrapper;
import com.cqvip.innocence.common.exception.FrontUserNoLoginException;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.model.enums.VerificationType;
import com.cqvip.innocence.project.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @ClassName FrontUserInterceptor
 * @Description 前台用户的登录状态拦截器
 * @Author Innocence
 * @Date 2021/9/26 16:47
 * @Version 1.0
 */
@Component
public class FrontUserInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserInfoService userInfoService;

    private Boolean loginByIp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> beanType = handlerMethod.getBeanType();
        NoLogin beanAnnotation = beanType.getAnnotation(NoLogin.class);
        NoLogin methodAnnotation = handlerMethod.getMethodAnnotation(NoLogin.class);
        if(beanAnnotation != null || methodAnnotation != null){
            return true;
        }
        LocalDateTime startTime = LocalDateTime.now();
        HttpSession session = null;
        session = request.getSession(false);
        if(session == null){
            return true;
        }
        String id = session.getId();
        UserInfo user = (UserInfo)redisUtil.get(id);
        if (null == user){
            String userId = request.getHeader("user_id");
            if(StringUtils.isNotBlank(userId)){//从参数中获取用户id
                String ip = IpUtils.getIpAddr(request);
                UserInfo userByIp = userInfoService.getUserByIp(ip, VerificationType.Ip);
                //通过请求ip获取用户，如果用户存在并且可以通过ip登录则执行自动登录
                if(userByIp != null && userId.toString().equals(userByIp.getId().toString()) && userByIp.isStatus() && (userByIp.getVerificationType() == VerificationType.All || userByIp.getVerificationType() == VerificationType.Ip)){
                    redisUtil.set(request.getSession().getId(), userByIp, 60 * 60L);
                }
            }
            user = (UserInfo)redisUtil.get(id);
            if(user == null){
                throw new FrontUserNoLoginException("前台用户登录状态已过期");
            }
        }else{
                redisUtil.expire(id,60*60L);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {

    }

}
