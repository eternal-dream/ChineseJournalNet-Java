package com.cqvip.innocence.common.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;

/**
 * @Author eternal
 * @Date 2021/12/9
 * @Version 1.0
 */
public enum SearchField {
    @EnumAlias("文献主键id")
    LNGID("lngid"),
    @EnumAlias("任意字段")//任意字段必须使用此编码后的字符串，原因未知
    U("@paper#sb=1#sp=2"),

    @EnumAlias("题名或关键词")
// M("title_c;title_e;keyword_c;keyword_e"),
    M("@paper#f=title_c;title_e;keyword_c;keyword_e#sp=2"),
    @EnumAlias("题名")
// T("title_c;title_e"),
    T("@paper#f=title_c;title_e#sp=2"),

    @EnumAlias("关键词")
// K("keyword_c;keyword_e"),
    K("@paper#f=keyword_c;keyword_e#sp=2"),

    @EnumAlias("文摘")
// R("remark_c;remark_e"),
    R("@field#f=remark_c#sp=2"),

    @EnumAlias("作者")
// A("showwriter;writer_text;author_e"),
    A("@paper#f=writer_text;author_e;showwriter#sp=2"),

    @EnumAlias("作者ID")
    AID("writerids_s"),

    @EnumAlias("第一作者")
    F("firstwriter"),

    @EnumAlias("机构")
    S("@paper#f=organ_text;organ_e;showorgan#sp=2"),

    @EnumAlias("机构ID")
    SID("organids_s"),

    @EnumAlias("刊名")
// J("media_c;media_e"),
    J("@paper#f=media_c;media_e;wname_s#sp=2"),

    @EnumAlias("分类号")
    C("class"),

    @EnumAlias("分类号ID")
    CID("classids_s"),

    @EnumAlias("年份")
    Y("years"),

    @EnumAlias("期刊收录")
    RN("range"),

// @EnumAlias("参考文献")
// Y("strrefsearch"),
//
// @EnumAlias("作者简介")
// Z("任意字段"),

    @EnumAlias("基金资助")
    I("imburse"),

    @EnumAlias("栏目信息")
    L("muinfo"),

    // 以下为期刊检索字段
    @EnumAlias("刊名")
    MN("@paper#f=media;oldname#sp=2"),

    @EnumAlias("期刊id")
    MID("mediaid"),

    @EnumAlias("issn")
    ISSN("issn"),

    @EnumAlias("cn")
    CN("cn"),

    @EnumAlias("主办单位")
    PB("publisher"),

    @EnumAlias("主编")
    ED("editor"),

    @EnumAlias("邮发代号")
    PS("yfdh"),

    @EnumAlias("中图分类号")
    CL("classid"),

    @EnumAlias("核心刊标识")
    LNG("lngrangeid"),

    @EnumAlias("地区")
    AREA("areaidlevel"),

    @EnumAlias("主题")
    SUB("subjectids_s"),

    @EnumAlias("期刊首字母")
    FN("firstname"),

    @EnumAlias("馆藏号")
    GCH("gch"),

    @EnumAlias("馆藏号")
    GCH5("gch5");

    private String name;
    private String value;


    SearchField(String value) {
        this.value = value;
    }

    public static String getName(String value) {
        for (SearchField c : SearchField.values()) {
            if (c.getValue().equals(value)) {
                return c.name();
            }
        }
        return null;
    }

    public String getName() {
        for (SearchField c : SearchField.values()) {
            if (c.getValue().equals(value)) {
                return c.name();
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
