package com.example.admin.service;
import com.example.admin.entity.Visit.*;
import java.util.List;
public interface ExhibitionVisitService {
    void createDataExhb(ExhibitionVisit exhibitionVisit);
    void createDataProd(ProductVisit productVisit);
    List<Exhibition_data> getDatabyDate(Long startDate, Long endDate);
    void downloadDataExcel();
}
