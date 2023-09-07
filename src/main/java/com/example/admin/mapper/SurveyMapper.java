package com.example.admin.mapper;
import com.example.admin.entity.UserSurvey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyMapper {
    // 插入用户填写了一张问卷的基本信息,包括用户id,提交时间,作答时间,
    void insertSurveyUser(UserSurvey userSurvey);
    // 插入用户填写的问卷中有关每一个选项的作答情况,某张问卷选了哪个选项,是否有填空内容
    void insertSurveyOption(int survey_id, int option_id, String option_content);



}
