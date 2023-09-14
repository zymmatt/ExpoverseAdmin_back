package com.example.admin.entity.Resource;

public class URL {
    private String file_url;
    private String file_type;
    private String product_id;
    private int file_no;

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public int getFile_no() {
        return file_no;
    }

    public String getFile_type() {
        return file_type;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_no(int file_no) {
        this.file_no = file_no;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public URL(String file_url, String file_type, String product_id, int file_no){
        this.file_url = file_url;
        this.file_type = file_type;
        this.product_id = product_id;
        this.file_no = file_no;
    }

}
