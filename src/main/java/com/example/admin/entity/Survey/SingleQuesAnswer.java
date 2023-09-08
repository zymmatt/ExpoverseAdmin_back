package com.example.admin.entity.Survey;

import com.example.admin.entity.Survey.SingleOptionSelect;

import java.util.List;

// 针对某一道问题的一次回答
public class SingleQuesAnswer {
    private int ques_id;
    private String answer; // 针对这道题的填空回答
    private List<SingleOptionSelect> option; //一道题目下很多个选项的选择详情,单选多选都适用
    // 这道题是否有填空内容
    public boolean filled(){
        return this.answer.length()>0;
    }
    // 这道题是否有选择内容
    public boolean hasOptions(){
        return this.option.size()>0;
    }

    public List<SingleOptionSelect> getOption() {
        return option;
    }

    public int getQues_id() { return ques_id; }

    public String getAnswer() { return answer; }
}
