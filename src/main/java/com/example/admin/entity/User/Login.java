package com.example.admin.entity.User;

public class Login {
    private int userid;
    private String username;
    private String username_english;
    private int loginid; // 登录ID
    private Long trigger_timestamp; // 登录的时间戳
    private Long alive_timestamp; // 心跳时间戳, 用于监控存活时长
    private boolean filledsurvey; // 是否在一周内填写了调查问卷

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
        this.alive_timestamp = trigger_timestamp+10; //登录时假定最少呆了10秒
    }

    public Login(int loginid, int userid, String username,
                 String username_english){
        this.userid = userid;
        this.loginid = loginid;
        this.username = username;
        this.username_english = username_english;
    }

    public Login(int loginid, int userid, String username,
                 String username_english, boolean filledsurvey){
        this.userid = userid;
        this.loginid = loginid;
        this.username = username;
        this.username_english = username_english;
        this.filledsurvey = filledsurvey;
    }

    public void setTrigger_timestamp(Long trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }

    public Long getTrigger_timestamp() {
        return trigger_timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getUsername_english() {
        return username_english;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsername_english(String username_english) {
        this.username_english = username_english;
    }

    public Long getAlive_timestamp() {
        return alive_timestamp;
    }

    public void setAlive_timestamp(Long alive_timestamp) {
        this.alive_timestamp = alive_timestamp;
    }

    public boolean isFilledsurvey() {
        return filledsurvey;
    }

    public void setFilledsurvey(boolean filledsurvey) {
        this.filledsurvey = filledsurvey;
    }

    @Override
    public String toString() {
        return "Login{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", username_english='" + username_english + '\'' +
                ", loginid=" + loginid +
                ", trigger_timestamp=" + trigger_timestamp +
                ", alive_timestamp=" + alive_timestamp +
                ", filledsurvey=" + filledsurvey +
                '}';
    }
}
