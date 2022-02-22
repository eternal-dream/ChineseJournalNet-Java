package com.cqvip.innocence.project.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigInteger;
import java.util.Date;

/**
 * @Author eternal
 * @Date 2021/12/15
 * @Version 1.0
 */
public class UserIpInfoVO {

 @TableField("ID")
 protected Long id;

 @TableField(value = "CREATE_TIME")
 protected Date createTime;

 @TableField(value = "MODIFY_TIME")
 protected Date modifyTime;

 @TableField(value = "DELETED")
 protected Integer deleted;

 @ApiModelProperty(value = "IP段 起始IP")
 @TableField("START_IP")
 private String startIp;

 @ApiModelProperty(value = "IP段 终止IP")
 @TableField("FINISH_IP")
 private String finishIp;

 @ApiModelProperty(value = "单位")
 private String unit;

 @ApiModelProperty(value = "备注")
 private String remark;

 @ApiModelProperty(value = "操作人id")
 @TableField("OPERATOR_ID")
 private BigInteger operatorId;

 @TableField("USER_ID")
 private String userId;

 private String userName;

 private String userUnit;
}