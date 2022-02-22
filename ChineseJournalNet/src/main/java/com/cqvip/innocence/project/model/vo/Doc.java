package com.cqvip.innocence.project.model.vo;

import com.cqvip.innocence.project.model.entity.solr.AbstractDoc;
import com.cqvip.innocence.project.model.entity.solr.TitleInfo;
import com.finemi.support.model.entity.BaseEntity;
import com.finemi.support.model.entity.BusinessEntity;
import lombok.Data;
import org.json.JSONObject;

/**
 * @Author eternal
 * @Date 2021/12/22
 * @Version 1.0
 */
@Data
public class Doc extends BaseEntity {

 private Integer kf_len;

 private JSONObject ndo_content;

 private Integer zkbycount;

// private String id;
}