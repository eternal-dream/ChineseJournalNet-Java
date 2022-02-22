package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @ClassName Admin
 * @Description
 * @Author Innocence
 * @Date 2020/7/11
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("CHINESEJOURNAL.ADMIN_USER")
@ApiModel(value="Admin对象", description="")
public class Admin extends BaseModel {

    private static final long serialVersionUID=1L;

    @TableField("USER_NAME")
    private String userName;

    @TableField("PASSWORD")
    private String password;

    @TableField("REAL_NAME")
    private String realName;

    @TableField("IP_LIMIT")
    private String ipLimit;

    @TableField(value = "EFFECT_BEGIN_DATE",fill = FieldFill.INSERT_UPDATE)
    private Date effectBeginDate;

    @TableField(value = "EFFECT_END_DATE",fill = FieldFill.INSERT_UPDATE)
    private Date effectEndDate;

    @TableField("remark")
    private String remark;

    @TableField(exist = false)
    private String roleIds;

}
