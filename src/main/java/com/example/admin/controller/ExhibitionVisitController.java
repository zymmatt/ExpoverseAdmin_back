package com.example.admin.controller;

import com.example.admin.entity.Visit.*;
import com.example.admin.service.ExhibitionVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public void createDataExhb(@RequestBody PostExhbVisit postExhbVisit) {
        exhibitionVisitService.createDataExhb(postExhbVisit);
    }

    // 应用端插入新的产品参观数据, 可能同时包含很多个产品
    @RequestMapping(value="/createDataProd", method= RequestMethod.POST)
    public void createDataProd(@RequestBody PostProdVisit postProdVisit) {
        exhibitionVisitService.createDataProd(postProdVisit);
    }

    // 根据时间区间获得这段时间内的展区总体参观数据
    @RequestMapping(value="/getDatabyDate", method= RequestMethod.GET)
    public List<Exhibition_data> getDatabyDate(String startDate, String endDate){
        // startDate "2023-09-07"  endDate "2023-09-20"
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long startday = sdf.parse(startDate).getTime()/1000;
            long endday = sdf.parse(endDate).getTime()/1000;
            endday = endday+86400; // 终止日要加24小时,包含进当天
            return exhibitionVisitService.getDatabyDate(startday, endday);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 把展区的参观时长数据汇集到Excel中,发送给前端供下载
    @RequestMapping(value="/downloadvisitTimeDataExcel", method= RequestMethod.GET)
    public void downloadvisitTimeDataExcel(HttpServletResponse response) throws IOException {
        exhibitionVisitService.downloadvisitTimeDataExcel(response);
    }

    // 把展区的参观人次数据汇集到Excel中,发送给前端供下载
    @RequestMapping(value="/downloadvisitNumDataExcel", method= RequestMethod.GET)
    public void downloadvisitNumDataExcel(HttpServletResponse response) throws IOException {
        exhibitionVisitService.downloadvisitNumDataExcel(response);
    }


}
