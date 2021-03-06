package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户组表
 * </p>
 *
 * @author Innocence
 * @since 2021-12-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.USER_GROUP")
@ApiModel(value = "Group对象", description = "用户组表")
public class UserGroup extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String name;

    @TableField("OPERATOR_ID")
    private BigInteger operatorId;

    @TableField(exist = false)
    private List<DownloadRules> downloadRules;
}
