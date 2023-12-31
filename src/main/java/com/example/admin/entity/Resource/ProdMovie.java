package com.example.admin.entity.Resource;

public class ProdMovie {
    private String url;//某张图片的URL

    private String prodid;

    private int file_no;

    private int download;

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getFile_no() {
        return file_no;
    }

    public void setFile_no(int file_no) {
        this.file_no = file_no;
    }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    @Override
    public String toString() {
        return "ProdMovie{" +
                "url='" + url + '\'' +
                ", prodid='" + prodid + '\'' +
                ", file_no=" + file_no +
                ", download=" + download +
                '}';
    }
}
