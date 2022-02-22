package com.cqvip.innocence.project.controller;


import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.service.ShieldInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

/**
 * <p>
 * 屏蔽数据表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2022-02-10
 */
@RestController
@RequestMapping("/${base-url.front}/shieldInfo")
public class ShieldInfoController extends AbstractController {
    @Autowired
    ShieldInfoService shieldInfoService;

    @GetMapping("/checkArticleDetail")
    public JsonResult checkArticleDetail(String lngid) {
        boolean shielded = shieldInfoService.checkArticleDetail(lngid);
        return JsonResult.Get().put("shielded",shielded);
    }

    @GetMapping("/checkJournalDetail")
    public JsonResult checkJournalDetail(String gch5) {
        boolean shielded = shieldInfoService.checkJournalDetail(gch5);
        return JsonResult.Get().put("shielded",shielded);
    }
}

