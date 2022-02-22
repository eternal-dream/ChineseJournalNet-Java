package com.cqvip.innocence.project.model.entity.solr;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SubjectInfo
 * @Description solr主题聚类信息
 * @Author Innocence
 * @Date 2021/12/28 10:52
 * @Version 1.0
 */
@Data
public class SubjectInfo implements Serializable {
    private static final long serialVersionUID = 917935769318800681L;

    private Integer subjectid;

    private String subject;

}
