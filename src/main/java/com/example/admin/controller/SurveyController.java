package com.example.admin.controller;

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
@CrossOrigin(origins = "http://localhost:8080") // 允许来自http://localhost:3000的请求
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    // 获取所有的问题id,问题内容和问题类型
    @RequestMapping(value="/getQuestions", method = RequestMethod.GET)
    public List<Question> getQuestions() {
        return surveyService.getQuestions();
    }

    // 获取所有的选项id,问题id和选项内容
    @RequestMapping(value="/getOptions", method = RequestMethod.GET)
    public List<Option> getOptions() { return surveyService.getOptions(); }

    // 某一位用户提交了问卷后,记录所有的回答结果录入数据库
    @RequestMapping(value="/createSurvey", method = RequestMethod.POST)
    public void createSurvey(@RequestBody SingleSurvey singleSurvey) {
        surveyService.createSurvey(singleSurvey);
    }

    // 获得已收集的问卷数量
    @RequestMapping(value="/getSurveyNum", method = RequestMethod.GET)
    public int getSurveyNum() {
        return surveyService.getSurveyNum();
    }

    // 获得平均完成时间
    @RequestMapping(value="/getAvgTime", method = RequestMethod.GET)
    public int getAvgTime() {
        return surveyService.getAvgTime();
    }

    // 获得所有问题的回答选项统计量
    @RequestMapping(value="/getAllQues", method = RequestMethod.GET)
    public List<SingleQuestionStat> getAllQues() {
        return surveyService.getAllQues();
    }

    // 获得单个问题每一位用户的具体作答
    @RequestMapping(value="/getQuesDetail", method = RequestMethod.GET)
    public List<SingleQuestionByUser> getQuesDetail(int ques_id){
        return surveyService.getQuesDetail(ques_id);
    }

    // 把客户问卷作答数据汇集到Excel中,发送给前端供下载
    @RequestMapping(value="/downloadDataExcel", method= RequestMethod.GET)
    //public ResponseEntity<byte[]> downloadExcel() throws IOException {
    public void downloadExcel(HttpServletResponse response) throws IOException {
        //File file = new File("C:\\Users\\IES235074\\Downloads\\localtest.xlsx");
        //FileInputStream fileInputStream = new FileInputStream(file);
        //Workbook workbook = new XSSFWorkbook(fileInputStream);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Hello");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("World");

        //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //workbook.write(byteArrayOutputStream);

        // 关闭 workbook 和 ByteArrayOutputStream
        //workbook.close();
        //byteArrayOutputStream.close();
        response.setHeader("Content-Disposition", "attachment;fileName=" +
                URLEncoder.encode("testexcel", String.valueOf(StandardCharsets.UTF_8)));
        response.setHeader("Access-Control-Allow-Origin", "*");
        //response.setHeader("content_type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("content_type","application/vnd.ms-excel");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        workbook.close();

        /*
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + URLEncoder.encode("fileName", "UTF-8"));
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
        */
    }
    //public void downloadDataExcel(HttpServletResponse response) throws IOException {
        //surveyService.downloadDataExcel(response);
    //}


}
