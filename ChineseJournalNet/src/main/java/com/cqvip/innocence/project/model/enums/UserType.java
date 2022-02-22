package com.cqvip.innocence.project.model.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;
import com.cqvip.innocence.common.enums.OperateType;

/**
 * @Author eternal
 * @Date 2021/12/10
 * @Version 1.0
 */
public enum UserType {

 @EnumAlias("试用户")
 Trial("试用户"),

 @EnumAlias("正式用户")
 Formal("正式用户");

 private String name;
 private String value;


 UserType(String value) {
  this.value = value;
 }

 public static String getName(String value) {
  for (UserType c : UserType.values()) {
   if (c.getValue().equals(value)) {
    return c.name();
   }
  }
  return null;
 }

 public String getName(){
  for (UserType c : UserType.values()) {
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