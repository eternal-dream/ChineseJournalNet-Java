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
 * 屏蔽数据表
 * </p>
 *
 * @author Innocence
 * @since 2022-02-10
 */
@Data
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.SHIELD_INFO")
@ApiModel(value = "ShieldInfo对象", description = "屏蔽数据表")
public class ShieldInfo {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    protected Long id;

    @ApiModelProperty(value = "文章ID")
    private String lngid;

    @ApiModelProperty(value = "期刊馆藏号")
    private String gch5;


}
