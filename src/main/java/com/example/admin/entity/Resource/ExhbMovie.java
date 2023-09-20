package com.example.admin.entity.Resource;

public class ExhbMovie {
    private String url;//某张图片的URL
    private String exhibition_id;
    private int file_no;
    private int download;

    @Override
    public String toString() {
        return "ExhbMovie{" +
                "url='" + url + '\'' +
                '}';
    }
    public ExhbMovie(String url){
        this.url=url;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

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

    public String getExhibition_id() {
        return exhibition_id;
    }

    public void setExhibition_id(String exhibition_id) {
        this.exhibition_id = exhibition_id;
    }
}
