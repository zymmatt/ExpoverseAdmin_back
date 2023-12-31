package com.example.admin.mapper;
import com.example.admin.entity.Survey.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyMapper {
    // 获取所有的问题id,问题内容和问题类型
    List<Question> getQuestions();
    // 获取所有的选项id,问题id和选项内容
    List<Option> getOptions();
    // 插入用户填写了一张问卷的基本信息,包括用户id,提交时间,作答时间
    void insertSurveyUser(SingleSurvey singleSurvey);
    // 得到刚才插入的问卷信息的用户id
    int getSurveyUser(SingleSurvey singleSurvey);
    // 插入用户填写的问卷中有关每一个选项的作答情况,某张问卷选了哪个选项
    void insertSurveyOption(SurveyOption surveyOption);
    // 插入用户填写的问卷中某一道题目的回答文字
    void insertQuesFill(QuesFill quesFill);
    // 获得已收集的问卷数量
    List<Integer> getSurveyNum();
    // 获得所有的问题id
    List<Integer> getAllQuesID();
    // 获得某一道题目名下有哪些选项,返回所有的选项id
    List<Integer> getOptionsForOneQues(int ques_id);
    // 获得某一个选项的所有作答loginid和userid
    List<UserSurvey> getUserForOneOption(int option_id);
    // 获得平均完成时间
    List<Integer> getAvgTime();
    // 获取所有问卷调查的问卷id,用户id,答题时长,提交时间
    List<SurveyInfo> getSurvey();
    // 获取某一张调查问卷勾选的所有选项
    List<Integer> getOptionAnswer(int surveyid);
    // 获取所有填空问题的回答,要按照问卷的提交时间从晚到早排序
    List<QuesFill> getallQuesFill();
    // 获取某一张调查问卷某一道题目的填写内容
    String getFilledForOneQues(QuesFill quesFill);
    // 获取某个用户最后填写的一张问卷的触发时间
    SurveyInfo getlatestsurveytimestamp(int userid);
}

