package com.example.admin.service.impl;

import com.example.admin.entity.SingleQuestion;
import com.example.admin.entity.UserSurvey;
import com.example.admin.entity.SingleQuestionDetail;
import com.example.admin.mapper.ExhibitionVisitMapper;
import com.example.admin.service.SurveyService;
import com.example.admin.mapper.SurveyMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SurveyServiceImpl implements SurveyService{

    @Autowired
    private SurveyMapper surveyMapper;

    @Override
    public void createSurvey(UserSurvey userSurvey) {

    }

    @Override
    public List<SingleQuestion> getAllQues() {
        return null;
    }

    @Override
    public List<SingleQuestionDetail> getQuesDetail(int ques_id) {
        return null;
    }

    @Override
    public void downloadDataExcel() {

    }
}
