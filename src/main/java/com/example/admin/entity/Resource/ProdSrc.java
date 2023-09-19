package com.example.admin.entity.Resource;

import java.util.List;

public class ProdSrc {
    private String prodid;
    private List<DM> DMlist;

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public String getProdid() {
        return prodid;
    }

    public List<DM> getDMlist() {
        return DMlist;
    }

    public void setDMlist(List<DM> DMlist) {
        this.DMlist = DMlist;
    }

    public ProdSrc(String prodid, List<DM> DMlist){
        this.prodid = prodid;
        this.DMlist = DMlist;
    }
}
