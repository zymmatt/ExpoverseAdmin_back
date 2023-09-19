package com.example.admin.entity.Survey;
// 在问卷中对某一道问题给出的填空回答
public class QuesFill {
    private int ques_id;
    private int loginid;
    private String filltext;

    public QuesFill(int ques_id, int loginid, String filltext){
        this.ques_id = ques_id;
        this.loginid = loginid;
        this.filltext = filltext;
    }

}
