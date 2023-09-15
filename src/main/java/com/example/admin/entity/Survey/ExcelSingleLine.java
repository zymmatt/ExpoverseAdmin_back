package com.example.admin.entity.Survey;
import com.example.admin.entity.User.User;
import com.example.admin.entity.Survey.SurveyInfo;
import com.example.admin.entity.Survey.SingleQuestionByUser;

import java.util.ArrayList;
import java.util.List;

// 问卷调查一行内的信息, 包含了问卷id,提交时间,用户姓名,电子邮箱,每一道问题的回答
public class ExcelSingleLine {
    private User user;
    private SurveyInfo surveyInfo;
    private List<SingleQuestionByUser> singleQuestionByUserList;

    public List<SingleQuestionByUser> getSingleQuestionByUserList() {
        return singleQuestionByUserList;
    }

    public SurveyInfo getSurveyInfo() {
        return surveyInfo;
    }

    public User getUser() {
        return user;
    }

    public void setSingleQuestionByUserList(List<SingleQuestionByUser> singleQuestionByUserList) {
        this.singleQuestionByUserList = singleQuestionByUserList;
    }

    public void setSurveyInfo(SurveyInfo surveyInfo) {
        this.surveyInfo = surveyInfo;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExcelSingleLine(User user, SurveyInfo surveyInfo){
        this.user = user;
        this.surveyInfo = surveyInfo;
        this.singleQuestionByUserList = new ArrayList<>();
    }

    public void addquestion(SingleQuestionByUser singleQuestionByUser){
        this.singleQuestionByUserList.add(singleQuestionByUser);
    }


}
