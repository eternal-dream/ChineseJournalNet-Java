package com.cqvip.innocence.project.controller.front;

import com.cqvip.innocence.common.annotation.VisitLog;
import com.cqvip.innocence.common.enums.VisitType;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.SearchParam;
import com.cqvip.innocence.project.service.MediaService;
import io.swagger.annotations.ApiOperation;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @ClassName MediaFacetsController
 * @Description 期刊
 * @Author Innocence
 * @Date 2021/12/28 10:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/media/")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @VisitLog(content = "获取期刊聚类和导航", plate = "期刊导航", visitType = VisitType.View)
    @PostMapping("getMediaFacet")
    @ApiOperation("根据检索条件获取期刊的聚类数据")
    public JsonResult getFacetByQuery(@RequestBody SearchParam searchParam) {
        return mediaService.getMediaFacet(searchParam);
    }

    @VisitLog(content = "获取期刊分页", plate = "期刊检索", visitType = VisitType.View)
    @PostMapping("getMediaPage")
    @ApiOperation("根据检索条件获取期刊分页")
    public JsonResult getMediaPageByQuery(@RequestBody SearchParam searchParam) throws IOException, SolrServerException {
        return mediaService.getMediaPage(searchParam);
    }

}
