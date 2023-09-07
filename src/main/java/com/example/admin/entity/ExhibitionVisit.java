package com.example.admin.entity;
import java.time.LocalDate;
import java.time.LocalTime;

// 展区参观行为记录
public class ExhibitionVisit {
    private int id; //展区参观id

    private int userid; // 用户id

    private Long trigger_timestamp; // 参观动作的触发时间

    private String exhibition_id; //展区id

    private boolean enter; // 进入还是离开, 默认是进入

    public String getExhibition_id() {
        return exhibition_id;
    }

    public int getId() {
        return id;
    }

    public int getUserid() { return userid; }

    public Long gettrigger_timestamp() {
        return trigger_timestamp;
    }

    public void setExhibition_id(String exhibition_id) {
        this.exhibition_id = exhibition_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void settrigger_timestamp(Long trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }
}



