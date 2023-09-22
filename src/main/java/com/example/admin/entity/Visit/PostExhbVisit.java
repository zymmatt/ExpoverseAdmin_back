package com.example.admin.entity.Visit;

public class PostExhbVisit {
    private int loginid; // 登录id

    private int userid; // 用户id

    private String exhibition_id; //展区id

    private boolean enter; // 进入还是离开, 默认是进入

    private String trigger_timestamp; // 参观动作的触发时间

    public int getUserid() { return userid; }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setLoginid(int loginid) {
        this.loginid = loginid;
    }

    public int getLoginid() {
        return loginid;
    }

    public void setTrigger_timestamp(String trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }

    public void setExhibition_id(String exhibition_id) {
        this.exhibition_id = exhibition_id;
    }

    public String getExhibition_id() {
        return exhibition_id;
    }

    public String getTrigger_timestamp() {
        return trigger_timestamp;
    }

    public boolean getEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }


}
