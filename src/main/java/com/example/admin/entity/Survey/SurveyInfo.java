package com.example.admin.entity.Survey;

// survey_user表的对应信息
public class SurveyInfo {
    private int surveyid; // 问卷提交后得到的问卷id
    private int loginid; // 登录id,一次登录有可能填写多次问卷
    private int userid; // 用户id
    private int duration_sec; // 回答用了多长时间
    private int trigger_timestamp; // 提交的时间戳

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public int getLoginid() {
        return loginid;
    }

    public void setLoginid(int loginid) {
        this.loginid = loginid;
    }

    public int getUserid() {
        return userid;
    }

    public int getDuration_sec() {
        return duration_sec;
    }

    public int getTrigger_timestamp() {
        return trigger_timestamp;
    }

    public int getloginid() {
        return loginid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setDuration_sec(int duration_sec) {
        this.duration_sec = duration_sec;
    }

    public void setloginid(int loginid) {
        this.loginid = loginid;
    }

    public void setTrigger_timestamp(int trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }
}
