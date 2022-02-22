package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.dto.UserDatabaseDTO;
import com.cqvip.innocence.project.model.entity.UserDatabase;
import com.cqvip.innocence.project.mapper.UserDatabaseMapper;
import com.cqvip.innocence.project.model.enums.PaymentType;
import com.cqvip.innocence.project.model.enums.UserType;
import com.cqvip.innocence.project.model.vo.UserDatabaseVO;
import com.cqvip.innocence.project.service.UserDatabaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户-数据库关联表(包库信息) 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
@Service
@Transactional
public class UserDatabaseServiceImpl extends ServiceImpl<UserDatabaseMapper, UserDatabase> implements UserDatabaseService {

    @Override
    public void batchAddUserDatabase(UserDatabaseDTO userDatabaseDTO) {
        List<Long> userIds = userDatabaseDTO.getUserIds();
        List<UserDatabase> userDatabase = userDatabaseDTO.getUserDatabase();
        List<UserDatabase> newUserDatabases = new ArrayList<>();
        userDatabase.forEach(item -> {
            userIds.forEach(userId -> item.setUserId(userId));
            newUserDatabases.add(item);
        });
        saveBatch(newUserDatabases);
    }

    @Override
    public Page<UserDatabaseVO> searchUserDatabase(boolean filterByIP, String userName, String unit, String linkMan, String email, UserType userType, Boolean status, PaymentType paymentType, Long userGroupId,String userRemark, String ip, String ipUnit, String ipRemark, Date effectStartTime1, Date effectStartTime2, Date effectEndTime1, Date effectEndTime2, Boolean expired, Boolean effective, Long databaseId, Integer inceptionYear, Integer terminationYear, String remark, Page pageParams) {        return baseMapper.searchUserDatabase(filterByIP, userName,
                unit, linkMan, email, userType, status, paymentType, userGroupId, userRemark, ip, ipUnit, ipRemark,
                effectStartTime1, effectStartTime2, effectEndTime1, effectEndTime2, expired, effective,
                databaseId, inceptionYear, terminationYear, remark, pageParams);
    }

}
