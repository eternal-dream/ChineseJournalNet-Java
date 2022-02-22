package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ShieldInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 屏蔽数据表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2022-02-10
 */
public interface ShieldInfoService extends IService<ShieldInfo> {
    /**
     * 文献详情验证
     *
     * @param lngid
     * @return {@link boolean}  true 被屏蔽了，false 没有屏蔽
     * @throws
     * @author 01
     * @date 2022/2/14 14:37
     */
    boolean checkArticleDetail(String lngid);

    /**
     * 期刊详情验证
     *
     * @param gch5
     * @return {@link boolean}  true 被屏蔽了，false 没有屏蔽
     * @throws
     * @author 01
     * @date 2022/2/14 14:37
     */
    boolean checkJournalDetail(String gch5);
}
