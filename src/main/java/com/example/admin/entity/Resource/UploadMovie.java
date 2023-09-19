package com.example.admin.entity.Resource;

public class UploadMovie
{
    private String src;
    //某张图片的src  https://expoverseazureblobdb.blob.core.windows.net/test-ctn1/InteTraffic_AIMobile_1.jpg?
    // sv=2023-01-03&st=2023-09-13T10%3A45%3A59Z&se=2023-09-14T10%3A50%3A59Z&sr=c&sp=r&sig=IxDmwZvqzvFychMw1pu65TJOSjCCFj2%2B1xbGNW8sY%2Bo%3D
    private int id;  //某张图片的顺序id
    private String name;
    //private MultipartFile file;//文件
    //某张图片的名字
    // https://expoverseazureblobdb.blob.core.windows.net/test-ctn1/InteTraffic_AIMobile_1.jpg

    public void setsrc(String src) { this.src = src; }

    public String getsrc() { return src; }

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

}
