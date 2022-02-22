package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.UserIpInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户ip限制表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-12-13
 */
public interface UserIpInfoMapper extends BaseMapper<UserIpInfo> {

    UserIpInfo selectByIp(@Param("ip") String ip,@Param("userId") Long userId);
}
