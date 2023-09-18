package com.example.admin.entity.Visit;

// 产品参观行为记录
public class ProductVisit {
    private int loginid; // 登录id

    private int userid; // 用户id

    private String prodid; // 产品id

    private int duration; // 观看时长 秒为单位

    private Long trigger_timestamp; // 参观动作的触发时间

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setLoginid(int loginid) {
        this.loginid = loginid;
    }

    public int getLoginid() {
        return loginid;
    }

    public void setTrigger_timestamp(Long trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public int getDuration() {
        return duration;
    }

    public String getProdid() {
        return prodid;
    }

    public Long getTrigger_timestamp() {
        return trigger_timestamp;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
