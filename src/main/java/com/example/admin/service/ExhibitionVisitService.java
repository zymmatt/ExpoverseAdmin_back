package com.example.admin.service;
import com.example.admin.entity.Visit.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
public interface ExhibitionVisitService {
    void createDataExhb(PostExhbVisit postExhbVisit);
    void createDataProd(PostProdVisit postProdVisit);
    List<Exhibition_data> getDatabyDate(Long startDate, Long endDate);
    // void downloadvisitTimeDataExcel(HttpServletResponse response) throws IOException;
    String downloadvisitTimeDataExcel() throws IOException;

    // void downloadvisitNumDataExcel(HttpServletResponse response) throws IOException;
    String downloadvisitNumDataExcel() throws IOException;
}
