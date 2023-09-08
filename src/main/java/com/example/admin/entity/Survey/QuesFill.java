package com.example.admin.entity.Survey;
// 在问卷中对某一道问题给出的填空回答
public class QuesFill {
    private int ques_id;
    private int survey_id;
    private String filltext;

    public QuesFill(int ques_id, int survey_id, String filltext){
        this.ques_id = ques_id;
        this.survey_id = survey_id;
        this.filltext = filltext;
    }

}
