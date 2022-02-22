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
 * 
 * </p>
 *
 * @author Innocence
 * @since 2021-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("CHINESEJOURNAL.PERIOD_INFO")
@ApiModel(value = "PeriodInfo对象", description = "")
public class PeriodInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String gch;

    private String name;

    @TableField("SMALL_CLASS")
    private String smallClass;

    @TableField("FIRST_LETTER")
    private String firstLetter;

    @TableField("COLLECT_ABBR")
    private String collectAbbr;

    private Integer lngnewrangeid;

    @TableField("POST_CODE")
    private String postCode;

    @TableField("OPR_ID")
    private BigInteger oprId;


}
