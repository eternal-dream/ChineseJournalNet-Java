package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Innocence
 * @since 2020-07-13
 */
@Data
@TableName("CHINESEJOURNAL.ADMIN_ROLE")
@ApiModel(value="AdminRole对象", description="用户角色对应关系")
public class AdminRole implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.INPUT)
    private Long adminId;

    private Long roleId;


}
