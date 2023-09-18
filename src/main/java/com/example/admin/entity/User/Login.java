package com.example.admin.entity.User;

public class Login {
    private int userid;
    private int loginid; // 登录ID
    private Long trigger_timestamp; // 登录的时间戳

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return userid;
    }

    public int getLoginid() {
        return loginid;
    }

    public void setLoginid(int loginid) {
        this.loginid = loginid;
    }

    public Long getTimestamp() {
        return trigger_timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.trigger_timestamp = timestamp;
    }

    public Login(int userid, Long trigger_timestamp){
        this.userid = userid;
        this.trigger_timestamp = trigger_timestamp;
    }

    public Login(int loginid, int userid){
        this.userid = userid;
        this.loginid = loginid;
    }
}
