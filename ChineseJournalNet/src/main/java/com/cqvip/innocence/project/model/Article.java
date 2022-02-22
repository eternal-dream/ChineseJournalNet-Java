package com.cqvip.innocence.project.model;

import com.finemi.support.model.annotation.CreateTime;
import com.finemi.support.model.entity.BusinessEntity;
import com.finemi.support.solr.annotation.SolrField;
import lombok.Data;

import java.util.Date;

/**
 * 文献
 */
@Data
public class Article extends BusinessEntity {
	//重写 方便存入solr
	@CreateTime
	@SolrField
	private Date createTime;
	//文献类型  qk
	@SolrField
	private String articleType;
	//资讯分类
	private String articleCatlog;
	//采集来源
	@SolrField
	private String source;
	//文章来源
	private String articleSource;
	//源网站二级分类
	private String secCatlog;

	private String title;

	private String author;

	@SolrField
	private String pubYear;
}