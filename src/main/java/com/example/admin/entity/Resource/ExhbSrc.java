package com.example.admin.entity.Resource;

import java.util.List;

public class ExhbSrc {
    private String exhibition_id;
    private List<ExhbMovie> exhibition_movie_url;
    private List<ProdSrc> prodSrcList;
    private String SAS; //访问密钥

    public String getSAS() {
        return SAS;
    }

    public void setSAS(String SAS) {
        this.SAS = SAS;
    }

    public String getExhibition_id() {
        return exhibition_id;
    }

    public void setExhibition_id(String exhibition_id) {
        this.exhibition_id = exhibition_id;
    }

    public List<ProdSrc> getProdSrcList() {
        return prodSrcList;
    }

    public void setProdSrcList(List<ProdSrc> prodSrcList) {
        this.prodSrcList = prodSrcList;
    }

    public void setExhibition_movie_url(List<ExhbMovie> exhibition_movie_url) {
        this.exhibition_movie_url = exhibition_movie_url;
    }

    public List<ExhbMovie> getExhibition_movie_url() {
        return exhibition_movie_url;
    }

    public ExhbSrc(String exhibition_id, List<ExhbMovie> exhibition_movie_url,
                   List<ProdSrc> prodSrcList){
        this.exhibition_id=exhibition_id;
        this.exhibition_movie_url = exhibition_movie_url;
        this.prodSrcList = prodSrcList;
    }

}
