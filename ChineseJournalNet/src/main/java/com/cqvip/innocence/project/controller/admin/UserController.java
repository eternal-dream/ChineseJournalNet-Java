package com.cqvip.innocence.project.controller.admin;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.common.util.encryption.Md5Util;
import com.cqvip.innocence.common.util.encryption.SM2Util;
import com.cqvip.innocence.common.util.enums.EnumUtil;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.model.enums.*;
import com.cqvip.innocence.project.model.vo.UserInfoVO;
import com.cqvip.innocence.project.service.DownloadRulesService;
import com.cqvip.innocence.project.service.UserGroupService;
import com.cqvip.innocence.project.service.UserInfoService;
import com.cqvip.innocence.project.service.UserIpInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author eternal
 * @Date 2021/12/10
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.manager}/user")
public class UserController extends AbstractController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserIpInfoService userIpInfoService;

    @Autowired
    private DownloadRulesService downloadRulesService;

    @GetMapping("getUserInfoPage")
    @Log(title = "????????????????????????",operateType = OperateType.SEARCH,resourceName = "????????????")
    public JsonResult getUserInfoPage(boolean filterByIP, String userName, String unit,
                                      String linkMan, String email, UserType userType,
                                      Boolean status, PaymentType paymentType, Long userGroupId,
                                      String remark,String ip, String ipUnit, String ipRemark) {
        String ipNumber = "";
        try{
            ipNumber = formatIpNumber(ip);
        }catch (Exception e){
            return JsonResult.Get(false,"??????????????????IP??????");
        }
        Page<UserInfoVO> userInfoPage = userInfoService.getUserInfoPage(filterByIP, userName, unit, linkMan, email, userType, status, paymentType, userGroupId, remark, ipNumber, ipUnit, ipRemark, getPageParams());
        userInfoPage.getRecords().forEach(userInfo -> {
            userInfo.setPassword(null);
        });
        return JsonResult.Get("userInfoPage", userInfoPage);
    }

    @PostMapping("addOrModify")
    @Log(title = "??????/??????????????????",operateType = OperateType.SAVE_OR_UPDATE,resourceName = "????????????")
    public JsonResult addOrModify(@RequestBody UserInfo userInfo) {
        JsonResult checkUserName = checkUserName(userInfo.getUserName(), userInfo.getId());
        if (!checkUserName.getSuccess()) {
            return checkUserName;
        }

        if (StrUtil.isNotBlank(userInfo.getPassword())) {
            SM2Util sm2Util = new SM2Util();
            String decryptPassword = sm2Util.decrypt(userInfo.getPassword(), SM2Util.PRIAVTEKEY);
            userInfo.setPassword(sm2Util.encrypt(decryptPassword, SM2Util.PUBLICKEY));
        }
        userInfo.insertOrUpdate();
        JsonResult result = userInfoService.addOrModify(userInfo);
        return result;
    }

    @PostMapping("deleteUserInfo")
    @Log(title = "??????????????????",operateType = OperateType.DELETE,resourceName = "????????????")
    public JsonResult deleteUserInfo(@RequestBody Long[] ids) {
        LambdaQueryWrapper<UserIpInfo> ipInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ipInfoLambdaQueryWrapper.in(UserIpInfo::getUserId, ids);
        userIpInfoService.remove(ipInfoLambdaQueryWrapper);
        LambdaQueryWrapper<DownloadRules> downloadRulesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadRulesLambdaQueryWrapper.in(DownloadRules::getUserId,ids);
        downloadRulesService.remove(downloadRulesLambdaQueryWrapper);
        userInfoService.removeByIds(Arrays.asList(ids));
        return JsonResult.Get();
    }

    /**
     * ????????????????????????
     *
     * @param userName                ?????????
     * @param id???????????????????????????????????????id??????????????????
     * @return
     */
    @GetMapping("checkUserName")
    public JsonResult checkUserName(String userName, Long id) {
        if (StringUtils.isBlank(userName)) {
            return JsonResult.Get(false, "?????????????????????");
        }
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserName, userName)
                .ne(id != null, BaseModel::getId, id);
        UserInfo one = userInfoService.getOne(wrapper);
        if (one == null) {
            return JsonResult.Get();
        } else {
            return JsonResult.Get(false, "?????????????????????!");
        }
    }

    @GetMapping("getAllUser")
    public JsonResult getAllUser() {
        return JsonResult.Get("users", userInfoService.list());
    }

    @PostMapping("export")
    @Log(title = "??????????????????Excel",operateType = OperateType.DOWNLOAD,resourceName = "????????????")
    public void export(boolean filterByIP, String userName, String unit,
                       String linkMan, String email, UserType userType,
                       Boolean status, PaymentType paymentType, Long userGroupId,String remark,
                       String ip, String ipUnit, String ipRemark, HttpServletResponse response) throws UnsupportedEncodingException {
        Page<UserInfoVO> userInfoPage = userInfoService.getUserInfoPage(filterByIP, userName, unit, linkMan, email, userType, status, paymentType, userGroupId, remark, ip, ipUnit, ipRemark, new Page(1, 1000));
        userInfoPage.getRecords().forEach(userInfo -> {
            userInfo.setPassword(null);
        });
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.setOnlyAlias(true);
        writer.addHeaderAlias("userName", "?????????");
        writer.addHeaderAlias("unit", "??????");
        writer.addHeaderAlias("linkMan", "?????????");
        writer.addHeaderAlias("userGroupName", "?????????");
        writer.addHeaderAlias("userType", "????????????");
        writer.addHeaderAlias("status", "????????????");
        writer.addHeaderAlias("paymentType", "????????????");
        writer.addHeaderAlias("remark", "??????");
        List<Map> exportContent = new ArrayList<>();
        userInfoPage.getRecords().forEach(item -> {
            JSONObject userInfo = JSONObject.parseObject(JSON.toJSONString(item));
            if (item.getUserType() != null) {
                userInfo.put("userType", EnumUtil.getAlias(item.getUserType()));
            }
            if (item.getPaymentType() != null) {
                userInfo.put("paymentType", EnumUtil.getAlias(item.getPaymentType()));
            }
            if (item.getStatus()) {
                userInfo.put("status", "??????");
            } else {
                userInfo.put("status", "??????");
            }
            exportContent.add(userInfo);
        });
        writer.write(exportContent);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String fileName = URLEncoder.encode("????????????(" + LocalDate.now().toString() + ").xls", "UTF-8");
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

    @GetMapping("/downloadTemplate")
    @ApiOperation("????????????????????????")
    @Log(title = "??????????????????????????????",operateType = OperateType.DOWNLOAD)
    public void downloadTemplate(HttpServletResponse resp, HttpServletRequest request) throws Exception {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("static/template/userImportTemplate.xls");

        ServletOutputStream out= null;
        ExcelWriter writer = null;
        try {
            ExcelReader reader = ExcelUtil.getReader(stream);
            writer = reader.getWriter();
            resp.setContentType("application/vnd.ms-excel;charset=utf-8");
            resp.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            String s = new String("??????????????????????????????".getBytes("UTF-8"), "ISO8859-1");
            resp.setHeader("Content-disposition", "attachment; filename=" + s +".xls");
            out=resp.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null) {
                writer.close();
            }
            if (out != null) {
                IoUtil.close(out);
            }
            if (stream != null) {
                stream.close();
            }
        }
    }

    @PostMapping("import")
    @Log(title = "????????????????????????",operateType = OperateType.INSERT,resourceName = "????????????")
    public JsonResult importUser(MultipartFile file)  {
        try {
            return userInfoService.importUser(file);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.Get(false,"???????????????????????????????????????!");
        }
    }

}
