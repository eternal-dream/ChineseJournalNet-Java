package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.model.enums.PaymentType;
import com.cqvip.innocence.project.model.enums.UnitType;
import com.cqvip.innocence.project.model.enums.UserType;
import com.cqvip.innocence.project.model.enums.VerificationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 用户基础信息表
 * </p>
 *
 * @author Innocence
 * @since 2021-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.USER_INFO")
@ApiModel(value = "UserInfo对象", description = "用户基础信息表")
public class UserInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "账户状态（true:有效，false:无效）")
    private boolean status;

    @ApiModelProperty(value = "登录验证方式")
    @TableField("VERIFICATION_TYPE")
    private VerificationType verificationType;

    @ApiModelProperty(value = "联系人")
    @TableField("LINK_MAN")
    private String linkMan;

    @ApiModelProperty(value = "所在单位")
    private String unit;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "电子邮件")
    private String email;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "所在区域")
    private String area;

    @ApiModelProperty(value = "所在省份")
    private String province;

    @ApiModelProperty(value = "单位类型")
    @TableField("UNIT_TYPE")
    private UnitType unitType;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "用户类型")
    @TableField("USER_TYPE")
    private UserType userType;

    @ApiModelProperty(value = "付费方式")
    @TableField("PAYMENT_TYPE")
    private PaymentType paymentType;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "邮政编码")
    @TableField("POST_CODE")
    private String postCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField("OPERATOR_ID")
    private Long operatorId;

    @TableField(value = "USER_GROUP_ID",fill = FieldFill.INSERT_UPDATE)
    private Long userGroupId;

    @TableField(exist = false)
    private List<UserIpInfo> userIpInfos;

    @TableField(exist = false)
    private List<DownloadRules> downloadRules;
}
