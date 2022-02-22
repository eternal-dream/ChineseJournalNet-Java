package com.cqvip.innocence.project.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 01
 * @date 2021-12-30 11:18
 */
@Data
public class JournalSummaryVO {
    private String muiInfo;

    private List<Summary> summarys;
}
