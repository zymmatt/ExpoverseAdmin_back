package com.example.admin.entity;

import java.util.List;

// 针对一道问题,一个用户选择的所有选项id
public class SingleQuestionDetail {
    private int userid; // 用户id
    private int ques_id; // 问题id
    private List<Integer> choices;
}
