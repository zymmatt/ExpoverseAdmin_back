package com.example.admin.entity.User;
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

    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApprover_id() {
        return approver_id;
    }

    public void setApprover_id(String approver_id) {
        this.approver_id = approver_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // Override equals, hashCode, and toString methods if necessary...
}