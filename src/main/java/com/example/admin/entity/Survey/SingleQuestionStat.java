package com.example.admin.entity.Survey;

import java.util.ArrayList;
import java.util.List;

// 记录单个题目中每个选项的统计结果
public class SingleQuestionStat {
    private int ques_id; //问题的id
    private String ques_content; //问题的内容
    private String label; // 问题label
    private List<OptionAnswers> answers;

    public int getQues_id() {
        return ques_id;
    }

    public List<OptionAnswers> getAnswers() {
        return answers;
    }

    public String getQues_content() {
        return ques_content;
    }

    public void setAnswers(List<OptionAnswers> answers) {
        this.answers = answers;
    }

    public void addAnswer(OptionAnswers answer) {
        this.answers.add(answer);
    }

    public void setQues_content(String ques_content) {
        this.ques_content = ques_content;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public SingleQuestionStat(int ques_id, String label){
        this.ques_id = ques_id;
        this.label = label;
        this.answers = new ArrayList<>();
    }
}


