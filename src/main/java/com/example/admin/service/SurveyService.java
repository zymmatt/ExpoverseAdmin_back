package com.example.admin.service;
import com.example.admin.entity.Survey.*;
import java.util.List;

public interface SurveyService {
    List<Question> getQuestions();
    List<Option> getOptions();
    void createSurvey(SingleSurvey singleSurvey);
    int getSurveyNum();
    int getAvgTime();
    List<SingleQuestionStat> getAllQues();
    List<SingleQuestionByUser> getQuesDetail(int ques_id);
    void downloadDataExcel();

}
