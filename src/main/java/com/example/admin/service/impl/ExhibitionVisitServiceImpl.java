package com.example.admin.service.impl;

import com.example.admin.entity.ExhibitionVisit;
import com.example.admin.entity.Exhibition_data;
import com.example.admin.service.ExhibitionVisitService;
import com.example.admin.mapper.ExhibitionVisitMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExhibitionVisitServiceImpl implements ExhibitionVisitService{

    @Autowired
    private ExhibitionVisitMapper exhibitionVisitMapper;

    @Override
    public void createData(ExhibitionVisit exhibitionVisit) {
        exhibitionVisitMapper.insertExhibitionVisit(exhibitionVisit);
    }

    @Override
    public List<Exhibition_data> getDatabyDate(String startDate, String endDate) {
        List<ExhibitionVisit> visits = exhibitionVisitMapper.getDatabyDate(startDate, endDate);
        Map<String, Integer> visitNum = new HashMap<>();
        Map<String, List<Integer>> visitUser = new HashMap<>();
        Map<String, Integer> visitTime = new HashMap<>();
        for (ExhibitionVisit single:visits){
            String tempexhbid = single.getExhibition_id();
            if (!visitNum.containsKey(tempexhbid)){
                visitNum.put(tempexhbid,0);
            }
            if (single.isEnter()){

            }
            int tempuserid = single.getUserid();




        }
        return null;
    }

    @Override
    public void downloadDataExcel() {
        //下载Excel
    }
}
