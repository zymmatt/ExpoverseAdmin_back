package com.example.admin.mapper;

import com.example.admin.entity.Resource.*;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceMapper {
    List<product> getProdListbyExhbid(String exhbid);

    List<DM> getDMURLbyProdid(String prodid);


}





















