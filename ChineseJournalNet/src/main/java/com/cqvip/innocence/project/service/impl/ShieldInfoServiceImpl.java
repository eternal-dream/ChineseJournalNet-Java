package com.cqvip.innocence.project.service.impl;

import com.cqvip.innocence.project.model.entity.ShieldInfo;
import com.cqvip.innocence.project.mapper.ShieldInfoMapper;
import com.cqvip.innocence.project.service.ShieldInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 屏蔽数据表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2022-02-10
 */
@Service
public class ShieldInfoServiceImpl extends ServiceImpl<ShieldInfoMapper, ShieldInfo> implements ShieldInfoService {

    @Override
    public boolean checkArticleDetail(String lngid) {
        int count = baseMapper.checkArticleDetail(lngid);
        return count>0;
    }

    @Override
    public boolean checkJournalDetail(String gch5) {
        int count = baseMapper.checkJournalDetail(gch5);
        return count>0;
    }
}
