package com.cqvip.innocence.project.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.UserIpInfo;
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

import java.util.Date;
import java.util.List;

/**
 * @Author eternal
 * @Date 2021/12/10
 * @Version 1.0
 */
@Data
public class UserInfoVO {

 @TableField("ID")
 private Long id;

 @TableField(value = "CREATE_TIME")
 private Date createTime;

 @TableField(value = "MODIFY_TIME")
 private Date modifyTime;

 @ApiModelProperty(value = "用户名")
 @TableField("USER_NAME")
 private String userName;

 @ApiModelProperty(value = "密码")
 private String password;

 @ApiModelProperty(value = "账户状态（true:有效，false:无效）")
 private Boolean status;

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

 @ApiModelProperty(value = "邮政编码")
 @TableField("POST_CODE")
 private String postCode;

 @ApiModelProperty(value = "传真")
 private String fax;

 @ApiModelProperty(value = "备注")
 private String remark;

 @TableField("OPERATOR_ID")
 private Long operatorId;

 //用户组id
 private Long userGroupId;

 private String userGroupName;

 private List<UserIpInfo> userIpInfos;


}