package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.UserDatabase;
import com.cqvip.innocence.project.model.enums.PaymentType;
import com.cqvip.innocence.project.model.enums.UserType;
import com.cqvip.innocence.project.model.vo.UserDatabaseVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 用户-数据库关联表(包库信息) Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
public interface UserDatabaseMapper extends BaseMapper<UserDatabase> {

 //分页查询
 Page<UserDatabaseVO> searchUserDatabase(@Param("filterByIP") boolean filterByIP, @Param("userName") String userName, @Param("unit") String unit,
                                         @Param("linkMan") String linkMan, @Param("email") String email, @Param("userType") UserType userType,
                                         @Param("status") Boolean status, @Param("paymentType") PaymentType paymentType, @Param("userGroupId") Long userGroupId,
                                         @Param("userRemark") String userRemark, @Param("ip") String ip, @Param("ipUnit") String ipUnit, @Param("ipRemark") String ipRemark,
                                         @Param("effectStartTime1") Date effectStartTime1,@Param("effectStartTime2") Date effectStartTime2,
                                         @Param("effectEndTime1") Date effectEndTime1,@Param("effectEndTime2") Date effectEndTime2,
                                         @Param("expired") Boolean expired,@Param("effective") Boolean effective,@Param("databaseId") Long databaseId,
                                         @Param("inceptionYear")Integer inceptionYear,@Param("terminationYear") Integer terminationYear,
                                         @Param("remark") String remark, Page pageParams);

/**
 * 获取某用户包库情况
 *
 * @param uid
 * @return {@link UserDatabaseVO}
 * @throws
 * @author 01
 * @date 2022/2/9 15:47
 */
UserDatabaseVO getUserDatabaseByUserId(Long uid);
}
