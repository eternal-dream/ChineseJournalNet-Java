package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.common.util.enums.EnumUtil;
import com.cqvip.innocence.project.mapper.DownloadRulesMapper;
import com.cqvip.innocence.project.mapper.UserDatabaseMapper;
import com.cqvip.innocence.project.mapper.UserLogMapper;
import com.cqvip.innocence.project.model.dto.CheckResult;
import com.cqvip.innocence.project.model.dto.StatisticsDTO;
import com.cqvip.innocence.project.model.entity.DownloadRules;
import com.cqvip.innocence.project.model.entity.UserLog;
import com.cqvip.innocence.project.model.enums.PaymentType;
import com.cqvip.innocence.project.model.vo.UserDatabaseVO;
import com.cqvip.innocence.project.service.DetailService;
import com.cqvip.innocence.project.service.UserLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户日志表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2022-01-07
 */
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {
    @Autowired
    DetailService detailService;
    @Autowired
    DownloadRulesMapper downloadRulesMapper;
    @Autowired
    UserDatabaseMapper userDatabaseMapper;

    @Override
    public List<String> getAllPlates() {
        return baseMapper.getAllPlates();
    }

    @Override
    public List<Map> analysis(StatisticsDTO statisticsDTO) {
        return baseMapper.analysis(statisticsDTO);
    }

    /**
     * 比较两个日期Date2比Date1(年月日)多的天数,(只考虑天数不考虑时间)
     * 例如:2017-01-25 23:59:59与 2017-01-26 00:00:00   返回1
     * 2017-01-25 与 2017-01-25   返回0
     * 2017-01-28 与 2017-01-25   返回-3
     */
    public static int differDayQty(Date Date1, Date Date2) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.clear();
        calendar.setTime(Date1);
        int day1 = calendar.get(java.util.Calendar.DAY_OF_YEAR);
        int year1 = calendar.get(java.util.Calendar.YEAR);
        calendar.setTime(Date2);
        int day2 = calendar.get(java.util.Calendar.DAY_OF_YEAR);
        int year2 = calendar.get(java.util.Calendar.YEAR);
        //同一年
        if (year1 == year2) {
            return day2 - day1;
            //Date1<Date2
        } else if (year1 < year2) {
            int days = 0;
            for (int i = year1; i < year2; i++) {
                //闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    days += 366;
                } else {
                    days += 365;
                }
            }
            return days + (day2 - day1);
            //Date1>Date2
        } else {
            int days = 0;
            for (int i = year2; i < year1; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    days += 366;
                } else {
                    days += 365;
                }
            }
            return 0 - days + (day2 - day1);
        }
    }


    private CheckResult checkDatabaseInfo(Long userId, String lngid) {
        Map<String, Object> detailInfo = detailService.getDetailInfo("title_info", "lngid:" + lngid.toLowerCase(), 0, 1, "ndo_content", "json");
        UserDatabaseVO userDatabaseVO = userDatabaseMapper.getUserDatabaseByUserId(userId);
        if (userDatabaseVO == null) {
            CheckResult checkResult = new CheckResult(true, "无相关包库信息！");
            return checkResult;
        }
        //判断是否有效
        if (!userDatabaseVO.getEffective()) {
            CheckResult checkResult = new CheckResult(true, "包库权限失效！");
            return checkResult;
        }
        //判断是不是包全文
        if (!PaymentType.Full_text.toString().equals(userDatabaseVO.getPaymentType())) {
            CheckResult checkResult = new CheckResult(true, "没有下载权限");
            return checkResult;
        }
        //判断包全文是否过期
        Date nowDate = new Date();
        if (userDatabaseVO.getBeginTime() != null && userDatabaseVO.getEndTime() != null) {
            if (differDayQty(userDatabaseVO.getBeginTime(), nowDate) < 0 || differDayQty(userDatabaseVO.getEndTime(), nowDate) > 0) {
                CheckResult checkResult = new CheckResult(true, "包库权限不在有效时间内！");
                return checkResult;
            }
        }

        //判断包全文分类
        if (StringUtils.isNotBlank(userDatabaseVO.getClassify())) {
            String[] classArray = userDatabaseVO.getClassify().split(",");
            String aClass = detailInfo.get("class") != null ? detailInfo.get("class").toString() : null;
            if (StringUtils.isNotBlank(aClass)) {
                List<String> collect = Arrays.stream(classArray).filter(item ->
                        aClass.startsWith(item)).collect(Collectors.toList());
                if (collect.size() == 0) {
                    CheckResult checkResult = new CheckResult(true, "没有该分类号的全文下载权限");
                    return checkResult;
                }
            }
        } else {
            CheckResult checkResult = new CheckResult(true, "没有该分类号的全文下载权限");
            return checkResult;
        }
        //年限
        if (userDatabaseVO.getInceptionYear() != null && userDatabaseVO.getTerminationYear() != null) {
            Integer years = detailInfo.get("years") != null ? Integer.parseInt(detailInfo.get("years").toString()) : null;
            if (years != null) {
                if (!(userDatabaseVO.getInceptionYear() <= years && years <= userDatabaseVO.getTerminationYear())) {
                    CheckResult checkResult = new CheckResult(true, "没有该年限的全文下载权限");
                    return checkResult;
                }
            }
        }
        return new CheckResult(false, "");
    }

    @Override
    public CheckResult downloadLimitCheck(Long userId, String lngid) {

        CheckResult result = checkDatabaseInfo(userId, lngid);
        if (result.getLimited()) {
            return result;
        }

        List<DownloadRules> rules = downloadRulesMapper.getRulesByUserId(userId);
        Map<Boolean, List<DownloadRules>> sumCollect = rules.stream().collect(Collectors.groupingBy(p -> p.getGroupId() == null, Collectors.toList()));
        List<DownloadRules> groupRuleList = sumCollect.get(false);
        List<DownloadRules> userRuleList = sumCollect.get(true);
        /*
         * 判断当前用户所在组和用户自身的下载限制，优先判断个人规则
         */
        if ((CollectionUtils.isNotEmpty(userRuleList) && CollectionUtils.isNotEmpty(groupRuleList))
                || (CollectionUtils.isNotEmpty(userRuleList) && CollectionUtils.isEmpty(groupRuleList))) {
            // 按user算
            for (DownloadRules downloadRules : userRuleList) {
                int count = baseMapper.getDownloadCountInDateScope(userId, downloadRules.getRule().toString());
                if (count >= downloadRules.getLimit()) {
                    CheckResult checkResult = new CheckResult(true, "超过" + EnumUtil.getAlias(downloadRules.getRule()) + "量");
                    return checkResult;
                }
            }
        } else if (CollectionUtils.isEmpty(userRuleList) && CollectionUtils.isNotEmpty(groupRuleList)) {
            // 按group算
            for (DownloadRules downloadRules : groupRuleList) {
                int count = baseMapper.getDownloadCountInDateScope(userId, downloadRules.getRule().toString());
                if (count >= downloadRules.getLimit()) {
                    CheckResult checkResult = new CheckResult(true, "超过" + EnumUtil.getAlias(downloadRules.getRule()) + "量");
                    return checkResult;
                }
            }
        } else if (CollectionUtils.isEmpty(userRuleList) && CollectionUtils.isEmpty(groupRuleList)) {
            // 两种均没有,暂定不限制
            return new CheckResult(false, "");
        }
        return new CheckResult(false, "");
    }

    @Override
    public CheckResult readLimitCheck(Long id, String lngid) {
        return checkDatabaseInfo(id, lngid);
    }

}
