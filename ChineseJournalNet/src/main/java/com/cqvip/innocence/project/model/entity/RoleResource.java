package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体字段不需要公共字段时，可以不继承BaseModel,自己实现Serializable即可，常见于一些中间关系表
 * 由于设置了全局ID自动填充，所以当数据库内没有对应的"id"字段时，要手动设置实体类的某个字段为ID， 如下：
 * @TableId(type = IdType.INPUT)
 * @author Innocence
 * @since 2020-07-13
 */
@Data
@TableName("CHINESEJOURNAL.ROLE_RESOURCE")
@ApiModel(value="RoleResource对象", description="角色与资源对应关系")
public class RoleResource implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.INPUT)
    private Long roleId;

    private Long resourceId;

}
