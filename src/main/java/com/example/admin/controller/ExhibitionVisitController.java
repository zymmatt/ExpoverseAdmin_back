package com.example.admin.controller;

import com.example.admin.entity.Exhibition_data;
import com.example.admin.entity.ExhibitionVisit;
import com.example.admin.service.ExhibitionVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exhibitionVisit")
public class ExhibitionVisitController {
    @Autowired
    private ExhibitionVisitService exhibitionVisitService;

    // 应用端插入新的展区参观数据
    @RequestMapping(value="/createData", method= RequestMethod.POST)
    public void createData(@RequestBody ExhibitionVisit exhibitionVisit) {
        exhibitionVisitService.createData(exhibitionVisit);
    }

    // 根据时间区间获得这段时间内的展区总体参观数据
    @RequestMapping(value="/getDatabyDate", method= RequestMethod.GET)
    public List<Exhibition_data> getDatabyDate(String startDate, String endDate){
        return exhibitionVisitService.getDatabyDate(startDate, endDate);
    }

    // 把展区参观数据汇集到Excel中,发送给前端供下载
    @RequestMapping(value="/downloadDataExcel", method= RequestMethod.GET)
    public void downloadDataExcel(){
        exhibitionVisitService.downloadDataExcel();
    }

}
