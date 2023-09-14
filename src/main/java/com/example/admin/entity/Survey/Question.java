package com.example.admin.entity.Survey;

public class Question {
    private int ques_id;
    private String ques_content;
    private String ques_type;

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public void setQues_content(String ques_content) {
        this.ques_content = ques_content;
    }

    public String getQues_content() {
        return ques_content;
    }

    public int getQues_id() {
        return ques_id;
    }

    public String getQues_type() {
        return ques_type;
    }

    public void setQues_type(String ques_type) {
        this.ques_type = ques_type;
    }
}
