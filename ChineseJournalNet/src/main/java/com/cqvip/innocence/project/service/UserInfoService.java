package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.enums.PaymentType;
import com.cqvip.innocence.project.model.enums.UserType;
import com.cqvip.innocence.project.model.enums.VerificationType;
import com.cqvip.innocence.project.model.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 用户基础信息表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-12-10
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户信息页面/用户IP绑定页面查询方法
     *
     * @param filterByIP  是否通过ip过滤
     * @param userName    用户名
     * @param unit        用户单位
     * @param linkMan     联系人
     * @param email       邮箱
     * @param userType    用户类型
     * @param status      用户是否有效
     * @param paymentType 用户付费类型
     * @param userGroupId 用户组id
     * @param ip          ip
     * @param ipUnit      ip绑定单位
     * @param ipRemark    ip备注
     * @param page        分页参数
     * @return
     */
    Page<UserInfoVO> getUserInfoPage(boolean filterByIP, String userName, String unit, String linkMan, String email, UserType userType, Boolean status, PaymentType paymentType, Long userGroupId, String ip, String remark, String ipUnit, String ipRemark, Page page);

    JsonResult addOrModify(UserInfo userInfo);

    UserInfo getUserByIp(String ip);

    UserInfo getUserByIp(String ip, VerificationType verificationType);

    List<UserInfo> getUserByIpRange(String startIp, String finishIp);

    JsonResult importUser(MultipartFile file);
}
