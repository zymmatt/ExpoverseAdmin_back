package com.example.admin.entity.Survey;

import java.util.HashSet;
import java.util.List;

// 针对一道问题,一个用户选择的所有选项id
public class SingleQuestionByUser {
    private int ques_id;
    private int survey_id;
    private int userid;
    private String user_name;
    private HashSet<Integer> selectedOptions; //基于某一道问题勾选的所有选项

    public SingleQuestionByUser(int ques_id, int survey_id, int userid, String user_name){
        this.ques_id = ques_id;
        this.survey_id = survey_id;
        this.userid = userid;
        this.user_name = user_name;
        this.selectedOptions = new HashSet<>();
    }

    public HashSet<Integer> getSelectedOptions() {
        return selectedOptions;
    }


}
