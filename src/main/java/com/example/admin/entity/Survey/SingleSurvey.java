package com.example.admin.entity.Survey;

import com.example.admin.entity.Survey.SingleQuesAnswer;

import java.util.List;

// 用户提交上来的答题问卷的形式
public class SingleSurvey {
    private int loginid;// 问卷提交后得到的问卷id
    private int userid; // 用户id
    private int duration_sec; // 回答用了多长时间
    private int trigger_timestamp; // 提交的时间戳
    private List<SingleQuesAnswer> questions; // 问卷的详情

    public int getUserid() {
        return userid;
    }

    public int getDuration_sec() {
        return duration_sec;
    }

    public int getTrigger_timestamp() {
        return trigger_timestamp;
    }

    public List<SingleQuesAnswer> getQuestions() {
        return questions;
    }

    public int getLoginid() {
        return loginid;
    }

    public void setLoginid(int loginid) {
        this.loginid = loginid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setTrigger_timestamp(int trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }

    public void setDuration_sec(int duration_sec) {
        this.duration_sec = duration_sec;
    }

    public void setQuestions(List<SingleQuesAnswer> questions) {
        this.questions = questions;
    }
}
