package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.model.entity.UserIpInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.vo.UserInfoVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户ip限制表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-13
 */
@Transactional
public interface UserIpInfoService extends IService<UserIpInfo> {

 /**
  * 新增或修改用户IP信息
  * @param userInfo
  */
 JsonResult addOrModify(UserInfo userInfo);

 UserIpInfo selectByIp(String ip,Long userId);
}
