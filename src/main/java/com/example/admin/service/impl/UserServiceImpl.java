// UserServiceImpl.java
package com.example.admin.service.impl;

import com.example.admin.entity.User.*;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.example.admin.utils.pinyin.containsChinese;
import static com.example.admin.utils.pinyin.convertToPinyin;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public List<User> findAll() {
        List<User>users = userMapper.findAll();
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
            System.out.println(loginid);
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
        System.out.println("delete user"+id);
        userMapper.delete(id);
    }


}