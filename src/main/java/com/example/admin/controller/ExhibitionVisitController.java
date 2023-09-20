package com.example.admin.controller;

import com.example.admin.entity.Visit.Exhibition_data;
import com.example.admin.entity.Visit.ExhibitionVisit;
import com.example.admin.entity.Visit.ProductVisit;
import com.example.admin.service.ExhibitionVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@RestController
@RequestMapping("/exhibitionVisit")
public class ExhibitionVisitController {
    @Autowired
    private ExhibitionVisitService exhibitionVisitService;

    // 应用端插入新的展区参观数据
    @RequestMapping(value="/createDataExhb", method= RequestMethod.POST)
    public void createDataExhb(@RequestBody ExhibitionVisit exhibitionVisit) {
        exhibitionVisitService.createDataExhb(exhibitionVisit);
    }

    // 应用端插入新的产品参观数据
    @RequestMapping(value="/createDataProd", method= RequestMethod.POST)
    public void createDataProd(@RequestBody ProductVisit productVisit) {
        exhibitionVisitService.createDataProd(productVisit);
    }

    // 根据时间区间获得这段时间内的展区总体参观数据
    @RequestMapping(value="/getDatabyDate", method= RequestMethod.GET)
    public List<Exhibition_data> getDatabyDate(String startDate, String endDate){
        // startDate "2023-09-07"  endDate "2023-09-20"
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long startday = sdf.parse(startDate).getTime()/1000;
            long endday = sdf.parse(endDate).getTime()/1000;
            return exhibitionVisitService.getDatabyDate(startday, endday);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 把展区参观数据汇集到Excel中,发送给前端供下载
    @RequestMapping(value="/downloadDataExcel", method= RequestMethod.GET)
    public void downloadDataExcel(){
        exhibitionVisitService.downloadDataExcel();
    }

}
