package com.cqvip.innocence.project.model.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * @author 01
 * @date 2021-12-30 11:29
 */
public class Summary {

    private String lngid;

    private String title;

    private String writer;

    private String beginpage;

    private String muinfo;

    private String endpage;

    private String jumppage;

    private String volumn;

    public String getLngid() {
        return lngid;
    }

    public void setLngid(String lngid) {
        this.lngid = lngid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getBeginpage() {
        return beginpage;
    }

    public void setBeginpage(String beginpage) {
        this.beginpage = beginpage;
    }

    public String getMuinfo() {
        if (StringUtils.isBlank(muinfo)) {
            return "其他";
        }
        return muinfo;
    }

    public void setMuinfo(String muinfo) {
        this.muinfo = muinfo;
    }

    public String getEndpage() {
        return endpage;
    }

    public void setEndpage(String endpage) {
        this.endpage = endpage;
    }

    public String getJumppage() {
        return jumppage;
    }

    public void setJumppage(String jumppage) {
        this.jumppage = jumppage;
    }

    public String getVolumn() {
        return volumn;
    }

    public void setVolumn(String volumn) {
        this.volumn = volumn;
    }
}
