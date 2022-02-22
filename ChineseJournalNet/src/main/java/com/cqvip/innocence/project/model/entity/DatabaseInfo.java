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
 * 数据库表
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.DATABASE_INFO")
@ApiModel(value = "DatabaseInfo对象", description = "数据库表")
public class DatabaseInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据库名称")
    private String name;

    @TableField("OPERATOR_ID")
    private Long operatorId;


}
