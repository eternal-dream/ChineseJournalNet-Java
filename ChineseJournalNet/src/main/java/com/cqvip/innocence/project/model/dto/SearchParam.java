package com.cqvip.innocence.project.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.swing.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author eternal
 * @Date 2021/12/9
 * @Version 1.0
 */
@Data
public class SearchParam implements Serializable {
    /**
     * 检索条件数组
     */
    private List<SearchModel> searchModels;

    /**
     * 检索表达式
     */
    private String searchExpression;

    /**
     * 当前页
     */
    private Integer pageNum;
    /**
     * pageSize
     */
    private Integer pageSize;
    /**
     * 排序
     */
    private String sortField;

    /**
     * 排序是否正序
     */
    private boolean isAsc;

    /**
     * 是否使用检索式检索（true 为使用检索式，false使用构造API）
     */
    private Boolean searchByExpression;

    /**
     * 二次聚类过滤条件(必填)
     */
    private String fq;

    /**
     * 中英文扩展
     */
    private boolean englishExtension;

    /**
     * 同义词扩展
     */
    private boolean synonymExtension;

    /**
     * 年份限制
     */
    private Integer beginYear;

    private Integer endYear;

    /**
     * 数据更新时间限制
     */
    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-M-d")
    private Date pubDate;

    private List<String> journalRange;

    private List<String> subjects;

    private Set<String> highlightValues;

    /**
     * 包库规则
     */
    private String includeRule;

}

