package com.example.admin.entity.Survey;

// 一次问卷作答中勾选了一个选项
public class SurveyOption {
    private int survey_id;
    private int option_id;
    public SurveyOption(int survey_id, int option_id){
        this.survey_id = survey_id;
        this.option_id = option_id;
    }
}
