package com.cqvip.innocence.common.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;

/**
 * 前台请求访问类型枚举
 * @Author eternal
 * @Date 2022/1/7
 * @Version 1.0
 */
public enum VisitType {

    @EnumAlias("浏览")
    View("浏览"),

//    @EnumAlias("阅读")
//    Read("阅读文章"),

    @EnumAlias("下载")
    Download("下载");

    private String name;
    private String value;


    VisitType(String value) {
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