package com.cqvip.innocence.project.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @ClassName AbstractController
 * @Description TODO
 * @Author Innocence
 * @Date 2020/9/22 11:19
 * @Version 1.0
 */
public abstract class AbstractController {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final Long DEFAULT_PAGE_NUM = 1L;
    private static final Long DEFAULT_PAGE_SIZE = 10L;
    private static final String PAGE_NUM_KEY = "pageNum";
    private static final String PAGE_SIZE_KEY = "pageSize";
    private ThreadLocal<Page> pageParams = new ThreadLocal();
    private ThreadLocal<PageRequest> pageRequest = new ThreadLocal();
    private ThreadLocal<Boolean> initedParamsVariable = new ThreadLocal();
    private ThreadLocal<HttpServletRequest> request = new ThreadLocal();
    private ThreadLocal<HttpServletResponse> response = new ThreadLocal();

    protected void initParamsVariable() {
        Boolean aBoolean = this.initedParamsVariable.get();
        if (aBoolean == null || !aBoolean) {
            this.initedParamsVariable.set(true);
            Long page ;
            Long size ;
            String param = this.getParam(PAGE_NUM_KEY);
            if (StrUtil.isNotBlank(param)){
                page = Long.valueOf(param);
            }else{
                page = DEFAULT_PAGE_NUM;
            }
            String sizeStr = this.getParam(PAGE_SIZE_KEY);
            if (StrUtil.isNotBlank(sizeStr) ) {
                size = Long.valueOf(sizeStr);
            }else{
                size = DEFAULT_PAGE_SIZE;
            }
            this.pageParams.set(new Page(page, size));
            this.initedParamsVariable.remove();
        }
    }

    protected String getParam(String key) {
        return this.getRequest().getParameter(key);
    }

    protected HttpServletRequest getRequest() {
        HttpServletRequest request = this.request.get();
        if (request == null) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            request = ((ServletRequestAttributes)requestAttributes).getRequest();
        }
        return request;
    }

    /**
     * ??????mybatis-plus????????????
     * @author Innocence
     * @date 2021/8/23
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page
     */
    protected Page getPageParams() {
        this.initParamsVariable();
        return this.pageParams.get();
    }

    protected String formatIpNumber(String ip) {
        if(StringUtils.isBlank(ip)){
            return null;
        }
        List<String> ips = new ArrayList<>();
        if(ip.contains("-")){
            ips = Arrays.asList(ip.split("-"));
        }else{
            ips.add(ip);
        }
        List<String> ipNumbers = new ArrayList<>();
        for (String item : ips) {
            String ipNumber = "";
            if(StringUtils.isBlank(item)){
                continue;
            }
            for (String s : item.split("\\.")) {
                String format = String.format("%0" + 3 + "d", Integer.parseInt(s));
                ipNumber+=format;
            }
            ipNumbers.add(ipNumber);
        }
        return CollectionUtil.join(ipNumbers,"-");
    }

}
