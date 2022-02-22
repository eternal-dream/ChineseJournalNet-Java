package com.cqvip.innocence.project.model.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;
import com.cqvip.innocence.common.enums.OperateType;

/**
 * @Author eternal
 * @Date 2021/12/10
 * @Version 1.0
 */
public enum VerificationType {

 @EnumAlias("IP验证")
 Ip("IP验证"),

 @EnumAlias("用户名/密码验证")
 UserName_password("用户名/密码验证"),

 @EnumAlias("用户名/密码验证/IP验证")
 All("用户名/密码验证/IP验证");


 private String name;
 private String value;


 VerificationType(String value) {
  this.value = value;
 }

 public static String getName(String value) {
  for (VerificationType c : VerificationType.values()) {
   if (c.getValue().equals(value)) {
    return c.name();
   }
  }
  return null;
 }

 public String getName(){
  for (VerificationType c : VerificationType.values()) {
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