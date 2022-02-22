package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.UserGroup;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户组表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-13
 */
public interface UserGroupService extends IService<UserGroup> {

 void addOrModify(UserGroup group);
}
