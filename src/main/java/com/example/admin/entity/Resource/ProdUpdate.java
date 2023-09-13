package com.example.admin.entity.Resource;

import java.util.List;

public class ProdUpdate {
    private String exhbid; // 展区ID
    private String prodid; // 产品ID
    private List<UploadDM> uploadDMlist; // 上传的DM列表

    public List<UploadDM> getUploadDMlist() { return uploadDMlist; }

    public String getExhbid() { return exhbid; }

    public String getProdid() { return prodid; }

    public void setExhbid(String exhbid) { this.exhbid = exhbid; }

    public void setProdid(String prodid) { this.prodid = prodid; }

    public void setUploadDMlist(List<UploadDM> uploadDMlist) { this.uploadDMlist = uploadDMlist; }

    @Override
    public String toString() {
        return "ProdUpdate{" +
                "exhbid='" + exhbid + '\'' +
                ", prodid='" + prodid + '\'' +
                ", uploadDMlist=" + uploadDMlist +
                '}';
    }
}
