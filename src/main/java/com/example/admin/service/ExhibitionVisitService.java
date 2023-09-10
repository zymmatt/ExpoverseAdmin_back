package com.example.admin.service;
import com.example.admin.entity.Visit.Exhibition_data;
import com.example.admin.entity.Visit.ExhibitionVisit;
import java.util.List;
public interface ExhibitionVisitService {
    void createData(ExhibitionVisit exhibitionVisit);
    List<Exhibition_data> getDatabyDate(Long startDate, Long endDate);
    void downloadDataExcel();
}
