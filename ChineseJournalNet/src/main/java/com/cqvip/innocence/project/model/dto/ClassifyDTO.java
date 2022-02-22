package com.cqvip.innocence.project.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 01
 * @date 2022-01-06 17:10
 */
@Data
public class ClassifyDTO {
    private Integer id;

    private String strClass;

    private String strName;

    private String strLevel;

    private Integer parentId;

    private String strParentClass;

    private Integer children;

    private List<ClassifyDTO> childrenList;
}
