package com.cqvip.innocence.project.model.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;

/**
 * @Author eternal
 * @Date 2021/12/14
 * @Version 1.0
 */
public enum DownloadRule {

 @EnumAlias("每小时最大下载")
 Per_hour,

 @EnumAlias("每天最大下载")
 Per_day,

 @EnumAlias("每周最大下载")
 Per_week,

 @EnumAlias("每月最大下载")
 Per_month,

 @EnumAlias("每年最大下载")
 Per_year,
}