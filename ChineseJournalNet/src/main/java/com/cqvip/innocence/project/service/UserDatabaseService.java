package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.dto.UserDatabaseDTO;
import com.cqvip.innocence.project.model.entity.UserDatabase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.enums.PaymentType;
import com.cqvip.innocence.project.model.enums.UserType;
import com.cqvip.innocence.project.model.vo.UserDatabaseVO;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户-数据库关联表(包库信息) 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
public interface UserDatabaseService extends IService<UserDatabase> {

 void batchAddUserDatabase(UserDatabaseDTO userDatabaseDTO);

 Page<UserDatabaseVO> searchUserDatabase(boolean filterByIP, String userName, String unit, String linkMan, String email, UserType userType, Boolean status, PaymentType paymentType, Long userGroupId, String userRemark, String ip, String ipUnit, String ipRemark, Date effectStartTime1, Date effectStartTime2, Date effectEndTime1, Date effectEndTime2, Boolean expired, Boolean effective, Long databaseId, Integer inceptionYear, Integer terminationYear, String remark, Page pageParams);

}
