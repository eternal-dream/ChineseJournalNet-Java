package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.StatClass;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分类限制专用表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
public interface StatClassService extends IService<StatClass> {

 /**
  * 根据名称合并class_number
  * @return
  */
 List<StatClass> statByName();

}
