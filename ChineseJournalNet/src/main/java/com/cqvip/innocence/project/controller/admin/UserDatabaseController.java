package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.UserDatabaseDTO;
import com.cqvip.innocence.project.model.entity.UserDatabase;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.model.enums.PaymentType;
import com.cqvip.innocence.project.model.enums.UserType;
import com.cqvip.innocence.project.model.vo.UserDatabaseVO;
import com.cqvip.innocence.project.service.UserDatabaseService;
import com.cqvip.innocence.project.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户-数据库关联表(包库信息) 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-12-16
 */
@RestController
@RequestMapping("/${base-url.manager}/userDatabase")
public class UserDatabaseController extends AbstractController {

    @Autowired
    private UserDatabaseService userDatabaseService;

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("searchUserDatabase")
    @Log(title = "查看用户包库信息",operateType = OperateType.SEARCH,resourceName = "用户包库信息")
    public JsonResult searchUserDatabase(boolean filterByIP, String userName, String unit,
                                         String linkMan, String email, UserType userType,
                                         Boolean status, PaymentType paymentType, Long userGroupId,String userRemark,
                                         String ip, String ipUnit, String ipRemark, Date effectStartTime1,
                                         Date effectStartTime2, Date effectEndTime1, Date effectEndTime2,
                                         Boolean expired, Boolean effective, Long databaseId, Integer inceptionYear,
                                         Integer terminationYear, String remark) {
        String ipNumber = "";
        try{
            ipNumber = formatIpNumber(ip);
        }catch (Exception e){
            return JsonResult.Get(false,"请输入正确的IP地址");
        }
        Page<UserDatabaseVO> page = userDatabaseService.searchUserDatabase(filterByIP, userName,
                unit, linkMan, email, userType, status, paymentType, userGroupId, userRemark, ipNumber, ipUnit, ipRemark,
                effectStartTime1, effectStartTime2, effectEndTime1, effectEndTime2, expired, effective,
                databaseId, inceptionYear, terminationYear, remark, getPageParams());
        return JsonResult.Get("userDatabasePage", page);
    }

    @PostMapping("batchAddUserDatabase")
    @Log(title = "新增包库信息",operateType = OperateType.INSERT,resourceName = "用户包库信息")
    public JsonResult batchAddUserDatabase(@RequestBody UserDatabaseDTO userDatabaseDTO) {
        if (CollectionUtil.isEmpty(userDatabaseDTO.getUserIds()) || CollectionUtil.isEmpty(userDatabaseDTO.getUserDatabase())) {
            return JsonResult.Get(false, "参数错误!");
        }
        userDatabaseService.batchAddUserDatabase(userDatabaseDTO);
        return JsonResult.Get();
    }

    @GetMapping("getUnsetUsers")
    @ApiOperation("设置用户包库信息时，获取未设置的用户")
    public JsonResult getUnsetUsers() {
        List<UserDatabase> list = userDatabaseService.list();
        List<Long> userIds = new ArrayList<>();
        if (null != list && !list.isEmpty()) {
            list.forEach(item -> userIds.add(item.getUserId()));
        }
        LambdaQueryWrapper<UserInfo> userQuery = new LambdaQueryWrapper<>();
        userQuery.notIn(UserInfo::getId, userIds);
        return JsonResult.Get("users",userInfoService.list(userQuery));
    }

    @PostMapping("modify")
    @Log(title = "修改用户包库信息",operateType = OperateType.UPDATE,resourceName = "用户包库信息")
    public JsonResult addOrModify(UserDatabase userDatabase) {
        userDatabase.updateById();
        return JsonResult.Get();
    }

    @PostMapping("deleteUserDatabase")
    @Log(title = "删除用户包库信息",operateType = OperateType.DELETE,resourceName = "用户包库信息")
    public JsonResult deleteUserDatabase(@RequestBody Long[] ids) {
        userDatabaseService.removeByIds(Arrays.asList(ids));
        return JsonResult.Get();
    }

    @PostMapping("export")
    @Log(title = "导出用户包库信息",operateType = OperateType.DOWNLOAD, resourceName = "用户包库信息")
    public void export(boolean filterByIP, String userName, String unit,
                       String linkMan, String email, UserType userType,
                       boolean status, PaymentType paymentType, Long userGroupId,String userRemark,
                       String ip, String ipUnit, String ipRemark, Date effectStartTime1,
                       Date effectStartTime2, Date effectEndTime1, Date effectEndTime2,
                       Boolean expired, Boolean effective, Long databaseId, Integer inceptionYear,
                       Integer terminationYear, String remark, HttpServletResponse response) throws UnsupportedEncodingException {
        //限制最大导出条数1000条
        Page<UserDatabaseVO> content = userDatabaseService.searchUserDatabase(filterByIP, userName,
                unit, linkMan, email, userType, status, paymentType, userGroupId, userRemark, ip, ipUnit, ipRemark,
                effectStartTime1, effectStartTime2, effectEndTime1, effectEndTime2, expired, effective,
                databaseId, inceptionYear, terminationYear, remark, new Page(1, 1000));
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("userName", "用户名");
        writer.addHeaderAlias("unit", "单位");
//        writer.addHeaderAlias("databaseName", "数据库");
        writer.addHeaderAlias("classify", "分类限制");
        writer.addHeaderAlias("inceptionYear", "起始数据年限");
        writer.addHeaderAlias("terminationYear", "终止数据年限");
        writer.addHeaderAlias("effective", "状态");
        writer.addHeaderAlias("beginTime", "开始时间");
        writer.addHeaderAlias("endTime", "结束时间");
        writer.addHeaderAlias("remark", "备注");
        writer.setOnlyAlias(true);
        writer.write(content.getRecords());
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String fileName = URLEncoder.encode("用户包库信息(" + LocalDate.now().toString() + ").xls", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        IoUtil.close(out);
    }

}

