package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.common.enums.VisitType;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户日志表
 * </p>
 *
 * @author Innocence
 * @since 2022-01-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.USER_LOG")
@ApiModel(value = "UserLog对象", description = "用户日志表")
public class UserLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "板块名称")
    private String plate;

    @ApiModelProperty(value = "用户id")
    @TableField("USER_ID")
    private Long userId;

    @ApiModelProperty(value = "用户ip段")
    @TableField("IP_RANGE")
    private String ipRange;

    @ApiModelProperty(value = "访问类型(浏览/下载)")
    @TableField("VISIT_TYPE")
    private VisitType visitType;

    @ApiModelProperty(value = "文献名称")
    private String title;

    @ApiModelProperty(value = "文献分类号")
    @TableField("CLASS_NUMBER")
    private String classNumber;

    @ApiModelProperty(value = "刊名")
    @TableField("JOURNAL_NAME")
    private String journalName;

    @ApiModelProperty(value = "日志内容")
    private String content;

    @ApiModelProperty(value = "IP地址")
    private String ip;

}
