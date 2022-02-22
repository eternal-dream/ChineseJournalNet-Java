package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.dto.ClassifyDTO;

import java.util.List;

/**
 * 分类Service
 * @author 01
 * @date 2022/1/7 9:07
 */
public interface ClassifyService {
    /**
     * 获取分类的树状结构数据
     * @return {@link List<ClassifyDTO>}
     */
    List<ClassifyDTO> getClassifyTreeData();

    /**
     * 获取父级分类
     * @return
     */
    List<ClassifyDTO> getSuperClassifyData();
    /**
     * 通过父级id获取对应子分类
     *
     * @param parentId
     * @return {@link List< ClassifyDTO>}
     * @throws
     * @author 01
     * @date 2022/1/7 10:39
     */
    List<ClassifyDTO> getSubClassifyDataByParentId(Integer parentId);
}
