package com.cqvip.innocence.framework.config.aspect;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.common.util.html.ServletUtils;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.Admin;
import com.cqvip.innocence.project.model.entity.Operlog;
import com.cqvip.innocence.project.service.OperlogService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Date;
import java.util.Map;


/**
 * 操作日志注解处理
 * @author Innocence
 * @date 2021/1/5
 */
@Aspect
@Component
public class LogAspect extends AbstractAspect<Log> {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private OperlogService operlogService;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Value("${base-url.front}")
//    public String frontBaseUrl;

    /**
     * 配置织入点
     * @author Innocence
     * @date 2020/12/30
     */
    @Pointcut("@annotation(com.cqvip.innocence.common.annotation.Log)")
    public void logPointCut() {
    }


    /**
     * 处理完请求后执行
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, JsonResult jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, JsonResult jsonResult) {
        try {
            // 获得注解
            Log controllerLog = super.getAnnotationTag(joinPoint, Log.class);
            if (controllerLog == null) {
                return;
            }
            // 获取当前的用户
            Admin loginAdmin = null;
            Object loginAdminObj = SecurityUtils.getSubject().getSession().getAttribute(SessionKeys.LOGIN_ADMIN_KEY);
            if(loginAdminObj !=null ){
                loginAdmin = (Admin) loginAdminObj;
            }
            // *========数据库日志=========*//
            Operlog userLog = new Operlog();
            userLog.setStatus(true);
            // 请求的地址
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            userLog.setIpAddress(formatIp(ip));
            // 返回参数
            if (controllerLog.isSaveResponseData()) {
                Boolean success = jsonResult.getSuccess();
                if (success==true){
                    JsonResult result = new JsonResult();
                    result.setMsg(jsonResult.getMsg());
                    result.setSuccess(jsonResult.getSuccess());
                    result.setCode(jsonResult.getCode());
                    userLog.setResponseData(JSON.toJSONString(result));
                    userLog.setStatus(true);
                }else {
                    userLog.setResponseData(JSON.toJSONString(jsonResult));
                    userLog.setStatus(false);
                    userLog.setErrMessage(StrUtil.isNotBlank(jsonResult.getMsg()) ? jsonResult.getMsg():"");
                }
            }
            String requestURI = ServletUtils.getRequest().getRequestURI();
            if(loginAdmin != null){
                userLog.setAdminUserId(loginAdmin.getId());
                userLog.setUserName(loginAdmin.getUserName());
            }

            if (e != null) {
                userLog.setStatus(false);
                userLog.setErrMessage(StringUtils.substring(e.getMessage(), 0, 500));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            userLog.setMethod(className + "." + methodName + "()");

//            // 设置请求方式
//            userLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, userLog);
            if(StringUtils.isNotBlank(jsonResult.getLog())){
             userLog.setTitle(jsonResult.getLog());
            }
            // 保存数据库
            operlogService.save(userLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * @param log     日志
     * @param userLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, Operlog userLog) throws Exception {
        // 设置action动作
//        OperLog.setType(log.businessType().getValue());
        userLog.setOperateType(log.operateType());
        // 设置标题EnumUtil.getAlias(log.operateType())
        userLog.setTitle(log.title());
        userLog.setResourceName(log.resourceName());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, userLog);
        }
    }

    /**
     * 获取请求的参数，放到log中
     * @param userLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, Operlog userLog) throws Exception {
        String requestMethod = userLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            userLog.setRequestParam(StringUtils.substring(params, 0, 2000));
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            userLog.setRequestParam(StringUtils.substring(paramsMap.toString(), 0, 2000));
        }
    }

    private String formatIp(String ip){
        String formatIp = "";
        for (String s : ip.split("\\.")) {
            String format = String.format("%0" + 3 + "d", Integer.parseInt(s));
            formatIp+=format+".";
        }
        return formatIp;
    }
}
