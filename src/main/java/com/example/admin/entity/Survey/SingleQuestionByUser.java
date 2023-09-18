package com.example.admin.entity.Survey;

import java.util.HashSet;
import java.util.List;

// 针对一道问题,一个用户选择的所有选项id,还有可能填入的内容
public class SingleQuestionByUser {
    private int ques_id;
    private int survey_id;
    private int userid;
    private String user_name;
    private String filled; //填空的内容
    private HashSet<Integer> selectedOptions; //基于某一道问题勾选的所有选项

    public SingleQuestionByUser(int ques_id, int survey_id, int userid, String user_name){
        this.ques_id = ques_id;
        this.survey_id = survey_id;
        this.userid = userid;
        this.user_name = user_name;
        this.selectedOptions = new HashSet<>();
        this.filled="";
    }

    public void setSurvey_id(int survey_id) {
        this.survey_id = survey_id;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return userid;
    }

    public int getSurvey_id() {
        return survey_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public int getQues_id() {
        return ques_id;
    }

    public String getFilled() {
        return filled;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setFilled(String filled) {
        this.filled = filled;
    }

    public void setSelectedOptions(HashSet<Integer> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public HashSet<Integer> getSelectedOptions() {
        return selectedOptions;
    }

    public void setOption(int optionid){
        this.selectedOptions.add(optionid);
    }

    public SingleQuestionByUser(int ques_id, int survey_id, int userid,
                                String user_name, HashSet<Integer> selectedOptions){
        this.ques_id=ques_id;
        this.survey_id=survey_id;
        this.userid=userid;
        this.user_name=user_name;
        this.selectedOptions=selectedOptions;
        this.filled="";
    }

}
