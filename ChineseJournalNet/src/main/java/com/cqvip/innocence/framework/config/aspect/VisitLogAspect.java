package com.cqvip.innocence.framework.config.aspect;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cqvip.innocence.common.annotation.VisitLog;
import com.cqvip.innocence.common.enums.VisitType;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.common.util.html.ServletUtils;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.Operlog;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.model.entity.UserIpInfo;
import com.cqvip.innocence.project.model.entity.UserLog;
import com.cqvip.innocence.project.service.DetailService;
import com.cqvip.innocence.project.service.OperlogService;
import com.cqvip.innocence.project.service.UserIpInfoService;
import com.cqvip.innocence.project.service.UserLogService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;


/**
 * 用户日志注解处理
 * @author Innocence
 * @date 2021/1/5
 */
@Aspect
@Component
public class VisitLogAspect extends AbstractAspect<VisitLog> {
    private static final Logger log = LoggerFactory.getLogger(VisitLogAspect.class);

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserIpInfoService userIpInfoService;

    @Autowired
    private DetailService detailService;

    /**
     * 配置织入点
     * @author Innocence
     * @date 2020/12/30
     */
    @Pointcut("@annotation(com.cqvip.innocence.common.annotation.VisitLog)")
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
            VisitLog visitLog = super.getAnnotationTag(joinPoint, VisitLog.class);
            if (visitLog == null) {
                return;
            }
            UserLog userLog = new UserLog();
            HttpServletRequest request = ServletUtils.getRequest();
            HttpSession session = request.getSession(false);
            // 获取当前的用户
            UserInfo userInfo = null;
            if (session != null){
                Object userObj = redisTemplate.opsForValue().get(session.getId());
                // 请求的地址
                String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
                userLog.setIp(formatIp(ip));
                if(userObj != null){
                    userInfo = (UserInfo) userObj;
                    userLog.setUserId(userInfo.getId());
                    UserIpInfo userIpInfo = userIpInfoService.selectByIp(ip, userInfo.getId());
                    if(userIpInfo != null){
                        userLog.setIpRange(userIpInfo.getStartIp() + "-" +userIpInfo.getFinishIp());
                    }
                }
            }

            if(VisitType.Download == visitLog.visitType()){//下载，阅读等需记录文章信息
                String lngid = request.getParameter("lngid");
                Map<String, Object> detailInfo = detailService.getDetailInfo("title_info", "lngid:" + lngid.toLowerCase(), 0, 1, "ndo_content", "json");
                userLog.setJournalName(detailInfo.get("media_c") != null ? detailInfo.get("media_c").toString() : "");
                userLog.setTitle(detailInfo.get("title_c") != null ? detailInfo.get("title_c").toString() : detailInfo.get("title_e").toString());
                userLog.setClassNumber(detailInfo.get("specialnum").toString());
            }
            // *========数据库日志=========*//

            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, visitLog, userLog);
            if(jsonResult!=null&&StringUtils.isNotBlank(jsonResult.getLog())){
             userLog.setTitle(jsonResult.getLog());
            }
            // 保存数据库
            userLogService.save(userLog);
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
    public void getControllerMethodDescription(JoinPoint joinPoint, VisitLog log, UserLog userLog) throws Exception {
        // 设置action动作
        userLog.setContent(log.content());
        userLog.setPlate(log.plate());
        userLog.setVisitType(log.visitType());
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
