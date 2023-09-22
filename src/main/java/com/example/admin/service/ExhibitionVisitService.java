package com.example.admin.service;
import com.example.admin.entity.Visit.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
public interface ExhibitionVisitService {
    void createDataExhb(ExhibitionVisit exhibitionVisit);
    void createDataProd(ProductVisit productVisit);
    List<Exhibition_data> getDatabyDate(Long startDate, Long endDate);
    void downloadvisitTimeDataExcel(HttpServletResponse response) throws IOException;
    void downloadvisitNumDataExcel(HttpServletResponse response) throws IOException;
}
