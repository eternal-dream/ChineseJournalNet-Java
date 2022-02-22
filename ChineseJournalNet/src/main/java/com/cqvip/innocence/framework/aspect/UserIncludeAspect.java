package com.cqvip.innocence.framework.aspect;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.exception.FrontUserNoLoginException;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.model.dto.SearchParam;
import com.cqvip.innocence.project.model.entity.UserDatabase;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.service.UserDatabaseService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName UserIncludeAspect
 * @Description 用户包库信息前置处理
 * @Author Innocence
 * @Date 2022/1/10 15:38
 * @Version 1.0
 */
@Aspect
@Component
public class UserIncludeAspect {

    @Autowired
    private UserDatabaseService userDatabaseService;

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(com.cqvip.innocence.common.annotation.UserInclude)")
    public void interceptor() {
    }

    @Around("interceptor()")
    public Object check(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length > 0) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            UserDatabase userDatabaseByUser = getUserDatabaseByUser(request);
            for (Object arg : args) {
                if (arg instanceof SearchParam) {
                    buildSearchParamBeforeExcute(userDatabaseByUser, (SearchParam) arg);
                }
            }
        }

        return pjp.proceed(args);
    }

    /**
     * 根据request获取当前用户的包库信息
     *
     * @param request
     * @return java.util.List<com.cqvip.innocence.project.model.entity.UserDatabase>
     * @author Innocence
     * @date 2022/1/10
     */
    protected UserDatabase getUserDatabaseByUser(HttpServletRequest request) {
        Object o = redisUtil.get(request.getSession().getId());
        if (null == o) {
            throw new FrontUserNoLoginException("前台用户登录状态已过期");
        }
        UserInfo userInfo = (UserInfo) o;
        Date nowTime = new Date();
        LambdaQueryWrapper<UserDatabase> eq = new LambdaQueryWrapper<UserDatabase>()
                .eq(UserDatabase::getUserId, userInfo.getId())
                .eq(UserDatabase::getEffective, true)
                .le(UserDatabase::getBeginTime, nowTime)
                .ge(UserDatabase::getEndTime, nowTime);
        return userDatabaseService.getOne(eq);
    }

    /**
     * 将包库信息前置注入到检索条件
     *
     * @param userDatabaseByUser
     * @param searchParam
     * @return void
     * @author Innocence
     * @date 2022/1/10
     */
    protected void buildSearchParamBeforeExcute(UserDatabase userDatabaseByUser, SearchParam searchParam) {
        List<String> strings = new ArrayList<>();
        String classify = userDatabaseByUser.getClassify();
        if (StrUtil.isNotBlank(classify)) {
            String[] split = classify.split(",");
            for (String s : split) {
                strings.add(s);
            }
        }
        List<String> collect = strings.stream().distinct().collect(Collectors.toList());
        String rule = " ( ";
        for (int i = 0; i < collect.size(); i++) {
            if (i == collect.size() - 1) {
                rule += " OR class:" + collect.get(i).toLowerCase() + "* )";
            } else if (i == 0){
                rule += "class:"+collect.get(i).toLowerCase()+ "*";
            }else {
                rule += " OR class:"+collect.get(i).toLowerCase()+"*";
            }
        }
        Integer inceptionYear = userDatabaseByUser.getInceptionYear();
        Integer terminationYear = userDatabaseByUser.getTerminationYear();
        rule += " AND years:[";
        if (inceptionYear != null){
            rule += inceptionYear.toString();
        }else {
            rule += " *";
        }
        rule += " TO ";
        if (terminationYear != null){
            rule += terminationYear.toString();
        }else {
            rule += "*";
        }
        rule += " ]";
        searchParam.setIncludeRule(rule);
    }

}
