package com.example.admin.entity.Survey;

public class UserSurvey {
    private int survey_id;
    private int userid;

    public int getUserid() {
        return userid;
    }

    public int getSurvey_id() {
        return survey_id;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setSurvey_id(int survey_id) {
        this.survey_id = survey_id;
    }

}
