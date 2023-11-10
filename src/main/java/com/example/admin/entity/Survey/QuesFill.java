package com.example.admin.entity.Survey;
// 在问卷中对某一道问题给出的填空回答
public class QuesFill {
    private int surveyid; // 调查问卷ID
    private int ques_id;
    private int loginid;
    private String filltext;
    private Long trigger_timestamp; // 这个问题的回答时间戳,在部分统计中会用到

    public QuesFill(){}

    public QuesFill(int ques_id, int loginid, String filltext, int surveyid){
        this.ques_id = ques_id;
        this.loginid = loginid;
        this.filltext = filltext;
        this.surveyid = surveyid;
    }

    public QuesFill(int ques_id, int loginid, String filltext, Long trigger_timestamp, int surveyid){
        this.ques_id = ques_id;
        this.loginid = loginid;
        this.filltext = filltext;
        this.trigger_timestamp = trigger_timestamp;
        this.surveyid = surveyid;
    }

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public String getFilltext() {
        return filltext;
    }

    public void setFilltext(String filltext) {
        this.filltext = filltext;
    }

    public int getLoginid() {
        return loginid;
    }

    public void setLoginid(int loginid) {
        this.loginid = loginid;
    }

    public Long getTrigger_timestamp() {
        return trigger_timestamp;
    }

    public void setTrigger_timestamp(Long trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }



}
