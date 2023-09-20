package com.example.admin.entity.Visit;

import java.util.ArrayList;
import java.util.List;

// 展区参观行为统计的单个结果
public class Exhibition_data {

    private String exhibition_id; //展区id

    private int visitNum;//参观人次

    private int visitUser;//参观人数

    private int visitTime;//参观时长

    private List<Product_data> product_dataList;

    public Exhibition_data(String exhbid, int visitNum, int visitUser,
                           int visitTime){
        this.exhibition_id=exhbid;
        this.visitNum = visitNum;
        this.visitUser = visitUser;
        this.visitTime = visitTime;
        this.product_dataList = new ArrayList<>();
    }

    public int getVisitNum() { return visitNum; }

    public void setVisitNum(int visitNum) {
        this.visitNum = visitNum;
    }

    public int getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(int visitTime) {
        this.visitTime = visitTime;
    }

    public int getVisitUser() {
        return visitUser;
    }

    public void setVisitUser(int visitUser) {
        this.visitUser = visitUser;
    }

    public String getExhibition_id() {
        return exhibition_id;
    }

    public void setExhibition_id(String exhibition_id) {
        this.exhibition_id = exhibition_id;
    }

    public List<Product_data> getProduct_dataList() {
        return product_dataList;
    }

    public void setProduct_dataList(List<Product_data> product_dataList) {
        this.product_dataList = product_dataList;
    }

}
