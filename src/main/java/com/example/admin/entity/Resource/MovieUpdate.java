package com.example.admin.entity.Resource;

import java.util.List;

public class MovieUpdate {
    private String exhbid; // 展区ID
    private String prodid; // 产品ID
    private List<UploadMovie> uploadMovielist; // 上传的电影列表
    public String getExhbid() { return exhbid; }

    public String getProdid() { return prodid; }

    public void setExhbid(String exhbid) { this.exhbid = exhbid; }

    public void setProdid(String prodid) { this.prodid = prodid; }

    public List<UploadMovie> getUploadMovielist() {
        return uploadMovielist;
    }

    public void setUploadMovielist(List<UploadMovie> uploadMovielist) {
        this.uploadMovielist = uploadMovielist;
    }
}
