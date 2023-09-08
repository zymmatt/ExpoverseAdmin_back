package com.example.admin.service;
import com.example.admin.entity.Survey.SingleQuestionStat;
import com.example.admin.entity.Survey.SingleSurvey;
import com.example.admin.entity.Survey.SingleQuestionByUser;
import java.util.List;

public interface SurveyService {

    void createSurvey(SingleSurvey singleSurvey);
    int getSurveyNum();
    int getAvgTime();
    List<SingleQuestionStat> getAllQues();
    List<SingleQuestionByUser> getQuesDetail(int ques_id);
    void downloadDataExcel();


}
