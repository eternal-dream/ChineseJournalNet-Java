package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 管理员操作日志
 * </p>
 *
 * @author Innocence
 * @since 2021-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.OPERLOG")
@ApiModel(value = "Operlog对象", description = "管理员操作日志")
public class Operlog extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "馆员操作类型")
    @TableField("OPERATE_TYPE")
    private OperateType operateType;

    @ApiModelProperty(value = "操作员id")
    @TableField("ADMIN_USER_ID")
    private Long adminUserId;

    @ApiModelProperty(value = "访问IP地址")
    @TableField("IP_ADDRESS")
    private String ipAddress;

    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "请求参数")
    @TableField("REQUEST_PARAM")
    private String requestParam;

    @ApiModelProperty(value = "返回结果")
    @TableField("RESPONSE_DATA")
    private String responseData;

    @ApiModelProperty(value = "操作是否成功")
    private Boolean status;

    @ApiModelProperty(value = "错误信息")
    @TableField("ERR_MESSAGE")
    private String errMessage;

    @ApiModelProperty(value = "操作人姓名")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty(value = "请求方式(post/get)")
    @TableField("REQUEST_METHOD")
    private String requestMethod;

    @ApiModelProperty(value = "功能模块(菜单页面)")
    @TableField("RESOURCE_NAME")
    private String resourceName;
}
