package com.example.admin.entity;

// 展区参观行为统计的单个结果
public class Exhibition_data {
    private String exhibition_id; //展区id

    private int visitNum=0;//参观人次

    private int visitUser=0;//参观人数

    private int visitTime=0;//参观时长

    public int getVisitNum() {
        return visitNum;
    }

    public int getVisitTime() {
        return visitTime;
    }

    public int getVisitUser() {
        return visitUser;
    }

    public String getExhibition_id() {
        return exhibition_id;
    }

    public void setVisitNum(int visitNum) {
        this.visitNum = visitNum;
    }

    public void setVisitTime(int visitTime) {
        this.visitTime = visitTime;
    }

    public void setVisitUser(int visitUser) {
        this.visitUser = visitUser;
    }

    public void setExhibition_id(String exhibition_id) {
        this.exhibition_id = exhibition_id;
    }
}
