package com.example.admin.entity.Resource;

public class ExhbMovie {
    private String url;//某张图片的URL

    public String getUrl() { return url; }

    @Override
    public String toString() {
        return "ExhbMovie{" +
                "url='" + url + '\'' +
                '}';
    }

    public void setUrl(String url) { this.url = url; }
}
