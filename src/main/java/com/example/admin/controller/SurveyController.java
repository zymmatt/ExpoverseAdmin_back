package com.example.admin.controller;

import com.example.admin.entity.Response.ResponseObject;
import com.example.admin.entity.Survey.*;

import com.example.admin.service.SurveyService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/survey")
// @CrossOrigin(origins = "http://localhost:8080") // 允许来自http://localhost:3000的请求
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    // 管理员平台  获取所有的问题id,问题内容和问题类型
    @RequestMapping(value="/getQuestions", method = RequestMethod.GET)
    public ResponseObject getQuestions() {
        return ResponseObject.success(surveyService.getQuestions());
    }

    // 管理员平台 获取所有的选项id,问题id和选项内容
    @RequestMapping(value="/getOptions", method = RequestMethod.GET)
    public ResponseObject getOptions() { return ResponseObject.success(surveyService.getOptions()); }

    // 应用端 某一位用户提交了问卷后,记录所有的回答结果录入数据库
    @RequestMapping(value="/createSurvey", method = RequestMethod.POST)
    public ResponseObject createSurvey(@RequestBody SingleSurvey singleSurvey) {
        surveyService.createSurvey(singleSurvey);
        return ResponseObject.success("提交新的问卷调查结果成功");
    }

    // 管理员平台 获得已收集的问卷数量
    @RequestMapping(value="/getSurveyNum", method = RequestMethod.GET)
    public ResponseObject getSurveyNum() {
        return ResponseObject.success(surveyService.getSurveyNum());
    }

    // 管理员平台 获得平均完成时间
    @RequestMapping(value="/getAvgTime", method = RequestMethod.GET)
    public ResponseObject getAvgTime() {
        return ResponseObject.success(surveyService.getAvgTime());
    }

    // 管理员平台 获得所有单选多选问题的回答选项统计量
    @RequestMapping(value="/getAllQues", method = RequestMethod.GET)
    public ResponseObject getAllQues() {
        return ResponseObject.success(surveyService.getAllQues());
    }

    // 管理员平台 获得所有填空问题的回答记录列表
    @RequestMapping(value="/getAllFill", method = RequestMethod.GET)
    public ResponseObject getAllFill() { return ResponseObject.success(surveyService.getAllFill()); }

    // 管理员平台 获得单个问题每一位用户的具体作答
    @RequestMapping(value="/getQuesDetail", method = RequestMethod.GET)
    public ResponseObject getQuesDetail(int ques_id){
        return ResponseObject.success(surveyService.getQuesDetail(ques_id));
    }

    // 管理员平台 把客户问卷作答数据汇集到Excel中,发送给前端供下载
    @RequestMapping(value="/downloadDataExcel", method= RequestMethod.GET)
    //public void downloadExcel(HttpServletResponse response) throws IOException {
    public String downloadExcel() throws IOException {
        return surveyService.downloadDataExcel();
        // return ResponseObject.success("下载客户问卷作答Excel成功");
    }

}
