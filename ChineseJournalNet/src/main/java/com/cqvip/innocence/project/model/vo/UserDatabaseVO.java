package com.cqvip.innocence.project.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author eternal
 * @Date 2021/12/17
 * @Version 1.0
 */
@Data
public class UserDatabaseVO {

 @TableField("ID")
 protected Long id;

 @TableField(value = "CREATE_TIME")
 protected Date createTime;

 @TableField(value = "MODIFY_TIME")
 protected Date modifyTime;

 @TableField(value = "DELETED")
 protected Integer deleted;

 @TableField("USER_ID")
 private Long userId;

 @TableField("DATABASE_ID")
 private Long databaseId;

 @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
 @ApiModelProperty(value = "有效期起始时间")
 @TableField("BEGIN_TIME")
 private Date beginTime;

 @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
 @ApiModelProperty(value = "有效期结束时间")
 @TableField("END_TIME")
 private Date endTime;

 @ApiModelProperty(value = "分类限制(;隔开)")
 private String classify;

 @ApiModelProperty(value = "数据年限(起始)")
 @TableField("INCEPTION_YEAR")
 private Integer inceptionYear;

 @ApiModelProperty(value = "数据年限(终止)")
 @TableField("TERMINATION_YEAR")
 private Integer terminationYear;

 @ApiModelProperty(value = "是否有效")
 private Boolean effective;

 private String remark;

 private String databaseName;

 private String paymentType;

 @TableField("USER_NAME")
 private String userName;

 private String unit;
}