package com.example.admin.entity.Survey;

import java.util.List;

public class FillQuestionStat {
    private int ques_id; //问题的id
    private String ques_content; //问题的内容
    private List<QuesFill> answers;

    public FillQuestionStat(int ques_id, String ques_content,
                            List<QuesFill> answers){
        this.ques_id = ques_id;
        this.ques_content = ques_content;
        this.answers = answers;
    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public String getQues_content() {
        return ques_content;
    }

    public void setQues_content(String ques_content) {
        this.ques_content = ques_content;
    }

    public List<QuesFill> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuesFill> answers) {
        this.answers = answers;
    }
}
