package com.example.admin.entity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


public class InvitationCode {
    
    private String codeId; // 使用UUID作为邀请码ID
    
    private LocalDate startDate;

    private String startDateStr;

    private LocalDate endDate;

    private String endDateStr;

    private LocalTime startTime;
    
    private Long userid;
    
    private Long approver_id;

    public InvitationCode() {
        this.codeId = UUID.randomUUID().toString(); // 自动生成UUID作为邀请码ID
    }

    // Getters, Setters, and other methods...

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

    public void setuserid(Long userid) {
        this.userid = userid;
    }

    public Long getuserid() {
        return userid;
    }

    public void setapprover_id(Long approver_id) {
        this.approver_id = approver_id;
    }

    public Long getapprover_id() {
        return approver_id;
    }

    // Override equals, hashCode, and toString methods if necessary...
}