package com.cqvip.innocence.project.model.dto;

import com.cqvip.innocence.common.enums.VisitType;
import lombok.Data;

import java.util.List;

/**
 * @Author eternal
 * @Date 2021/12/20
 * @Version 1.0
 */
@Data
public class StatisticsDTO {

 private VisitType visitType;

 private String startTime;
 private String endTime;

 private String startIp;
 private String endIp;
 private List<Long> userIds;
 private Long userGroupId;

 //页面板块
 private String plate;
 //分析对象
 private String type;
 //全部汇总/周期汇总
 private String customType;
 //粒度
 private String grainSize;

 private String journalName;

 private String[] classNumber;
}