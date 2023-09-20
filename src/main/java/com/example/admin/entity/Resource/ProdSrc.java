package com.example.admin.entity.Resource;

import java.util.List;

public class ProdSrc {
    private String prodid;
    private List<DM> DMlist;
    private List<ProdMovie> movielist;

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public List<DM> getDMlist() {
        return DMlist;
    }

    public void setDMlist(List<DM> DMlist) {
        this.DMlist = DMlist;
    }

    public List<ProdMovie> getMovielist() {
        return movielist;
    }

    public void setMovielist(List<ProdMovie> movielist) {
        this.movielist = movielist;
    }

    public ProdSrc(String prodid, List<DM> DMlist, List<ProdMovie> movielist){
        this.prodid = prodid;
        this.DMlist = DMlist;
        this.movielist = movielist;
    }


}
