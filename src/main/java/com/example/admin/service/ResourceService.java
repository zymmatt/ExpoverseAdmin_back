package com.example.admin.service;

import com.example.admin.entity.Resource.*;

import java.io.IOException;
import java.util.List;



public interface ResourceService {

    public List<Product> getAllProduct();

    List<Product> getProdListbyExhbid(String exhbid);

    List<DM> getDMURLbyProdid(String prodid);

    String gettempSAS();

    void uploadDMdict(ProdUpdate prodUpdate) throws IOException;
}
