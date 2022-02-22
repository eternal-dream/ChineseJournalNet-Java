package com.cqvip.innocence.common.annotation;

import com.cqvip.innocence.common.enums.VisitType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * @author
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VisitLog {

    /**
     * 日志内容
     * @return
     */
    String content() default "";

    /**
     * 板块名称
     * @return
     */
    String plate() default "其它";

    /**
     * 访问类型
     * @return
     */
    VisitType visitType() default VisitType.View;
}