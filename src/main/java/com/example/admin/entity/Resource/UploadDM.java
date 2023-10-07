package com.example.admin.entity.Resource;

import org.springframework.web.multipart.MultipartFile;

public class UploadDM {
    private String src;
    //某张图片的src  https://expoverseazureblobdb.blob.core.windows.net/test-ctn1/InteTraffic_AIMobile_1.jpg?
    // sv=2023-01-03&st=2023-09-13T10%3A45%3A59Z&se=2023-09-14T10%3A50%3A59Z&sr=c&sp=r&sig=IxDmwZvqzvFychMw1pu65TJOSjCCFj2%2B1xbGNW8sY%2Bo%3D
    private int real_no;  // 某张图片的真实展示顺序
    private int file_no; // 某张图片在应用端存储的顺序
    private String name;
    //private MultipartFile file;//文件
    //某张图片的名字
    // https://expoverseazureblobdb.blob.core.windows.net/test-ctn1/InteTraffic_AIMobile_1.jpg

    public void setsrc(String src) { this.src = src; }

    public String getsrc() { return src; }

    public void setReal_no(int real_no) {
        this.real_no = real_no;
    }

    public int getReal_no() {
        return real_no;
    }

    public void setFile_no(int file_no) {
        this.file_no = file_no;
    }

    public int getFile_no() {
        return file_no;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    //public MultipartFile getFile() {
    //    return file;
    //}
}





