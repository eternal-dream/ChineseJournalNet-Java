package com.cqvip.innocence.project.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author eternal
 * @Date 2021/12/30
 * @Version 1.0
 */
@Data
public class FacetItem {

    private String key;

    private Integer value;

    private String label;

    private List<FacetItem> children;
}