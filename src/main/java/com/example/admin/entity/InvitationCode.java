package com.example.admin.entity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


public class InvitationCode {
    private int id; //邀请记录id
    
    private String codeId; // 使用UUID作为邀请码ID
    
    private LocalDate startDate;

    private String startDateStr;

    private LocalDate endDate;

    private String endDateStr;

    private LocalTime startTime;
    
    private int userid;
    
    private String approver_id;

    public void setCodeId(){
        this.codeId=UUID.randomUUID().toString();
    } // 自动生成UUID作为邀请码ID

    public String getCodeId() {
        return codeId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setuserid(int userid) {
        this.userid = userid;
    }

    public int getuserid() {
        return userid;
    }

    public void setapprover_id(String approver_id) {
        this.approver_id = approver_id;
    }

    public String getapprover_id() {
        return approver_id;
    }

    // Override equals, hashCode, and toString methods if necessary...
}