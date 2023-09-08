package com.example.admin.mapper;
import com.example.admin.entity.Survey.SingleSurvey;
import com.example.admin.entity.Survey.UserSurvey;
import com.example.admin.entity.Survey.SurveyOption;
import com.example.admin.entity.Survey.QuesFill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyMapper {
    // 插入用户填写了一张问卷的基本信息,包括用户id,提交时间,作答时间,返回问卷id
    int insertSurveyUser(SingleSurvey singleSurvey);
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
    // 获得某一个选项的所有作答survey_id和userid
    List<UserSurvey> getUserForOneOption(int option_id);
    // 获得平均完成时间
    List<Integer> getAvgTime();
}

