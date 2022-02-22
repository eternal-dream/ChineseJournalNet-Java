package com.cqvip.innocence.common.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;

/**
 * @Author eternal
 * @Date 2021/12/9
 * @Version 1.0
 */
public enum LogicOperator {
    @EnumAlias("与")
    AND("与"),

    @EnumAlias("或")
    OR("或"),

    @EnumAlias("非")
    NOT("非");

    private String name;
    private String value;


    LogicOperator(String value) {
        this.value = value;
    }

    public static String getName(String value) {
        for (LogicOperator c : LogicOperator.values()) {
            if (c.getValue().equals(value)) {
                return c.name();
            }
        }
        return null;
    }

    public String getName() {
        for (LogicOperator c : LogicOperator.values()) {
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
