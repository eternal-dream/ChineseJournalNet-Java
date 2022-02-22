package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户ip限制表
 * </p>
 *
 * @author Innocence
 * @since 2021-12-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.USER_IP_INFO")
@ApiModel(value = "UserIpInfo对象", description = "用户ip限制表")
public class UserIpInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

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
    private Long operatorId;

    @TableField("USER_ID")
    private Long userId;


}
