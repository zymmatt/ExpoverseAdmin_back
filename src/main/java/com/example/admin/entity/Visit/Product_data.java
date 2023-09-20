package com.example.admin.entity.Visit;

public class Product_data {
    private String prodid; //产品id

    private String prodname; //产品名称

    private int visitNum;//参观人次

    private int visitUser;//参观人数

    private int visitTime;//参观时长

    public Product_data(String prodid, String prodname, int visitNum, int visitUser, int visitTime){
        this.prodid = prodid;
        this.prodname = prodname;
        this.visitNum = visitNum;
        this.visitUser = visitUser;
        this.visitTime = visitTime;
    }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public void setVisitUser(int visitUser) {
        this.visitUser = visitUser;
    }

    public int getVisitUser() {
        return visitUser;
    }

    public void setVisitTime(int visitTime) {
        this.visitTime = visitTime;
    }

    public int getVisitTime() {
        return visitTime;
    }

    public int getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(int visitNum) {
        this.visitNum = visitNum;
    }

}
