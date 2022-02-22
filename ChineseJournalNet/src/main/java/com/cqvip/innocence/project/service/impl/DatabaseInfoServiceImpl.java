package com.cqvip.innocence.project.service.impl;

import com.cqvip.innocence.project.model.entity.DatabaseInfo;
import com.cqvip.innocence.project.mapper.DatabaseInfoMapper;
import com.cqvip.innocence.project.service.DatabaseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据库表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
@Service
public class DatabaseInfoServiceImpl extends ServiceImpl<DatabaseInfoMapper, DatabaseInfo> implements DatabaseInfoService {

}
