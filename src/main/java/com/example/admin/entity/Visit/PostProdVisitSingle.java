package com.example.admin.entity.Visit;

// 在一次产品观看记录中的单个产品数据
public class PostProdVisitSingle {
    private String prodid; // 产品ID
    private int duration; // 这个产品的观看时长

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
