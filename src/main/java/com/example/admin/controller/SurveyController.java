package com.example.admin.controller;

import com.example.admin.entity.Survey.*;

import com.example.admin.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/survey")
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
    public void downloadDataExcel(){
        surveyService.downloadDataExcel();
    }

}
