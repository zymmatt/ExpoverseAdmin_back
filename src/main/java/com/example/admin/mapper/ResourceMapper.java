package com.example.admin.mapper;

import com.example.admin.entity.Resource.*;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceMapper {

    List<String> getallExhbid();

    List<Product> getallProduct();

    List<ExhbMovie> getallExhbMovie();

    List<DM> getallDM();

    List<ProdMovie> getallProdMovie();

    List<Product> getProdListbyExhbid(String exhbid);

    List<DM> getDMURLbyProdid(String prodid);

    // 删除某个product下已有的所有URL,之后要插入新的
    void deleteurlbyprodid(String prodid);

    // 向某个product下插入新的url
    void inserturlbyprodid(URL url);

    List<ExhbMovie> getExhbMovieURLbyExhbid(String exhbid);

    List<ProdMovie> getProdMovieURLbyExhbid(String exhbid);

    void deleteurlbyexhbmovie(String exhbid);

    void inserturlbyexhbmovie(URL url);

    void deleteurlbyprodmovie(String exhbid);

    void inserturlbyprodmovie(URL url);

}





















