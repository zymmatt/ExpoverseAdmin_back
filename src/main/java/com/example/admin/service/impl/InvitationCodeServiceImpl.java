package com.example.admin.service.impl;

import com.example.admin.entity.scheduled_invitation;
import com.example.admin.entity.InvitationCode;
import com.example.admin.service.InvitationCodeService;
import com.example.admin.mapper.InvitationCodeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class InvitationCodeServiceImpl implements InvitationCodeService{
    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    // 随机的生成UUID作为激活码,去表中验证有没有重复,
    // 若没有则发出激活码,否则重新生成
    @Override
    public String generateCodeID() {
        String uuid = null;
        boolean isUnique = false;
        do {
            uuid = UUID.randomUUID().toString();
            List<InvitationCode>codes = invitationCodeMapper.findCodebyId(uuid);
            if (codes.size()==0) {
                isUnique = true;
            }
        } while (!isUnique);
        return uuid;
    }

    //  获得两个日期之间的日期, 全是字符串格式
    public static List<String> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (!startDate.isAfter(endDate)) {
            dates.add(startDate.format(formatter));
            startDate = startDate.plusDays(1);
        }
        return dates;
    }

    /*
    * 把每一个请求记录中的起始日期到终止日期之间的日子放进字典中,
    * 字典中已经有的日子要把占据座位变量增加,最后字典中的所有日子和对应
    * 的占据座位量放入到scheduled Invitation中,列表返回
    *
    */
    @Override
    public List<scheduled_invitation> getScheduledDay() {
        List<InvitationCode>invitations = invitationCodeMapper.getAllInvitation();
        List<scheduled_invitation> schedule=new ArrayList<>();
        Map<String, Integer> scheduleDict = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (InvitationCode single:invitations){
            LocalDate startDate=single.getStartDate();
            LocalDate endDate=single.getEndDate();
            while (!startDate.isAfter(endDate)) {
                String tempday = startDate.format(formatter);
                if (scheduleDict.containsKey(tempday)){
                    scheduleDict.put(tempday, scheduleDict.get(tempday)+1);
                }
                else{
                    scheduleDict.put(tempday, 1);
                }
                startDate = startDate.plusDays(1);
            }
        }
        for (Map.Entry<String, Integer> entry: scheduleDict.entrySet()) {
            scheduled_invitation temp_day = new scheduled_invitation();
            temp_day.setCurrentDate(entry.getKey());
            temp_day.setCurrentTime("");
            temp_day.setOccupied_seats(entry.getValue());
            schedule.add(temp_day);
        }
        return schedule;
    }

    @Override
    public void createInvitation(InvitationCode invitation) {
        invitationCodeMapper.insertInvitationCode(invitation);
    }

    @Override
    public List<String> getAllCodeID() {
        //InvitationCodeMapper
        return null;
    }
}
