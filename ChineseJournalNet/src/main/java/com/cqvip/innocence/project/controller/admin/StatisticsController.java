package com.cqvip.innocence.project.controller.admin;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.enums.OperateType;
import com.cqvip.innocence.common.enums.VisitType;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.StatisticsDTO;
import com.cqvip.innocence.project.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author eternal
 * @Date 2021/12/20
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.manager}/statistics")
public class StatisticsController {

 @Autowired
 private UserLogService userLogService;

 @PostMapping("visitAnalysis")
 @Log(title = "访问量统计",operateType = OperateType.SEARCH,resourceName = "访问量统计")
 public JsonResult visitAnalysis(@RequestBody StatisticsDTO statisticsDTO){
  String[] classNumberArray=new String[0];
  String[] classNumbers = statisticsDTO.getClassNumber();
  if(classNumbers != null) {
   for (int i = 0; i < classNumbers.length; i++) {
    String classNumber = classNumbers[i];
    classNumberArray = ArrayUtil.addAll(classNumberArray, classNumber.split(","));
   }
  }
  statisticsDTO.setClassNumber(classNumberArray);
  List<Map> result = userLogService.analysis(statisticsDTO);
  long count = result.stream().collect(Collectors.summarizingInt(i -> Integer.parseInt(i.get("COUNT").toString()))).getSum();
  HashMap total = new HashMap<>();
  total.put("VALUE","合计");
  total.put("COUNT",count);
  result.add(total);
  return JsonResult.Get("result",result);
 }

 @PostMapping("downloadAnalysis")
 @Log(title = "下载量统计",operateType = OperateType.DOWNLOAD,resourceName = "下载量统计")
 public JsonResult downloadAnalysis(@RequestBody StatisticsDTO statisticsDTO){
  return visitAnalysis(statisticsDTO);
 }

 @PostMapping("visitExport")
 @Log(title = "访问量统计分析Excel下载",operateType = OperateType.DOWNLOAD,resourceName = "访问量统计")
 public void visitExport(@RequestBody StatisticsDTO statisticsDTO, HttpServletResponse response) throws UnsupportedEncodingException {
  List<Map> result = userLogService.analysis(statisticsDTO);
  long count = result.stream().collect(Collectors.summarizingInt(i -> Integer.parseInt(i.get("COUNT").toString()))).getSum();
  HashMap total = new HashMap<>();
  total.put("VALUE","合计");
  total.put("COUNT",count);
  result.add(total);

  ExcelWriter writer = ExcelUtil.getWriter();
  writer.addHeaderAlias("VALUE","统计名称");
  writer.addHeaderAlias("COUNT","次数");
  writer.setOnlyAlias(true);
  writer.write(result);
  response.setContentType("application/vnd.ms-excel;charset=utf-8");
  response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
  String fileName = URLEncoder.encode("统计信息.xls", "UTF-8");
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

 @PostMapping("downloadExport")
 @Log(title = "下周量统计分析Excel下载",operateType = OperateType.SEARCH,resourceName = "下载量统计")
 public void downloadExport(@RequestBody StatisticsDTO statisticsDTO, HttpServletResponse response) throws UnsupportedEncodingException {
  this.visitExport(statisticsDTO,response);
 }

 }