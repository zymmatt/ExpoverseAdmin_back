package com.example.admin.service.impl;
import com.example.admin.entity.User.*;
import com.example.admin.entity.Survey.*;
import com.example.admin.service.SurveyService;
import com.example.admin.mapper.SurveyMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.utils.datetime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;



@Service
public class SurveyServiceImpl implements SurveyService{

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public List<Question> getQuestions() { return surveyMapper.getQuestions(); }

    @Override
    @Transactional
    public List<Option> getOptions() { return surveyMapper.getOptions(); }

    // 生成新的调查问卷回报
    @Override
    @Transactional
    public void createSurvey(SingleSurvey singleSurvey) {
        // 插入用户填写了一张问卷的基本信息,包括用户id,提交时间,作答时间,
        surveyMapper.insertSurveyUser(singleSurvey);
        int loginid = singleSurvey.getLoginid();
        for (SingleQuesAnswer question: singleSurvey.getQuestions()){
            // 问题的填空回答插入
            //System.out.println(question.getAnswer().length());
            if (question.filled()){
                //System.out.println("answer:"+question.getAnswer()+"__end");
                QuesFill ques_fill = new QuesFill(question.getQues_id(), loginid, question.getAnswer());
                surveyMapper.insertQuesFill(ques_fill);
            }
            // 一个一个选项的插入
            if (question.hasOptions()){
                //System.out.println(question.getOption().size());
                for (SingleOptionSelect option: question.getOption()){
                    if (option.isSelected()){
                        SurveyOption surveyOption = new SurveyOption(loginid, option.getOption_id());
                        surveyMapper.insertSurveyOption(surveyOption);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public int getSurveyNum(){
        List<Integer> surveylist = surveyMapper.getSurveyNum();
        return surveylist.size();
    }

    @Override
    @Transactional
    public int getAvgTime(){
        List<Integer> timelist = surveyMapper.getAvgTime();
        int total_seconds = 0;
        for (int second: timelist){ total_seconds+= second; }
        return (int)(total_seconds /timelist.size()/60); //返回平均的分钟数
    }

    // 获得所有单选多选问题的统计信息
    @Override
    @Transactional
    public List<SingleQuestionStat> getAllQues() {
        List<SingleQuestionStat> singleQuestionStats=new ArrayList<>();
        List<Integer> queIDs = surveyMapper.getAllQuesID();
        //Map<String, Integer> scheduleDict = new HashMap<>();
        //for (Map.Entry<String, Integer> entry: scheduleDict.entrySet()) {}
        // 获得所有问题id
        for (Integer ques_id:queIDs){
            SingleQuestionStat singleQuestionStat = new SingleQuestionStat(ques_id);
            // 获得一个问题下的所有option id
            List<Integer> Options = surveyMapper.getOptionsForOneQues(ques_id);
            for (Integer option_id:Options){
                //一个一个选项的统计有多少个人选了
                List<UserSurvey>userSurveys = surveyMapper.getUserForOneOption(option_id);
                OptionAnswers optionAnswer=new OptionAnswers(option_id,userSurveys.size());
                singleQuestionStat.addAnswer(optionAnswer);
            }
            singleQuestionStats.add(singleQuestionStat);
        }
        return singleQuestionStats;
    }


    // 获得所有填空问题的回答列表,按照时间顺序由晚到早排序
    @Override
    @Transactional
    public List<FillQuestionStat> getAllFill() {
        List<FillQuestionStat> fillQuestionStats = new ArrayList<>();
        List<QuesFill> quesFillList = surveyMapper.getallQuesFill();
        List<Question> questions = surveyMapper.getQuestions();
        //List<Question> fillquestionList = new ArrayList<>(); // 找出所有填空题
        Map<Integer, List<QuesFill>> quesid2fill = new HashMap<>(); // 问题ID对应到填空答案的合集,可以保证是从晚到早的
        Map<Integer, String> quesid2content = new HashMap<>(); // 问题ID对应到问题内容
        for (Question question:questions){
            // 这道题包含了填空内容
            if (question.getQues_type().contains("fill")){
                int ques_id = question.getQues_id();
                String ques_content = question.getQues_content();
                // String ques_type = question.getQues_type();
                // fillquestionList.add(new Question(ques_id,ques_content,ques_type));
                quesid2fill.put(ques_id,new ArrayList<>());
                quesid2content.put(ques_id, ques_content);
            }
        }
        for (QuesFill quesFill:quesFillList){
            int ques_id = quesFill.getQues_id();
            int loginid = quesFill.getLoginid();
            String filltext = quesFill.getFilltext();
            Long trigger_timestamp = quesFill.getTrigger_timestamp();
            quesid2fill.get(ques_id).add(new QuesFill(ques_id,loginid,filltext,trigger_timestamp));
        }
        for (int ques_id:quesid2fill.keySet()){
            String ques_content = quesid2content.get(ques_id);
            List<QuesFill> answers = quesid2fill.get(ques_id);
            fillQuestionStats.add(new FillQuestionStat(ques_id,ques_content,answers));
        }
        return fillQuestionStats;
    }

    // 基于某一道题,获得所有答题的用户对这道题的选择
    @Override
    @Transactional
    public List<SingleQuestionByUser> getQuesDetail(int ques_id) {
        List<Integer> Options = surveyMapper.getOptionsForOneQues(ques_id);
        // 创建字典,存放每一张问卷针对一道题的选择集合
        Map<Integer, SingleQuestionByUser> surveydict = new HashMap<>();
        for (Integer option_id:Options){
            //一个一个选项的统计有多少个人选了
            List<UserSurvey>userSurveys = surveyMapper.getUserForOneOption(option_id);
            for (UserSurvey userSurvey: userSurveys){
                int tempuserid = userSurvey.getUserid();
                int tempsurveyid = userSurvey.getloginid();
                // 如果字典中没有某个问卷id则要先生成
                if (!surveydict.containsKey(tempsurveyid)){
                    String tempname = userMapper.findNamebyId(tempuserid);
                    surveydict.put(tempsurveyid, new SingleQuestionByUser(ques_id, tempsurveyid, tempuserid, tempname));
                }
                surveydict.get(tempsurveyid).getSelectedOptions().add(option_id);
            }

        }
        return new ArrayList<>(surveydict.values());
    }

    // 汇总所有答题问卷的答题情况
    @Override
    @Transactional
    public void downloadDataExcel(HttpServletResponse response) throws IOException {
        // 创建Excel表格   .xlsx
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("開始時間");
        headerRow.createCell(2).setCellValue("完成時長(秒)");
        headerRow.createCell(3).setCellValue("電子郵件");
        headerRow.createCell(4).setCellValue("名稱");
        int headerRownum = 5;
        // 所有问卷的信息
        List<SurveyInfo> surveyInfos = surveyMapper.getSurvey();
        List<Question> questions = surveyMapper.getQuestions();
        List<Option> options = surveyMapper.getOptions();
        // Map<Integer, String> quesid_text = new HashMap<>();//问题id查中文
        for (Question question:questions){
            // quesid_text.put(question.getQues_id(), question.getQues_content());
            headerRow.createCell(headerRownum).setCellValue(question.getQues_content());
            headerRownum += 1;
        }
        Map<Integer, String> optionid_text = new HashMap<>();//选项id查中文
        Map<Integer, Integer> option_quesid = new HashMap<>();//选项id查问题id
        for (Option option:options){
            optionid_text.put(option.getOption_id(),option.getOption_content());
            option_quesid.put(option.getOption_id(),option.getQues_id());
        }
        List<ExcelSingleLine> excelSingleLineList = new ArrayList<>();
        for (SurveyInfo surveyInfo:surveyInfos){
            int loginid = surveyInfo.getloginid();
            int userid = surveyInfo.getUserid();
            User tempuser = userMapper.findById(userid);
            if (tempuser != null){
                ExcelSingleLine templine = new ExcelSingleLine(tempuser, surveyInfo);
                //这张问卷中所有被勾选的选项
                List<Integer> OptionAnswers = surveyMapper.getOptionAnswer(loginid);
                System.out.println(OptionAnswers);
                //列出所有问题,以及每一个问题下的被勾选的回答选项
                Map<Integer, HashSet<Integer>> ques_option = new HashMap<>();
                for (Question question:questions){
                    ques_option.put(question.getQues_id(),new HashSet<>());
                }
                for (Integer optionid:OptionAnswers){
                    // 选项id找到问题id,再加入到这个问题id下被勾选的选项回答
                    ques_option.get(option_quesid.get(optionid)).add(optionid);
                }
                for (Question question:questions) {
                    int ques_id = question.getQues_id();
                    SingleQuestionByUser singleQuestionByUser = new SingleQuestionByUser(ques_id,loginid,userid,
                                                                tempuser.getName(),ques_option.get(ques_id));
                    if (question.getQues_type().contains("fill")){ //补充填空题内容
                        String filltext = surveyMapper.getFilledForOneQues(new QuesFill(ques_id,loginid,""));
                        singleQuestionByUser.setFilled(filltext);
                    }
                    templine.addquestion(singleQuestionByUser);
                }
                // 一行的回答记录已经收集齐了这张问卷的所有问题下的所有回答
                excelSingleLineList.add(templine);
            }
        }
        int dataRowNum = 1;
        for (ExcelSingleLine templine:excelSingleLineList){
            Row dataRow = sheet.createRow(dataRowNum);
            dataRow.createCell(0).setCellValue(templine.getSurveyInfo().getloginid()); //问卷ID
            dataRow.createCell(1).setCellValue(datetime.timestamp2str(templine.getSurveyInfo().getTrigger_timestamp())); //开始时间
            dataRow.createCell(2).setCellValue(templine.getSurveyInfo().getDuration_sec()); //答卷时长
            dataRow.createCell(3).setCellValue(templine.getUser().getmail()); //名称
            dataRow.createCell(4).setCellValue(templine.getUser().getName()); //名字
            int dataColNum = 5;
            for (SingleQuestionByUser ques:templine.getSingleQuestionByUserList()){
                StringBuilder stringBuilder = new StringBuilder();
                HashSet<Integer> tempset = ques.getSelectedOptions();
                for (Integer ans:tempset) {
                    stringBuilder.append(optionid_text.get(ans)).append(", "); // 追加字符串
                }
                System.out.println("ques.getFilled()   "+ques.getFilled());
                if (ques.getFilled() != null){
                    stringBuilder.append(ques.getFilled()); // 补充填空题内容
                }
                dataRow.createCell(dataColNum).setCellValue(stringBuilder.toString());
                dataColNum += 1;
            }
            //dataRow.createCell(0).setCellValue();
            dataRowNum += 1;
        }

        // 添加更多单元格和数据
        String rawFileName = "问卷调查结果.xlsx";
        response.reset();
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" +
                    URLEncoder.encode(rawFileName, String.valueOf(StandardCharsets.UTF_8)));
            response.setHeader("Access-Control-Allow-Origin", "*");
            //response.setHeader("content_type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("content_type","application/vnd.ms-excel");
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

