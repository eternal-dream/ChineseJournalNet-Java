package com.cqvip.innocence.project.service.impl;

import com.cqvip.innocence.project.model.entity.Operlog;
import com.cqvip.innocence.project.mapper.OperlogMapper;
import com.cqvip.innocence.project.service.OperlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员操作日志 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-06
 */
@Service
public class OperlogServiceImpl extends ServiceImpl<OperlogMapper, Operlog> implements OperlogService {

}
