package com.example.admin.entity;

import java.util.List;

// 用户提交上来的答题问卷的形式
public class UserSurvey {
    private int userid; // 用户id
    private int duration_sec; // 回答用了多长时间
    private int trigger_timestamp; // 提交的时间戳
    private List<Enum> questions; // 问卷的详情
}
