package com.example.admin.entity;
import java.util.Date;


public class User {
    private int id;    //访客唯一id
    private String name;    //姓名
    private String company; //公司名称
    private String job; //职务
    private String mail;   //邮箱
    private String phone;   //联系电话
    private String approval_date; //添加日期
    private String approver_id;   //审批人iD
    private String secret_key; // UUID

    public int getId() {
        return id;
    }

    public String getJob() {
        return job;
    }

    public String getPhone() {
        return phone;
    }

    public String getmail() {
        return mail;
    }

    public String getApproval_date() {
        return approval_date;
    }

    public String getApprover_id() {
        return approver_id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }


    // getters, setters, constructors...
}

