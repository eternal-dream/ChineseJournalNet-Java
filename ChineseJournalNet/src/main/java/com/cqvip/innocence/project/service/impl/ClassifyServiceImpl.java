package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cqvip.innocence.project.mapper.ClassifyMapper;
import com.cqvip.innocence.project.model.dto.ClassifyDTO;
import com.cqvip.innocence.project.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 01
 * @date 2022-01-07 9:08
 */
@Service
public class ClassifyServiceImpl implements ClassifyService {

    @Autowired
    ClassifyMapper classifyMapper;

    @Override
    public List<ClassifyDTO> getClassifyTreeData() {
        //非顶级菜单
        List<ClassifyDTO> notSuperMenuList = classifyMapper.getAllNotSuperClassify();
        //顶级菜单
        List<ClassifyDTO> superMenuList = classifyMapper.getAllSuperClassify();

        if (ObjectUtil.isNotNull(notSuperMenuList)) {
            Set<Integer> set = new HashSet<>(notSuperMenuList.size());
            superMenuList.forEach(item -> getChild(item, notSuperMenuList, set));
            return superMenuList;
        }

        return new ArrayList<>();
    }

    @Override
    public List<ClassifyDTO> getSuperClassifyData() {
        return classifyMapper.getAllSuperClassify();
    }

    @Override
    public List<ClassifyDTO> getSubClassifyDataByParentId(Integer parentId) {
        return classifyMapper.getSubClassifyDataByParentId(parentId);
    }

    /**
     * 递归获取当前节点的子节点，并组装
     *
     * @param menuNode     节点
     * @param menuNodeList 子节点列表
     * @param set          已递归的节点id
     * @author 01
     * @date 2021/8/25 16:24
     */
    private void getChild(ClassifyDTO menuNode, List<ClassifyDTO> menuNodeList, Set<Integer> set) {
        List<ClassifyDTO> childList = new ArrayList<>();
        menuNodeList.stream()
                //过滤已经递归了的节点
                .filter(item -> !set.contains(item.getId()))
                //判断是否是父子关系
                .filter(item -> NumberUtil.compare(item.getParentId(), menuNode.getId()) == 0)
                .filter(item -> set.size() <= menuNodeList.size())
                .forEach(item -> {
                    //set保存已经递归了的id
                    set.add(item.getId());
                    //递归
                    getChild(item, menuNodeList, set);
                    //加入子节点集合
                    childList.add(item);
                });
        menuNode.setChildrenList(childList);
    }
}
