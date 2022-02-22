package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.entity.AdminRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Innocence
 * @since 2020-07-13
 */
public interface AdminRoleService extends IService<AdminRole> {

    /**
     * 多数据源情况下的批量插入失效，重写
     * @author Innocence
     * @date 2020/11/24
     * @param list
     * @return boolean
     */
    void saveBatch(List<AdminRole> list);

}
