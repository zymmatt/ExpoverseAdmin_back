package com.example.admin.entity.Survey;

import java.util.List;

// survey_user表的对应信息
public class SurveyInfo {
    private int survey_id;// 问卷提交后得到的问卷id
    private int userid; // 用户id
    private int duration_sec; // 回答用了多长时间
    private int trigger_timestamp; // 提交的时间戳

    public int getUserid() {
        return userid;
    }

    public int getDuration_sec() {
        return duration_sec;
    }

    public int getTrigger_timestamp() {
        return trigger_timestamp;
    }

    public int getSurvey_id() {
        return survey_id;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setDuration_sec(int duration_sec) {
        this.duration_sec = duration_sec;
    }

    public void setSurvey_id(int survey_id) {
        this.survey_id = survey_id;
    }

    public void setTrigger_timestamp(int trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }
}
