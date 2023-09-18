package com.example.admin.mapper;
import com.example.admin.entity.Visit.ExhibitionVisit;
import com.example.admin.entity.Visit.ProductVisit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ExhibitionVisitMapper {

    void insertExhibitionVisit(ExhibitionVisit exhibitionVisit);
    void insertProductVisit(ProductVisit productVisit);
    List<ExhibitionVisit> getDatabyDate(Long startTime, Long endTime);
    List<String> getExhibitionList();


}
