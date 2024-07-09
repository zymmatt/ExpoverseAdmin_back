package com.example.admin.schedule;

import com.example.admin.entity.User.InvitationCode;
import com.example.admin.entity.User.User;
import com.example.admin.mapper.InvitationCodeMapper;
import com.example.admin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Component
@EnableScheduling
@EnableAsync
public class multiThreadScheduleTask {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    @Async
    @Transactional
    // @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void deleteUserRecord(){
        List<User> users = userMapper.getSurvived();
        long currentTimestamp = Instant.now().getEpochSecond();
        for (User temp:users){
            long expireTimestamp = Instant.ofEpochSecond(temp.getLast_login()).plus(Duration.ofDays(60)).getEpochSecond();
            // 若用户上次登录在60天之前，则删除用户表和邀请码表的对应用户记录
            if (expireTimestamp<=currentTimestamp){
                userMapper.setdelete(temp.getId());
                invitationCodeMapper.setdelete(temp.getId());
            }
        }
    }

}
