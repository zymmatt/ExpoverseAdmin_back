// UserServiceImpl.java
package com.example.admin.service.impl;

import com.example.admin.entity.User.*;
import com.example.admin.service.UserService;
import com.example.admin.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    public Login verifylogin(String code) {
        // 验证这个邀请码是谁的, 是否还在有效期内
        InvitationCode invitation = userMapper.findCode(code);
        System.out.println(invitation);
        //invitation.getStartDate().atStartOfDay().to
        //invitation.getEndDate()
        LocalDateTime localDateTimeStart = LocalDateTime.of(invitation.getStartDate(), invitation.getStartTime());
        LocalDateTime localDateTimeEnd = LocalDateTime.of(invitation.getEndDate(), invitation.getStartTime());
        long timestampStart = localDateTimeStart.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
        long timestampEnd = localDateTimeEnd.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
        long currentTimestamp = Instant.now().getEpochSecond();
        if (currentTimestamp>timestampStart && currentTimestamp<timestampEnd){
            System.out.println(timestampStart);
            System.out.println(timestampEnd);
            System.out.println(currentTimestamp);
            // 还未过期,允许登录
            int userid = invitation.getuserid();
            Login res = new Login(userid,currentTimestamp);
            userMapper.insertlogin(res);
            String username = userMapper.findNamebyId(userid);
            String username_english = username;
            if (containsChinese(username)){ // 中文转拼音
                username_english=convertToPinyin(username_english);
            }
            int loginid = userMapper.getlogin(res).get(0);
            return new Login(loginid, userid, username, username_english);
        }
        return null;
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