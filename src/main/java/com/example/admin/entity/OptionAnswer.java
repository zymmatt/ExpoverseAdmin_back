package com.example.admin.entity;

// 统计单个选项有多少个人选了
public class OptionAnswer {
    //private int ques_id; // 问题id
    private int option_id; // 选项id
    private String option_content; //选项内容
    private int answer_num; // 选择了这个选项的人数

    public int getAnswer_num() {
        return answer_num;
    }

    public int getOption_id() {
        return option_id;
    }

    public String getOption_content() {
        return option_content;
    }

    public void setAnswer_num(int answer_num) {
        this.answer_num = answer_num;
    }

    public void setOption_content(String option_content) {
        this.option_content = option_content;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }
}
