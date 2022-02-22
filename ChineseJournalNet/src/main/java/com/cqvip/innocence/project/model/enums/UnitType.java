package com.cqvip.innocence.project.model.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;
import com.cqvip.innocence.common.enums.LogicOperator;
import com.cqvip.innocence.common.enums.OperateType;

/**
 * @Author eternal
 * @Date 2021/12/10
 * @Version 1.0
 */
public enum UnitType {

 @EnumAlias("其它")
 Other("其它"),

 @EnumAlias("学校")
 School("学校"),

 @EnumAlias("信息情报")
 Information_intelligence ("信息情报"),

 @EnumAlias("研究机构")
 Research_institutions("研究机构"),

 @EnumAlias("公司")
 Company("公司"),

 @EnumAlias("企业")
 Enterprise("企业"),

 @EnumAlias("政府机关")
 Government_office("政府机关"),

 @EnumAlias("中介机构")
 Agency("中介机构"),

 @EnumAlias("学生")
 Student("学生");

 private String name;
 private String value;


 UnitType(String value) {
  this.value = value;
 }

 public static String getName(String value) {
  for (UnitType c : UnitType.values()) {
   if (c.getValue().equals(value)) {
    return c.name();
   }
  }
  return null;
 }

 public String getName(){
  for (UnitType c : UnitType.values()) {
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