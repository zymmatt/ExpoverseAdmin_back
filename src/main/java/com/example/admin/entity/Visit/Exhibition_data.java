package com.example.admin.entity.Visit;

// 展区参观行为统计的单个结果
public class Exhibition_data {

    private String exhibition_id; //展区id

    private int visitNum;//参观人次

    private int visitUser;//参观人数

    private int visitTime;//参观时长


    public Exhibition_data(String exhbid, int visitNum, int visitUser, int visitTime){
        this.exhibition_id=exhbid;
        this.visitNum = visitNum;
        this.visitUser = visitUser;
        this.visitTime = visitTime;
    }

    public int getVisitNum() { return visitNum; }

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
