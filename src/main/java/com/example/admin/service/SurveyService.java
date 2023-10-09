package com.example.admin.service;
import com.example.admin.entity.Survey.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface SurveyService {
    List<Question> getQuestions();
    List<Option> getOptions();
    void createSurvey(SingleSurvey singleSurvey);
    int getSurveyNum();
    int getAvgTime();
    List<SingleQuestionStat> getAllQues();
    List<FillQuestionStat> getAllFill();
    List<SingleQuestionByUser> getQuesDetail(int ques_id);
    // void downloadDataExcel(HttpServletResponse response) throws IOException;
    String downloadDataExcel() throws IOException;

}

