package com.cqvip.innocence.project.controller.front;

import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.service.ClassifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 01
 * @date 2022-01-06 16:50
 */
@Slf4j
@RestController
@RequestMapping("/${base-url.front}/classify")
public class ClassifyController {

    @Autowired
    ClassifyService classifyService;

    /**
     * 获取父分类
     * @return
     */
    @GetMapping("/getSuperClassifyData")
    public JsonResult getSuperClassifyData(){
        return JsonResult.Get().put("data", classifyService.getSuperClassifyData());
    }
    /**
     * 通过父id获取子级分类
     *
     * @param parentId
     * @return {@link JsonResult}
     * @throws
     * @author 01
     * @date 2022/1/7 10:35
     */
    @GetMapping("/getSubClassifyDataByParentId")
    public JsonResult getSubClassifyDataByParentId(Integer parentId){
        return JsonResult.Get().put("data", classifyService.getSubClassifyDataByParentId(parentId));
    }

    /**
     * 获取分类树状结构数据
     *
     * @return {@link JsonResult}
     * @author 01
     * @date 2022/1/7 9:38
     */
    @GetMapping("/getClassifyTreeData")
    public JsonResult getClassifyTreeData() {
        return JsonResult.Get().put("data", classifyService.getClassifyTreeData());
    }

}
