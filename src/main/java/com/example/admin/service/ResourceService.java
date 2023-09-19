package com.example.admin.service;

import com.example.admin.entity.Resource.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;



public interface ResourceService {

    List<ExhbSrc> getAllResource();

    List<Product> getProdListbyExhbid(String exhbid);

    List<DM> getDMURLbyProdid(String prodid);

    List<ExhbMovie> getExhbMovieURLbyExhbid(String exhbid);

    List<ProdMovie> getProdMovieURLbyExhbid(String exhbid);

    String gettempSAS();

    void uploadDMdict(ProdUpdate prodUpdate) throws IOException;

    String updateExhbMovie(MultipartFile file, String exhbid, String name) throws IOException;

    String updateProdMovie(MultipartFile file, String exhbid, String name) throws IOException;
}
