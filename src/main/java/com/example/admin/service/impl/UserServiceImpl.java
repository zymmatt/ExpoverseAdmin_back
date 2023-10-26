// UserServiceImpl.java
package com.example.admin.service.impl;

import com.example.admin.entity.User.*;
import com.example.admin.mapper.InvitationCodeMapper;
import com.example.admin.service.UserService;
import com.example.admin.mapper.UserMapper;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.admin.utils.pinyin.containsChinese;
import static com.example.admin.utils.pinyin.convertToPinyin;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    @Override
    @Transactional
    public List<User> findAll() {
        List<User>users = userMapper.findAll();
        List<InvitationCode>invitationCodes = invitationCodeMapper.getLatestCodeForEach();
        HashMap<Integer,InvitationCode> user2code = new HashMap<>(); // 用户id查找最近的邀请码的字典
        for (InvitationCode invitationCode:invitationCodes){
            user2code.put(invitationCode.getuserid(),invitationCode);
        }
        for (User user:users){
            int userid = user.getId();
            if (user2code.containsKey(userid)){
                LocalDate endDate = user2code.get(userid).getEndDate();
                LocalTime startTime = user2code.get(userid).getStartTime();
                // 获取当前的 LocalDateTime
                LocalDateTime currentDateTime = LocalDateTime.now();
                // 将给定的 LocalDate 和 LocalTime 转换为 LocalDateTime
                LocalDateTime givenDateTime = endDate .atTime(startTime);
                if (givenDateTime.isAfter(currentDateTime)) {
                    // 验证码还没有过期,返回过期时间
                    // 定义日期和时间的格式
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    // 将日期和时间转换为String
                    String dateStr = endDate.format(dateFormatter);
                    String timeStr = startTime.format(timeFormatter);
                    user.setEndDT(String.format("%s %s",dateStr,timeStr));
                }
                else {
                    // 验证码已经过期
                    user.setEndDT("");
                }
            }
            else{
                user.setEndDT("");
            }

        }
        return users;
    }

    @Override
    @Transactional
    public List<User> findbyNamePage(int limit, int page, String name) {
        return userMapper.findbyNamePage(limit, 20*page-20, name);
    }

    @Override
    @Transactional
    public User findById(int id) {
        return userMapper.findById(id);
    }

    @Override
    @Transactional
    public List<DayUserVisit> getUserbyDate(Long startDate, Long endDate) {
        // 将时间戳转换为LocalDate
        LocalDate start = Instant.ofEpochSecond(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = Instant.ofEpochSecond(endDate).atZone(ZoneId.systemDefault()).toLocalDate();
        // 创建日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 创建一个 SimpleDateFormat 实例，指定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 存储中间日期的列表
        List<Login> logins = userMapper.getUserVisitbyDate(startDate, endDate);
        List<String> daylist = new ArrayList<>();
        List<DayUserVisit> DayUserVisits = new ArrayList<>();
        HashMap<String, Collection<Integer>> day2userl = new HashMap<>(); // 日期查询当天有多少人的字典
        LocalDate currentDate = start;
        // 将初始日到结束日全都放进字典
        while (!currentDate.isAfter(end)) {
            String tempday = currentDate.format(formatter);
            day2userl.put(tempday,new HashSet<>());
            daylist.add(tempday);
            currentDate = currentDate.plusDays(1);
        }

        for (Login login:logins){
            // 使用 SimpleDateFormat 格式化时间戳
            String tempday = sdf.format(new Date(login.getTrigger_timestamp() * 1000L)); // 乘以1000将秒转换为毫秒
            //if (!day2userl.containsKey(tempday)){
            //
            //    day2userl.put(tempday,new HashSet<>());
            //}
            day2userl.get(tempday).add(login.getUserid());
        }
        for (String day:daylist) {
            int UserNum = day2userl.get(day).size();
            DayUserVisits.add(new DayUserVisit(day,UserNum));
        }
        return DayUserVisits;
    }

    @Override
    @Transactional
    public int getVisitbyDate(Long startDate, Long endDate){
        List<Login> logins = userMapper.getUserVisitbyDate(startDate, endDate);
        return logins.size();
    }

    @Override
    @Transactional
    public int getNewUserbyDate(String startday, String endday){
        // 创建SimpleDateFormat对象，指定日期格式
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // String startday =  sdf.format(new Date(timestamp));
        List<User> users = userMapper.getNewUserbyDate(startday, endday);
        return users.size();
    }

    @Override
    @Transactional
    public Login templogin(String code){
        User latestUser = userMapper.getLatestUser();
        int userid = latestUser.getId();
        long currentTimestamp = Instant.now().getEpochSecond();
        Login res = new Login(userid,currentTimestamp);
        userMapper.insertlogin(res);
        String username = latestUser.getName();
        String username_english = username;
        if (containsChinese(username)){ // 中文转拼音
            username_english=convertToPinyin(username_english);
        }
        int loginid = userMapper.getlogin(res).get(0);
        return new Login(loginid, userid, username, username_english);
    }

    @Override
    @Transactional
    public Login verifylogin(String code) {
        // 验证这个邀请码是谁的, 是否还在有效期内
        InvitationCode invitation = userMapper.findCode(code);
        if (invitation==null){
           return null;
        }
        // System.out.println(invitation);
        //invitation.getStartDate().atStartOfDay().to
        //invitation.getEndDate()
        LocalDateTime localDateTimeStart = LocalDateTime.of(invitation.getStartDate(), invitation.getStartTime());
        LocalDateTime localDateTimeEnd = LocalDateTime.of(invitation.getEndDate(), invitation.getStartTime());
        long timestampStart = localDateTimeStart.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
        long timestampEnd = localDateTimeEnd.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
        long currentTimestamp = Instant.now().getEpochSecond();
        if (currentTimestamp>timestampStart && currentTimestamp<timestampEnd){
            // 还未过期,允许登录
            int userid = invitation.getuserid();
            Login res = new Login(userid,currentTimestamp);
            userMapper.insertlogin(res);
            int loginid = res.getLoginid();
            // System.out.println(loginid);
            String username = userMapper.findNamebyId(userid);
            String username_english = username;
            if (containsChinese(username)){ // 中文转拼音
                username_english=convertToPinyin(username_english);
            }
            // int loginid = userMapper.getlogin(res).get(0);
            return new Login(loginid, userid, username, username_english);
        }
        else if (currentTimestamp<timestampStart){
            return new Login(-1,currentTimestamp); // 验证码的访问时间还没到
        }
        else{
            return new Login(-2,currentTimestamp); // 验证码的访问时间已经过期
        }
    }

    @Override
    @Transactional
    public int alive(int loginid, String alive_timestamp) {
        int userid = userMapper.getUseridByloginid(loginid);
        int newloginid = userMapper.getUserLatestloginid(userid);
        // 应用端传过来的时间戳是String格式的,要转成long
        userMapper.updatealive(loginid,Long.parseLong(alive_timestamp));
        return newloginid;
    }

    @Override
    @Transactional
    public String getLanguage() throws IOException {
        // JSON 文件路径
        String filePath = "./LanguageData.json";
        // 创建 ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper();

        // 从文件中读取 JSON 数据并将其解析为 Object（通常是 Map 或 List）
        Object jsonData = objectMapper.readValue(new File(filePath), Object.class);
        // 将 JSON 数据原封不动地输出
        return objectMapper.writeValueAsString(jsonData);
    }

    @Override
    @Transactional
    public void createUser(User user) {
        userMapper.insert(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
            userMapper.update(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        // System.out.println("delete user"+id);
        userMapper.delete(id);
    }

}