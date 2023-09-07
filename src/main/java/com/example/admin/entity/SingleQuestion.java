package com.example.admin.entity;

import com.example.admin.entity.OptionAnswer;

import java.util.List;

// 记录单个题目中每个选项的统计结果
public class SingleQuestion {
    private int ques_id; //问题的id
    private String ques_content; //问题的内容
    private List<OptionAnswer> answers;

    public int getQues_id() {
        return ques_id;
    }

    public List<OptionAnswer> getAnswers() {
        return answers;
    }

    public String getQues_content() {
        return ques_content;
    }

    public void setAnswers(List<OptionAnswer> answers) {
        this.answers = answers;
    }

    public void addAnswer(OptionAnswer answer) {
        this.answers.add(answer);
    }

    public void setQues_content(String ques_content) {
        this.ques_content = ques_content;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }
}
