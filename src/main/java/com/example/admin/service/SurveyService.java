package com.example.admin.service;
import com.example.admin.entity.SingleQuestion;
import com.example.admin.entity.UserSurvey;
import com.example.admin.entity.SingleQuestionDetail;
import java.util.List;

public interface SurveyService {

    void createSurvey(UserSurvey userSurvey);
    List<SingleQuestion> getAllQues();
    List<SingleQuestionDetail> getQuesDetail(int ques_id);
    void downloadDataExcel();

}
