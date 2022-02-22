package com.cqvip.innocence.project.model.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;
import com.cqvip.innocence.common.enums.OperateType;

/**
 * @Author eternal
 * @Date 2021/12/10
 * @Version 1.0
 */
public enum PaymentType {

 @EnumAlias("包全文")
 Full_text("包全文"),

 @EnumAlias("包题录")
 Inscription("包题录");

 private String name;
 private String value;


 PaymentType(String value) {
  this.value = value;
 }

 public static String getName(String value) {
  for (PaymentType c : PaymentType.values()) {
   if (c.getValue().equals(value)) {
    return c.name();
   }
  }
  return null;
 }

 public String getName(){
  for (PaymentType c : PaymentType.values()) {
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