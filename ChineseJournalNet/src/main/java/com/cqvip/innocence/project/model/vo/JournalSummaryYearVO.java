package com.cqvip.innocence.project.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 期刊收录汇总年限VO
 * @author 01
 * @date 2021-12-30 9:35
 */
@Data
public class JournalSummaryYearVO {
    private String year;

    private List<String> num;
}
