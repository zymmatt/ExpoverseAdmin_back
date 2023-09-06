package com.example.admin.mapper;
import com.example.admin.entity.ExhibitionVisit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ExhibitionVisitMapper {
    void insertExhibitionVisit(ExhibitionVisit exhibitionVisit);
    List<ExhibitionVisit> getDatabyDate(String startTime, String endTime);
}
