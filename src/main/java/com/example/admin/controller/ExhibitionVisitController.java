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
import com.example.admin.entity.Response.ResponseObject;

@RestController
@RequestMapping("/exhibitionVisit")
public class ExhibitionVisitController {
    @Autowired
    private ExhibitionVisitService exhibitionVisitService;

    // 应用端插入新的展区参观数据
    @RequestMapping(value="/createDataExhb", method= RequestMethod.POST)
    public ResponseObject createDataExhb(@RequestBody PostExhbVisit postExhbVisit) {
        exhibitionVisitService.createDataExhb(postExhbVisit);
        return ResponseObject.success("插入新的展区参观数据成功");
    }

    // 应用端插入新的产品参观数据, 可能同时包含很多个产品
    @RequestMapping(value="/createDataProd", method= RequestMethod.POST)
    public ResponseObject createDataProd(@RequestBody PostProdVisit postProdVisit) {
        exhibitionVisitService.createDataProd(postProdVisit);
        return ResponseObject.success("插入新的展品参观数据成功");
    }

    // 管理员后台  根据时间区间获得这段时间内的展区总体参观数据
    @RequestMapping(value="/getDatabyDate", method= RequestMethod.GET)
    public ResponseObject getDatabyDate(String startDate, String endDate){
        // startDate "2023-09-07"  endDate "2023-09-20"
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long startday = sdf.parse(startDate).getTime()/1000;
            long endday = sdf.parse(endDate).getTime()/1000;
            endday = endday+86400; // 终止日要加24小时,包含进当天
            return ResponseObject.success(exhibitionVisitService.getDatabyDate(startday, endday));
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 管理员后台  把展区的参观时长数据汇集到Excel中,发送给前端供下载
    @RequestMapping(value="/downloadvisitTimeDataExcel", method= RequestMethod.GET)
    public ResponseObject downloadvisitTimeDataExcel(HttpServletResponse response) throws IOException {
        exhibitionVisitService.downloadvisitTimeDataExcel(response);
        return ResponseObject.success("下载展区参观时长数据成功");
    }

    // 把展区的参观人次数据汇集到Excel中,发送给前端供下载
    @RequestMapping(value="/downloadvisitNumDataExcel", method= RequestMethod.GET)
    public ResponseObject downloadvisitNumDataExcel(HttpServletResponse response) throws IOException {
        exhibitionVisitService.downloadvisitNumDataExcel(response);
        return ResponseObject.success("下载展区参观人次数据成功");
    }


}
