package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.project.model.entity.DownloadRules;
import com.cqvip.innocence.project.model.entity.UserGroup;
import com.cqvip.innocence.project.mapper.UserGroupMapper;
import com.cqvip.innocence.project.service.DownloadRulesService;
import com.cqvip.innocence.project.service.UserGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户组表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-13
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements UserGroupService {

 @Autowired
 private DownloadRulesService downloadRulesService;
 @Override
 public void addOrModify(UserGroup group) {
  List<DownloadRules> downloadRules = group.getDownloadRules();
  group.insertOrUpdate();
  downloadRules.forEach(item->{
   item.setGroupId(group.getId());
  });
  LambdaQueryWrapper<DownloadRules> wrapper = new LambdaQueryWrapper<>();
  wrapper.eq(DownloadRules::getGroupId,group.getId());
  downloadRulesService.remove(wrapper);
  downloadRulesService.saveBatch(downloadRules);
 }
}
