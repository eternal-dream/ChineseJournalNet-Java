package com.cqvip.innocence.project.model.dto;

import com.cqvip.innocence.common.enums.LogicOperator;
import com.cqvip.innocence.common.enums.SearchField;
import lombok.Data;

/**
 * @Author eternal
 * @Date 2021/12/9
 * @Version 1.0
 */
@Data
public class SearchModel {

    //检索字段
    private SearchField searchField;

    //检索内容
    private String value;

    /**
     * 是否精确检索
     */
    private boolean exact;

    //逻辑运算符 AND
    private LogicOperator logicOperator;

    private float weight = 1.0f;

    private String displayName;

}
