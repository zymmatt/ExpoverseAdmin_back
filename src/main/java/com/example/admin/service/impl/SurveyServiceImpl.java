package com.example.admin.service.impl;
import com.example.admin.entity.User.*;
import com.example.admin.entity.Survey.*;
import com.example.admin.service.SurveyService;
import com.example.admin.mapper.SurveyMapper;
import com.example.admin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SurveyServiceImpl implements SurveyService{

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Question> getQuestions() { return surveyMapper.getQuestions(); }

    @Override
    public List<Option> getOptions() { return surveyMapper.getOptions(); }

    // 生成新的调查问卷回报
    @Override
    public void createSurvey(SingleSurvey singleSurvey) {
        // 插入用户填写了一张问卷的基本信息,包括用户id,提交时间,作答时间,
        int survey_id = surveyMapper.insertSurveyUser(singleSurvey);//返回得到的问卷id
        int userid = singleSurvey.getUserid();
        for (SingleQuesAnswer question: singleSurvey.getQuestions()){
            // 问题的填空回答插入
            //System.out.println(question.getAnswer().length());
            if (question.filled()){
                //System.out.println("answer:"+question.getAnswer()+"__end");
                QuesFill ques_fill = new QuesFill(question.getQues_id(), survey_id, question.getAnswer());
                surveyMapper.insertQuesFill(ques_fill);
            }
            // 一个一个选项的插入
            if (question.hasOptions()){
                //System.out.println(question.getOption().size());
                for (SingleOptionSelect option: question.getOption()){
                    if (option.isSelected()){
                        SurveyOption surveyOption = new SurveyOption(survey_id, option.getOption_id());
                        surveyMapper.insertSurveyOption(surveyOption);
                    }
                }
            }
        }
    }

    @Override
    public int getSurveyNum(){
        List<Integer> surveylist = surveyMapper.getSurveyNum();
        return surveylist.size();
    }

    @Override
    public int getAvgTime(){
        List<Integer> timelist = surveyMapper.getAvgTime();
        int total_seconds = 0;
        for (int second: timelist){ total_seconds+= second; }
        return (int)(total_seconds /timelist.size()/60); //返回平均的分钟数
    }

    // 获得所有问题的统计信息
    @Override
    public List<SingleQuestionStat> getAllQues() {
        List<SingleQuestionStat> singleQuestionStats=new ArrayList<>();
        List<Integer> queIDs = surveyMapper.getAllQuesID();
        //Map<String, Integer> scheduleDict = new HashMap<>();
        //for (Map.Entry<String, Integer> entry: scheduleDict.entrySet()) {}
        // 获得所有问题id
        for (Integer ques_id:queIDs){
            SingleQuestionStat singleQuestionStat = new SingleQuestionStat(ques_id);
            // 获得一个问题下的所有option id
            List<Integer> Options = surveyMapper.getOptionsForOneQues(ques_id);
            for (Integer option_id:Options){
                //一个一个选项的统计有多少个人选了
                List<UserSurvey>userSurveys = surveyMapper.getUserForOneOption(option_id);
                OptionAnswers optionAnswer=new OptionAnswers(option_id,userSurveys.size());
                singleQuestionStat.addAnswer(optionAnswer);
            }
            singleQuestionStats.add(singleQuestionStat);
        }
        return singleQuestionStats;
    }

    // 基于某一道题,获得所有答题的用户对这道题的选择
    @Override
    public List<SingleQuestionByUser> getQuesDetail(int ques_id) {
        List<Integer> Options = surveyMapper.getOptionsForOneQues(ques_id);
        // 创建字典,存放每一张问卷针对一道题的选择集合
        Map<Integer, SingleQuestionByUser> surveydict = new HashMap<>();
        for (Integer option_id:Options){
            //一个一个选项的统计有多少个人选了
            List<UserSurvey>userSurveys = surveyMapper.getUserForOneOption(option_id);
            for (UserSurvey userSurvey: userSurveys){
                int tempuserid = userSurvey.getUserid();
                int tempsurveyid = userSurvey.getSurvey_id();
                // 如果字典中没有某个问卷id则要先生成
                if (!surveydict.containsKey(tempsurveyid)){
                    String tempname = userMapper.findNamebyId(tempuserid);
                    surveydict.put(tempsurveyid, new SingleQuestionByUser(ques_id, tempsurveyid, tempuserid, tempname));
                }
                surveydict.get(tempsurveyid).getSelectedOptions().add(option_id);
            }

        }
        return new ArrayList<>(surveydict.values());
    }

    // 汇总所有答题问卷的答题情况
    @Override
    public void downloadDataExcel() {
        // 所有问卷的信息
        List<SurveyInfo> surveyInfos = surveyMapper.getSurvey();
        List<Question> questions = surveyMapper.getQuestions();
        List<>
        List<ExcelSingleLine> excelSingleLineList = new ArrayList<>();
        for (SurveyInfo surveyInfo:surveyInfos){
            int survey_id = surveyInfo.getSurvey_id();
            int userid = surveyInfo.getUserid();
            User tempuser = userMapper.findById(userid);
            ExcelSingleLine templine = new ExcelSingleLine(tempuser, surveyInfo);
            List<Integer> OptionAnswers = surveyMapper.getOptionAnswer(survey_id);
            for (Question question:questions){




            }



        }



    }
}
