package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.dto.router.Router;
import com.cqvip.innocence.project.model.dto.router.Routers;
import com.cqvip.innocence.project.model.entity.Resource;
import com.cqvip.innocence.project.model.entity.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Innocence
 * @since 2020-07-13
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 给前端组装路由对象
     * @author Innocence
     * @date 2020/11/23
     * @param resources
     * @return java.util.List<com.cqvip.innocence.project.model.dto.router.Routers>
     */
    List<Router> processRouter(List<Resource> resources);

    List<Resource> getRoleResources(Role role);
}
