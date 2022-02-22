package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.util.Date;

import com.cqvip.innocence.project.model.enums.DownloadRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文献下载限制规则表
 * </p>
 *
 * @author Innocence
 * @since 2021-12-14
 */
@Data
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.DOWNLOAD_RULES")
@ApiModel(value = "DownloadRules对象", description = "文献下载限制规则表")
public class DownloadRules {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    protected Long id;

    @TableField("USER_ID")
    private Long userId;

    @TableField("GROUP_ID")
    private Long groupId;

    @ApiModelProperty(value = "规则")
    private DownloadRule rule;

    @ApiModelProperty(value = "最大下载量")
    private Integer limit;

    @TableField("OPERATOR_ID")
    private Long operatorId;

    @TableField(value = "CREATE_TIME",fill = FieldFill.INSERT)
    protected Date createTime;

    @TableField(value = "MODIFY_TIME",fill = FieldFill.INSERT_UPDATE)
    protected Date modifyTime;

}
