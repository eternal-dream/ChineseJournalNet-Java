package com.cqvip.innocence.common.util.downpaper;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author 01
 * @date 2022-01-10 14:24
 */
@Slf4j
public class DownPaperApiUtil {

    public static final boolean DOWN_PAPER_API_LOCK = (Boolean) YmlReadUtil.newInstance().getValue("downpaper.yml", "downPaperApiLock");

    private static String appId = YmlReadUtil.newInstance().getValueToString("downpaper.yml", "downpaper.appId");

    private static String appSecret = YmlReadUtil.newInstance().getValueToString("downpaper.yml", "downpaper.appSecret");

    private static String clientId = YmlReadUtil.newInstance().getValueToString("downpaper.yml", "downpaper.clientId");

    private static Integer isTianYuanData = YmlReadUtil.newInstance().getValueToInteger("downpaper.yml", "downpaper.isTianYuanData");

    private static Integer limitRuleId = YmlReadUtil.newInstance().getValueToInteger("downpaper.yml", "downpaper.limitRuleId");

    private static String limitUserType = YmlReadUtil.newInstance().getValueToString("downpaper.yml", "downpaper.limitUserType");

    private static String token = null;

    /**
     * 类型
     */
    private static String fileType = "qk";
    /**
     * 语言
     */
    private static String languageType = "cn";

    /**
     * 创建token
     *
     * @return
     */
    private static void createAuthToken() {
        Map<String, Object> param = new HashMap<>(16);
        param.put("appId", appId);
        param.put("appSecret", appSecret);
        param.put("IsUseRefreshToken", 0);

        String paramJson = JSON.toJSONString(param);
        String result = HttpRequest
                .post("https://downpaperapi.cqvip.com/api/Auth/CreateAuthToken")
                .body(paramJson).execute().body();
        if (StringUtils.isNotBlank(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            Map<String, Object> retValue = (Map<String, Object>) jsonObject.get("retValue");
            try{
                token = retValue.get("token").toString();
            }catch(NullPointerException ex){
                log.info("token 生成失败");
            }

        }
    }

    private static String getUrl(String params) {
        String result = HttpRequest
                .post("https://downpaperapi.cqvip.com/api/DownPaperCreateDownLink/CreateDownLoadUrl")
                .header("Authorization", "Bearer " + token).body(params).execute().body();
        if (StringUtils.isNotBlank(result)) {
            if (StringUtils.contains(result, "401")) {
                createAuthToken();
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            Map<String, Object> retValue = (Map<String, Object>) jsonObject.get("retValue");
            Integer overLimit = (Integer) retValue.get("overLimit");
            if (overLimit == 1) {
                createAuthToken();
                return getUrl(params);
            }

            return retValue.get("url").toString();
        }
        return null;
    }

    /**
     * 获取URL
     *
     * @param lngid
     * @param fileName 文件名
     * @param userId   用户id
     * @param year     出版年限
     * @return {@link java.lang.String}
     * @author 01
     * @date 2022/1/10 15:18
     */
    public static String createDownLoadUrl(String lngid, String userId, String fileName, String year) {
        if (StringUtils.isAnyBlank(lngid, fileName, year)) {
            return null;
        }
        if (StringUtils.isBlank(token)) {
            createAuthToken();
        }
        Map<String, Object> param = new HashMap<>(16);
        param.put("IsTianYuanData", isTianYuanData);
        param.put("LimitRuleId", limitRuleId);
        param.put("ClientId", clientId);
        param.put("FileName", fileName);
        param.put("Years", year);
        param.put("LngId", lngid);
        param.put("UserId", userId);
        param.put("limitUserType", limitUserType);
        param.put("UserIp", "");
        param.put("FileType", fileType);
        param.put("languageType", languageType);
        String paramJson = JSON.toJSONString(param);
        return getUrl(paramJson);
    }

}
