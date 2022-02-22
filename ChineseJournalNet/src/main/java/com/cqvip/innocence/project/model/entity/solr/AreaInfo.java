package com.cqvip.innocence.project.model.entity.solr;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName AreaInfo
 * @Description Solr地域信息实体
 * @Author Innocence
 * @Date 2021/12/28 9:14
 * @Version 1.0
 */
@Data
public class AreaInfo implements Serializable {

    private static final long serialVersionUID = -8891194937878953159L;

    private String areaid;

    private String areaname;

    private List<AreaInfo> children;
}
