package com.cqvip.innocence.project.model.vo;

import lombok.Data;

import java.util.List;


/**
 * @ClassName AdminRoleView
 * @Description 前后端交互管理员和角色数据实体
 * @Author Innocence
 * @Date 2020/8/5
 * @Version 1.0
 */
@Data
public class AdminRoleVO {

    private Long adminId;

    private Long roleId;

    private String userName;

    private String realName;

    private String passWord;

    private String roleName;

    private String remark;

    private Object roleIds;//角色id列表

    private List<String> roleNames;

    private String type;
}
