package com.example.admin.entity.Resource;

public class DM {
    private String url;//某张DM图片的URL

    private String prodid; // DM的产品ID

    private int file_no; // 某张DM图片在应用端存储的文件顺序,即应用端资源里存储第几个,不是真实展示的顺序

    private int download; // 某张DM图片的版本号

    private int real_no; // 某张DM图片的真实展示文件顺序,当图片顺序调换而没有插入新图片时,则只要更新real_no就可以了

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public int getFile_no() {
        return file_no;
    }

    public void setFile_no(int file_no) {
        this.file_no = file_no;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getReal_no() {
        return real_no;
    }

    public void setReal_no(int real_no) {
        this.real_no = real_no;
    }
}
