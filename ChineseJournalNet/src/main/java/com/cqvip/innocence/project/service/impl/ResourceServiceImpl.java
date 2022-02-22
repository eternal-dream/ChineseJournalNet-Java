package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.mapper.ResourceMapper;
import com.cqvip.innocence.project.model.dto.router.Meta;
import com.cqvip.innocence.project.model.dto.router.Router;
import com.cqvip.innocence.project.model.dto.router.Routers;
import com.cqvip.innocence.project.model.entity.Resource;
import com.cqvip.innocence.project.model.entity.Role;
import com.cqvip.innocence.project.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 *
 * </p>
 *
 * @author Innocence
 * @since 2020-07-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Override
    public List<Router> processRouter(List<Resource> resources) {
        //去重+排序
        List<Resource> resourceList = resources.stream()
         .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(r -> r.getId()))), ArrayList::new))
         .stream().sorted(Comparator.comparing(Resource::getSort))
         .collect(Collectors.toList());

        List<Router> routers = new ArrayList<>();
        //定义一个最外层的容器
        Router out = new Router();
        out.setComponent("Layout");
        out.setPath("/");
        defineRouter(out, resourceList);
        out.getChildren().forEach(item->{//将父菜单路由修改为第一个子菜单路由
            item.setPath(item.getChildren().get(0).getPath());
        });
        return out.getChildren();
    }

    @Override
    public List<Resource> getRoleResources(Role role) {
        return baseMapper.getRoleResources(role);
    }


    private void defineRouter(Router parent, List<Resource> resources) {
        Long id = parent.getId();
        List<Resource> subResources = resources.stream().filter(r -> {
            return (id == null && r.getParentId() == null) || (id != null && id.equals(r.getParentId()));
        }).collect(Collectors.toList());
        List<Router> children = new ArrayList<>();
        subResources.forEach(resource -> {
            Router router = new Router();
            //组装固定meta和其他固定信息
            Meta meta = new Meta();
            meta.setIcon(resource.getMenuIcon());
            meta.setNoCache(false);
            meta.setTitle(resource.getName());
            router.setMeta(meta);
            router.setIcon(resource.getMenuIcon());
            router.setTitle(resource.getName());
            router.setHidden(resource.getIsHidden());
            router.setId(resource.getId());
            router.setMenuType(resource.getMenuType());
            router.setParentId(resource.getParentId());
            router.setPermission(resource.getPermission());
            router.setAlwaysShow(false);
            router.setSort(resource.getSort());
            String s = resource.getMenuUrl().substring(resource.getMenuUrl().lastIndexOf("/") + 1);
            String name = s.substring(0, 1).toUpperCase() + s.substring(1);
            router.setName(name);

            if (StrUtil.isBlank(resource.getComponent())) {
                router.setComponent(null);
            } else {
                router.setComponent(resource.getComponent());
            }
            router.setPath(StringUtils.isNotBlank(resource.getMenuUrl()) ? resource.getMenuUrl() : "");
            if ("C".equals(resource.getMenuType())) {
                defineRouter(router, resources);
            } else if ("M".equals(resource.getMenuType())) {
                //递归获取此菜单下的子菜单
                defineRouter(router, resources);
            }
            children.add(router);

        });
        parent.setChildren(children);

    }
}
