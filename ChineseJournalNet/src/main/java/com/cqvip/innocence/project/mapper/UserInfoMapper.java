package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqvip.innocence.project.model.enums.PaymentType;
import com.cqvip.innocence.project.model.enums.UserType;
import com.cqvip.innocence.project.model.enums.VerificationType;
import com.cqvip.innocence.project.model.vo.UserInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户基础信息表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-12-10
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

 Page<UserInfoVO> getUserInfoPage(@Param("filterByIP") boolean filterByIP,@Param("userName") String userName,@Param("unit") String unit,
                                  @Param("linkMan") String linkMan,@Param("email") String email,@Param("userType") UserType userType,
                                  @Param("status") Boolean status,@Param("paymentType") PaymentType paymentType,@Param("userGroupId") Long userGroupId,
                                  @Param("remark") String remark,@Param("ip") String ip,@Param("ipUnit") String ipUnit,@Param("ipRemark") String ipRemark, Page page);

 UserInfo getUserByIp(@Param("ip") String ip, @Param("verificationType") VerificationType verificationType);

 List<UserInfo> getUserByIpRange(@Param("startIp") String startIp, @Param("finishIp") String finishIp);
}
