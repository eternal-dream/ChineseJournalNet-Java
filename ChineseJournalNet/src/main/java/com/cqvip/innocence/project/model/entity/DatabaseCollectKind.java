package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Innocence
 * @since 2021-12-28
 */
@Data
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.DATABASE_COLLECT_KIND")
@ApiModel(value = "DatabaseCollectKind对象", description = "")
public class DatabaseCollectKind implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private Long id;

    @TableField("DATABASE_NAME")
    private String databaseName;

    @TableField("ENGLISH_ABBR")
    private String englishAbbr;

    @TableField("NATION_NAME")
    private String nationName;

    @TableField("CLUSTER_SHOW_NAME")
    private String clusterShowName;
}
