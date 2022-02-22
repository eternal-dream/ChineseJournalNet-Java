package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.dto.ClassifyDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 01
 * 分类mapper
 */
public interface ClassifyMapper {
    /**
     * 获取所有非父分类
     * @return
     */
    List<ClassifyDTO> getAllNotSuperClassify();

    /**
     * 获取所有父分类
     * @return
     */
    List<ClassifyDTO> getAllSuperClassify();

    /**
     * 通过父id获取子级分类
     * @param parentId 父id
     * @return
     */
    List<ClassifyDTO> getSubClassifyDataByParentId(@Param("parentId") Integer parentId);
}
