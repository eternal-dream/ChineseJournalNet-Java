package com.cqvip.innocence.common.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;

/**
 * @Author eternal
 * @Date 2021/12/6
 * @Version 1.0
 */
public enum OperateType {
 @EnumAlias("其他")
 OTHER("其他"),

 @EnumAlias("新增")
 INSERT("新增"),

 @EnumAlias("修改")
 UPDATE("修改"),

 @EnumAlias("新增或修改")
 SAVE_OR_UPDATE("新增或修改"),

 @EnumAlias("删除")
 DELETE("删除"),

 @EnumAlias("查询")
 SEARCH("查询"),

 @EnumAlias("登录")
 LOGIN("登录"),

 @EnumAlias("下载")
 DOWNLOAD("下载");

 private String name;
 private String value;


 OperateType(String value) {
  this.value = value;
 }

 public static String getName(String value) {
  for (OperateType c : OperateType.values()) {
   if (c.getValue().equals(value)) {
    return c.name();
   }
  }
  return null;
 }

 public String getName(){
  for (OperateType c : OperateType.values()) {
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