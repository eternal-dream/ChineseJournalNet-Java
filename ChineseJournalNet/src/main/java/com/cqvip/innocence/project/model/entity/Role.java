package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Innocence
 * @since 2020-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("CHINESEJOURNAL.ROLE")
@ApiModel(value="Role对象", description="角色信息")
public class Role extends BaseModel implements Serializable {

    private static final long serialVersionUID=1L;

    private String description;

    private String name;

    @TableField(exist = false)
    private String resourceStr;

    @TableField(exist = false)
    private List<Long> resourceIds;
}
