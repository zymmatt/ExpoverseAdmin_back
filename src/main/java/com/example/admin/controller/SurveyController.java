package com.example.admin.controller;

import com.example.admin.entity.SingleQuestion;
import com.example.admin.entity.SingleQuestionDetail;
import com.example.admin.entity.UserSurvey;

import com.example.admin.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/survey")
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    // 某一位用户提交了问卷后,记录所有的回答结果录入数据库
    @RequestMapping(value="/createSurvey", method = RequestMethod.POST)
    public void createSurvey(@RequestBody UserSurvey userSurvey) {
        surveyService.createSurvey(userSurvey);
    }


    // 获得所有问题的回答选项统计量
    @RequestMapping(value="/getAllQues", method = RequestMethod.GET)
    public List<SingleQuestion> getAllQues() {
        return surveyService.getAllQues();
    }

    // 获得单个问题每一位用户的具体作答
    @RequestMapping(value="/getQuesDetail", method = RequestMethod.GET)
    public List<SingleQuestionDetail> getQuesDetail(int ques_id){
        return surveyService.getQuesDetail(ques_id);
    }

    // 把客户问卷作答数据汇集到Excel中,发送给前端供下载
    @RequestMapping(value="/downloadDataExcel", method= RequestMethod.GET)
    public void downloadDataExcel(){
        surveyService.downloadDataExcel();
    }

}
