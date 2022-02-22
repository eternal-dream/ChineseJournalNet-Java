package com.cqvip.innocence.project.model.entity.solr;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ClassInfo
 * @Description Solr学科分类实体
 * @Author Innocence
 * @Date 2021/12/28 9:18
 * @Version 1.0
 */
@Data
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = -2656869672677373304L;

    private String classid;

    private String classtypename;

    private List<ClassInfo> children;
}
