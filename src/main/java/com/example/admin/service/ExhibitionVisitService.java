package com.example.admin.service;
import com.example.admin.entity.Exhibition_data;
import com.example.admin.entity.ExhibitionVisit;
import java.util.List;
public interface ExhibitionVisitService {
    void createData(ExhibitionVisit exhibitionVisit);
    List<Exhibition_data> getDatabyDate(String startDate, String endDate);
    void downloadDataExcel();

}
