package com.example.admin.service.impl;

import com.example.admin.entity.User.scheduled_invitation;
import com.example.admin.entity.User.InvitationCode;
import com.example.admin.service.InvitationCodeService;
import com.example.admin.mapper.InvitationCodeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class InvitationCodeServiceImpl implements InvitationCodeService{
    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    // 随机的生成UUID作为激活码,去表中验证有没有重复,
    // 若没有则发出激活码,否则重新生成
    @Override
    @Transactional
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
     * 精细到整点小时的单位
     */
    @Override
    @Transactional
    public List<scheduled_invitation> getScheduledHour() {
        List<InvitationCode>invitations = invitationCodeMapper.getAllInvitation();
        List<scheduled_invitation> schedule=new ArrayList<>();
        Map<String, Integer> scheduleDict = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        for (InvitationCode single:invitations){
            LocalTime starttime = single.getStartTime();
            LocalDate startDate=single.getStartDate();
            LocalDate endDate=single.getEndDate();
            LocalDateTime startDT = startDate.atTime(starttime);
            LocalDateTime endDT = endDate.atTime(starttime);
            while (startDT.compareTo(endDT)<0) {
                String tempDT = startDT.format(formatter);
                if (scheduleDict.containsKey(tempDT)){
                    scheduleDict.put(tempDT, scheduleDict.get(tempDT)+1);
                }
                else{
                    scheduleDict.put(tempDT, 1);
                }
                startDT = startDT.plusHours(1);
            }
        }
        for (Map.Entry<String, Integer> entry: scheduleDict.entrySet()) {
            scheduled_invitation temp_day = new scheduled_invitation();
            String[] parts = entry.getKey().split(" ");
            temp_day.setCurrentDate(parts[0]);
            temp_day.setCurrentTime(parts[1]);
            temp_day.setOccupied_seats(entry.getValue());
            schedule.add(temp_day);
        }
        return schedule;
    }


    /*
     * 把每一个请求记录中的起始日期到终止日期之间的小时放进字典中,
     * 字典中已经有的小时要把占据座位变量增加,
     * 每一个日子的占据座位数取每一小时的占据座位的最大值,
     * 最后字典中的所有日子和对应
     * 的占据座位量放入到scheduled Invitation中,列表返回
     * yyyy-MM-dd
     *      yyyy-MM-dd HH
     *              已经占据的座位数
     */
    @Override
    @Transactional
    public List<scheduled_invitation> getScheduledDay() {
        List<InvitationCode>invitations = invitationCodeMapper.getAllInvitation();
        List<scheduled_invitation> schedule=new ArrayList<>();
        Map<String, Map<String, Integer>> scheduleDict = new HashMap<>();
        DateTimeFormatter dayformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hourformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        for (InvitationCode single:invitations){
            LocalTime starttime = single.getStartTime();
            LocalDate startDate=single.getStartDate();
            LocalDate endDate=single.getEndDate();
            LocalDateTime startDT = startDate.atTime(starttime);
            LocalDateTime endDT = endDate.atTime(starttime);
            while (startDT.compareTo(endDT)<0) {
                String tempday = startDT.format(dayformatter);
                String tempDT = startDT.format(hourformatter);
                // 如果某一天是头一回被加入字典
                if (!scheduleDict.containsKey(tempday)){
                    Map<String, Integer> hourDict = new HashMap<>();
                    scheduleDict.put(tempday, hourDict);
                }
                // 在某一天下添加新的小时-占据座位数
                if (scheduleDict.get(tempday).containsKey(tempDT)){
                    scheduleDict.get(tempday).put(tempDT, scheduleDict.get(tempday).get(tempDT)+1);
                }
                else{
                    scheduleDict.get(tempday).put(tempDT, 1);
                }
                startDT = startDT.plusHours(1);
            }
        }
        for (String tempday : scheduleDict.keySet()) {
            // System.out.println(scheduleDict.get(tempday).values().toString());
            int maxValue = Collections.max(scheduleDict.get(tempday).values());
            // System.out.println(tempday);
            // System.out.println(maxValue);
            // System.out.println("day: " + tempday + ", Value: " + maxValue);
            scheduled_invitation temp_day = new scheduled_invitation();
            temp_day.setCurrentDate(tempday);
            temp_day.setCurrentTime("");
            temp_day.setOccupied_seats(maxValue);
            schedule.add(temp_day);
        }
        /*
        for (Map.Entry<String, Integer> entry: scheduleDict.entrySet()) {
            scheduled_invitation temp_day = new scheduled_invitation();
            temp_day.setCurrentDate(entry.getKey());
            temp_day.setCurrentTime("");
            temp_day.setOccupied_seats(entry.getValue());
            schedule.add(temp_day);
        }
        */
        return schedule;
    }

    @Override
    @Transactional
    public void createInvitation(InvitationCode invitation) {
        LocalTime originalTime = invitation.getStartTime();
        // 将LocalTime减去8小时
        invitation.setStartTime(originalTime.minusHours(8));
        invitationCodeMapper.insertInvitationCode(invitation);
    }

    @Override
    @Transactional
    public List<InvitationCode> getAllInvitation() {
        return invitationCodeMapper.getAllInvitation();
    }
}



