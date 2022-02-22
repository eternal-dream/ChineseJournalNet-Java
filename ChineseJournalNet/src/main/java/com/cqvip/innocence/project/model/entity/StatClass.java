package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分类限制专用表
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
@Data
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.STAT_CLASS")
@ApiModel(value = "StatClass对象", description = "分类限制专用表")
public class StatClass {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类号")
    @TableField("CLASS_NUMBER")
    private String classNumber;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @TableField("SORT_NUM")
    private Integer sortNum;


}
