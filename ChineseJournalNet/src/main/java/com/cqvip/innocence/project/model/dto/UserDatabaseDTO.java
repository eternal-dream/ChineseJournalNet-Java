package com.cqvip.innocence.project.model.dto;

import com.cqvip.innocence.project.model.entity.UserDatabase;
import lombok.Data;

import java.util.List;

/**
 * @Author eternal
 * @Date 2021/12/16
 * @Version 1.0
 */
@Data
public class UserDatabaseDTO {

    //批量新增时传入用户id数组
    private List<Long> userIds;

    //批量新增时传入包库信息
    private List<UserDatabase> userDatabase;
}
