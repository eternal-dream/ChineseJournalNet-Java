package com.cqvip.innocence.project.model.entity.solr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MediaInfo
 * @Description Solr期刊数据
 * @Author Innocence
 * @Date 2021/12/28 9:20
 * @Version 1.0
 */
@Data
public class MediaInfo implements Serializable {

    private static final long serialVersionUID = -7021968726720765560L;

    @ApiModelProperty("期刊id")
    private Integer mediaid;

    @ApiModelProperty("期刊名")
    private String media;

    @ApiModelProperty("期刊简介")
    private String introduction;

    @ApiModelProperty("中刊发文量")
    private Integer zkfwcount;

    @ApiModelProperty("中刊被引量")
    private Integer zkbycount;

    private String issn;

    private String tel;

    @ApiModelProperty("主管单位")
    private String organizers;

    @ApiModelProperty("出版社（主办单位）")
    private String publisher;

    @ApiModelProperty("普通刊、核心刊标识")
    private String lngrangeid;

    @ApiModelProperty("单价")
    private Double unitprice;

    @ApiModelProperty("主编")
    private String editor;

    private String gch;

    private String gch5;

    @ApiModelProperty("馆藏号-年-期信息")
    private String gchyearsnum;

    @ApiModelProperty("馆藏号年期信息的格式化，map以年为key,期次为value，馆藏号对应当前期刊馆藏号")
    private List<Map<String,List<String>>> yearsNumFacet;

    @ApiModelProperty("学科分类id")
    private String classid;

    @ApiModelProperty("出版周期（年刊，季刊，月刊，半月刊等）")
    private String periodtype;

    private String myclassid_g1;

    private String areaids_g1;

    @ApiModelProperty("期刊名首字母大写")
    private String firstname;

    @ApiModelProperty("邮编")
    private String postcode;

    @ApiModelProperty("")
    private String subjectids_s;

    private String vipnow;

    private String price;

    private String cn;

    private String email;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("地域等级")
    private String areaidlevel;

    @ApiModelProperty("邮发代号")
    private String yfdh;

}
