package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("CHINESEJOURNAL.RESOURCE")
@ApiModel(value="Resource对象", description="资源实体类")
public class Resource extends Model implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    /** 路由地址 */
    private String menuUrl;

    /** 组件路径 */
    private String component;

    /** 是否为外链（true是 false否） */
    @TableField("is_frame")
    private Boolean isFrame;

    private String name;

    /** 权限字符串 */
    private String permission;

    private String sort;

    private Long parentId;

    private String menuIcon;

    /** 是否缓存（true缓存 false否不缓存） */
    @TableField("is_cache")
    private Boolean isCache;

    /** 类型（M目录 C菜单 F按钮） */
    private String menuType;
    /**
     * 是否隐藏
     */
    @TableField("is_hidden")
    private Boolean isHidden;

}
