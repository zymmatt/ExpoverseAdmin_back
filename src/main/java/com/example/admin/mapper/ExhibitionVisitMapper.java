package com.example.admin.mapper;
import com.example.admin.entity.Visit.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ExhibitionVisitMapper {

    void insertExhibitionVisit(ExhibitionVisit exhibitionVisit);
    void insertProductVisit(ProductVisit productVisit);
    List<ExhibitionVisit> ExhbgetDatabyDate(Long startTime, Long endTime);
    List<ProductVisit> ProdgetDatabyDate(Long startTime, Long endTime);
    List<String> getExhibitionList();
    List<exhb2prod> getexhb2prod();

}
