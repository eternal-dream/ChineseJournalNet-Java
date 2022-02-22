package com.cqvip.innocence.project.service.impl;

import com.cqvip.innocence.project.model.entity.StatClass;
import com.cqvip.innocence.project.mapper.StatClassMapper;
import com.cqvip.innocence.project.service.StatClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分类限制专用表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
@Service
public class StatClassServiceImpl extends ServiceImpl<StatClassMapper, StatClass> implements StatClassService {

 @Override
 public List<StatClass> statByName() {
  return baseMapper.statByName();
 }
}
