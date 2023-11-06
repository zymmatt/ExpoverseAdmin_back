package com.example.admin.entity.Survey;

// 一次问卷作答中勾选了一个选项
public class SurveyOption {
    private int surveyid;
    private int loginid;
    private int option_id;
    public SurveyOption(int loginid, int option_id, int surveyid){
        this.loginid = loginid;
        this.option_id = option_id;
        this.surveyid = surveyid;
    }
}
