package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户-数据库关联表(包库信息)
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.USER_DATABASE")
@ApiModel(value = "UserDatabase对象", description = "用户-数据库关联表(包库信息)")
public class UserDatabase extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private Long userId;

    @TableField("DATABASE_ID")
    private Long databaseId;

    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "有效期起始时间")
    @TableField(value = "BEGIN_TIME",fill = FieldFill.INSERT_UPDATE)
    private Date beginTime;

    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "有效期结束时间")
    @TableField(value = "END_TIME",fill = FieldFill.INSERT_UPDATE)
    private Date endTime;

    @ApiModelProperty(value = "分类限制(,隔开)")
    private String classify;

    @ApiModelProperty(value = "数据年限(起始)")
    @TableField(value = "INCEPTION_YEAR",fill = FieldFill.INSERT_UPDATE)
    private Integer inceptionYear;

    @ApiModelProperty(value = "数据年限(终止)")
    @TableField(value = "TERMINATION_YEAR",fill = FieldFill.INSERT_UPDATE)
    private Integer terminationYear;

    @ApiModelProperty(value = "是否有效")
    private Boolean effective;

    private String remark;


}
