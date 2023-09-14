package com.example.admin.mapper;

import com.example.admin.entity.Resource.*;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceMapper {
    List<Product> getProdListbyExhbid(String exhbid);

    List<DM> getDMURLbyProdid(String prodid);

    // 删除某个product下已有的所有URL,之后要插入新的
    void deleteurlbyprodid(String prodid);

    // 向某个product下插入新的url
    void inserturlbyprodid(URL url);
}





















