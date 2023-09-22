package com.example.admin.entity.Visit;

import java.util.List;

public class PostProdVisit {
    private int loginid; //登录ID

    private int userid; // 用户id

    private String trigger_timestamp; // 参观动作的触发时间

    private List<PostProdVisitSingle> product; // 一个或多个产品的记录都在里面

    public int getLoginid() {
        return loginid;
    }

    public void setLoginid(int loginid) {
        this.loginid = loginid;
    }

    public String getTrigger_timestamp() {
        return trigger_timestamp;
    }

    public void setTrigger_timestamp(String trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public List<PostProdVisitSingle> getProduct() {
        return product;
    }

    public void setProduct(List<PostProdVisitSingle> product) {
        this.product = product;
    }
}
