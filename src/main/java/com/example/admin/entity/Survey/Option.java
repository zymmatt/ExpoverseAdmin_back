package com.example.admin.entity.Survey;

public class Option {
    private int option_id;
    private int ques_id;
    private String option_content;

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public void setOption_content(String option_content) {
        this.option_content = option_content;
    }

    public String getOption_content() {
        return option_content;
    }

    public int getOption_id() {
        return option_id;
    }
}
