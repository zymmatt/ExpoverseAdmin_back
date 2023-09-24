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
    List<Exhibition> getAllExhibition(); // 获取所有的展区, 要按照展区编号排序
    List<String> getExhibitionList(); // 获取所有的展区ID
    List<exhb2prod> getexhb2prod();
    List<ExhibitionVisit> getexhbvisitbyexhbid(String exhbid); //根据展区ID寻找展区参观记录
    List<ExhibitionVisit> getexhbvisitbyloginid(String exhbid, int loginid);
}

