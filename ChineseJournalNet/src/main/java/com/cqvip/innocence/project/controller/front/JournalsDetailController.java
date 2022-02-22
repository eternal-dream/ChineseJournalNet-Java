package com.cqvip.innocence.project.controller.front;

import com.cqvip.innocence.common.annotation.VisitLog;
import com.cqvip.innocence.common.enums.VisitType;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.SearchParam;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.service.JournalsDetailService;
import com.cqvip.innocence.project.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 期刊详情
 *
 * @author 01
 * @date 2021-12-28 13:36
 */
@Slf4j
@RestController
@RequestMapping("/${base-url.front}/journalsDetail")
public class JournalsDetailController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SearchService searchService;
    @Autowired
    JournalsDetailService journalsDetailService;

    @PostMapping("search")
    @VisitLog(content = "查看详情", plate = "期刊发表作品", visitType = VisitType.View)
    public JsonResult search(@RequestBody SearchParam searchParam, HttpServletRequest request) {
        UserInfo currentUser = (UserInfo) redisTemplate.opsForValue().get(request.getSession().getId());

        try {
            return searchService.search(searchParam, currentUser.getId());
        } catch (Exception e) {
            return JsonResult.Get("pageBean", new HashMap<>());
        }
    }

    @GetMapping("/getSummary")
    @VisitLog(content = "查看详情", plate = "期刊收录汇总", visitType = VisitType.View)
    public JsonResult getSummary(String index, String query,
                                 @RequestParam(required = false, defaultValue = "0") Integer start,
                                 @RequestParam(required = false, defaultValue = "1") Integer rows,
                                 @RequestParam(required = false, defaultValue = "ndo_content") String fl,
                                 @RequestParam(required = false, defaultValue = "json") String wt) {
        Map map = journalsDetailService.getSummary(index, query, start, rows, fl, wt);
        return JsonResult.Get().put("data", map);
    }

    /**
     * 获取期刊详情
     *
     * @param index 索引
     * @param query 检索式
     * @param start
     * @param rows
     * @param fl
     * @param wt
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/12/28 15:08
     */
    @GetMapping("/getJournalsDetail")
    @VisitLog(content = "查看详情", plate = "期刊详情", visitType = VisitType.View)
    public JsonResult getJournalsDetail(String index, String query,
                                        @RequestParam(required = false, defaultValue = "0") Integer start,
                                        @RequestParam(required = false, defaultValue = "1") Integer rows,
                                        @RequestParam(required = false, defaultValue = "ndo_content") String fl,
                                        @RequestParam(required = false, defaultValue = "json") String wt) {
        Map map = journalsDetailService.getJournalsDetail(index, query, start, rows, fl, wt);
        return JsonResult.Get().put("data", map);
    }

}
