package com.example.admin.service;

import com.example.admin.entity.Resource.*;

import java.util.List;



public interface ResourceService {

    public List<product> getAllProduct();

    List<product> getProdListbyExhbid(String exhbid);

    List<DM> getDMURLbyProdid(String prodid);

    String gettempSAS();
}
