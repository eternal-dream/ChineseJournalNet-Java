package com.cqvip.innocence.project.controller.front;

import com.cqvip.innocence.common.annotation.VisitLog;
import com.cqvip.innocence.common.enums.VisitType;
import com.cqvip.innocence.common.util.downpaper.DownPaperApiUtil;
import com.cqvip.innocence.common.util.file.FileUtil;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;
import com.cqvip.innocence.project.model.dto.CheckResult;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.UserInfo;
import com.cqvip.innocence.project.service.DetailService;
import com.cqvip.innocence.project.service.UserLogService;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文献详情
 *
 * @author 01
 * @date 2022-01-04 15:13
 */
@Slf4j
@RestController
@RequestMapping("/${base-url.front}/detail")
public class DetailController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    DetailService detailService;

    @Autowired
    UserLogService userLogService;

    @GetMapping("/getDetail")
    @VisitLog(content = "查看详情", plate = "文献详情", visitType = VisitType.View)
    public JsonResult getDetail(String index, String query,
                                @RequestParam(required = false, defaultValue = "0") Integer start,
                                @RequestParam(required = false, defaultValue = "1") Integer rows,
                                @RequestParam(required = false, defaultValue = "ndo_content") String fl,
                                @RequestParam(required = false, defaultValue = "json") String wt) {

        Map<String, Object> detailInfo = detailService.getDetailInfo(index, query, start, rows, fl, wt);
        List<Map<String, Object>> refList = new ArrayList<>();
        if (detailInfo != null && !detailInfo.isEmpty()) {
            String zkreferidsReal = (String) detailInfo.get("zkreferids_real");
            if (StringUtils.isNotBlank(zkreferidsReal)) {
                refList = detailService.getRelDetailInfo(index, zkreferidsReal, start, rows);
            }
        }
        return JsonResult.Get()
                .put("data", detailInfo)
                .put("refList", refList);
    }

    /**
     * 获取本地pdf路径规则，通过lngid由hash算法得到pdf路径
     *
     * @param lngid
     * @return
     */
    private String getPartOfHash(String lngid) {
        String n = lngid.toUpperCase() + ".pdf";
        String hashHex = Hashing.murmur3_128().hashString(n, StandardCharsets.UTF_8).toString().toUpperCase();

        String l = StringUtils.substring(hashHex, 0, 2);
        String r = StringUtils.substring(hashHex, 2, 3);

        int numR;
        try {
            //是数字
            numR = Integer.parseInt(r);
            r = String.valueOf(numR % 5);
        } catch (NumberFormatException e) {
            r = r.replace("D", "A")
                    .replace("E", "B")
                    .replace("F", "C");
        }

        return l + r;
    }

    /**
     * 用户阅读限制检查
     *
     * @param request
     * @param lngid
     * @return {@link JsonResult}
     * @throws
     * @author 01
     * @date 2022/2/10 16:38
     */
    @GetMapping("/readLimitCheck")
    public JsonResult readLimitCheck(HttpServletRequest request, String lngid) {
        UserInfo currentUser = (UserInfo) redisTemplate.opsForValue().get(request.getSession().getId());
        if (currentUser == null) {
            CheckResult checkResult = new CheckResult(true, "用户信息失效");
            return JsonResult.Get().put("data", checkResult);
        }
        CheckResult checkResult = userLogService.readLimitCheck(currentUser.getId(), lngid);
        return JsonResult.Get().put("data", checkResult);
    }

    /**
     * 用户下载限制检查
     *
     * @param request
     * @param lngid
     * @return {@link JsonResult}
     * @throws
     * @author 01
     * @date 2022/2/10 16:38
     */
    @GetMapping("/downloadLimitCheck")
    public JsonResult downloadLimitCheck(HttpServletRequest request, String lngid) {
        UserInfo currentUser = (UserInfo) redisTemplate.opsForValue().get(request.getSession().getId());
        if (currentUser == null) {
            CheckResult checkResult = new CheckResult(true, "用户信息失效");
            return JsonResult.Get().put("data", checkResult);
        }
        CheckResult checkResult = userLogService.downloadLimitCheck(currentUser.getId(), lngid);
        return JsonResult.Get().put("data", checkResult);
    }

    /**
     * 路径规则
     */
    private final static Map<String, String> yearsPath = (Map<String, String>) YmlReadUtil.newInstance().getValue("downloadConf.yml", "download.years");
    /**
     * 是否是本机下载
     */
    private final static boolean local = (Boolean) YmlReadUtil.newInstance().getValue("downloadConf.yml", "download.local");

    private String getBasePath(Integer year) {
        if (year < 1989) {
            return yearsPath.get("before1989");
        } else {
            return yearsPath.get(year);
        }
    }

    /**
     * pdf
     *
     * @param lngid
     * @author 01
     * @date 2022/1/10 11:15
     */
    @GetMapping("/downloadPdf")
    @VisitLog(content = "下载", plate = "全文下载", visitType = VisitType.Download)
    public JsonResult downloadPdf(String lngid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        getStreamOfPdf(lngid, request, response);
        return null;
    }

    /**
     * 阅读pdf
     *
     * @param lngid
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/readPdf")
    public void readPdf(String lngid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        getStreamOfPdf(lngid, request, response);
    }

    private void getStreamOfPdf(String lngid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.isNotBlank(lngid)) {
            FileUtil fileUtil = FileUtil.newInstance();
            //全文在本机
            Map<String, Object> detailInfo = detailService.getDetailInfo("title_info", "lngid:" + lngid.toLowerCase(), 0, 1, "ndo_content", "json");
            log.info("======================== 原文在本机？"+local);
            log.info("======================== DownPaperApiUtil.DOWN_PAPER_API_LOCK？"+DownPaperApiUtil.DOWN_PAPER_API_LOCK);
            if (local) {
                //本地下载
                String middlePath = getPartOfHash(lngid);
                String path = getBasePath(Integer.parseInt((String) detailInfo.get("years"))) + File.separator + middlePath + File.separator + lngid + ".pdf";
                log.info("========================pdfPath start===");
                log.info(path);
                log.info("========================pdfPath end===");
                if (new File(path).exists()) {
                    fileUtil.download(request, response, path, (String) detailInfo.get("title_c"));
                    //接口下载
                } else if (!new File(path).exists() && !DownPaperApiUtil.DOWN_PAPER_API_LOCK) {
                    getStreamOfPdfBydownpaperApi(request,response,detailInfo,lngid);
                }
                //全文不在本机
            } else {
                // 远程其他服务下载接口
                String fileName = (String) detailInfo.get("title_c");
                String urlStr = YmlReadUtil.newInstance().getValueToString("downloadConf.yml", "download.otherServerDownloadUrl");
                request.setCharacterEncoding("utf-8");
                response.setContentType("text/html;charset=UTF-8");
                response.setContentType("application/zip;application/octet-stream");
                String s = URLEncoder.encode(fileName + ".pdf", "UTF-8");

                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                // 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true
                urlConnection.setDoOutput(true);
                // Read from the connection. Default is true.
                urlConnection.setDoInput(true);
                // 默认是 GET方式
                urlConnection.setRequestMethod("POST");

                try{
                    urlConnection.connect();
                }catch(Exception ex){
                    log.info("========================连接下载服务失败！{}",ex.getMessage());
                }

                DataOutputStream out = new DataOutputStream(urlConnection
                        .getOutputStream());
                String content = "lngid=" + URLEncoder.encode(lngid);
                content += "&name=" + URLEncoder.encode(fileName, "UTF-8");
                content += "&year=" + URLEncoder.encode(detailInfo.get("years").toString());
                // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
                out.writeBytes(content);

                out.flush();
                out.close();

                try (
                        BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())
                ) {
                    int available = bis.available();
                    log.info("======================== available pdf size = "+available);
                    if (available == 0 && !DownPaperApiUtil.DOWN_PAPER_API_LOCK) {
                        getStreamOfPdfBydownpaperApi(request,response,detailInfo,lngid);
                    } else {
                        response.setHeader("Content-disposition", "attachment; filename=" + s);
                        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
                        byte[] buff = new byte[2048];
                        int bytesRead;
                        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                            bos.write(buff, 0, bytesRead);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void getStreamOfPdfBydownpaperApi(HttpServletRequest request, HttpServletResponse response, Map<String, Object> detailInfo, String lngid) throws IOException {
        UserInfo currentUser = (UserInfo) redisTemplate.opsForValue().get(request.getSession().getId());
        if (currentUser == null) {
            return;
        }
        String fileName = (String) detailInfo.get("title_c");
        String urlStr = DownPaperApiUtil.createDownLoadUrl(lngid, currentUser.getId() + "", fileName, (String) detailInfo.get("years"));
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/zip;application/octet-stream");
        String s = URLEncoder.encode(fileName + ".pdf", "UTF-8");

        URL url = new URL(urlStr);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        try (
                BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())
        ) {
            response.setHeader("Content-disposition", "attachment; filename=" + s);
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
