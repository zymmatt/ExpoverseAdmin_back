package com.example.admin.utils;


import com.example.admin.entity.Visit.ExhibitionVisit;

import java.util.Collections;
import java.util.List;

public class statistics {
    // 给定一个展区参观记录的列表,统计列表内这些数据的总共参观时长
    public static long exhbvisitTimeStat(List<ExhibitionVisit> exhbvisits){
        // 对记录按用户ID和时间戳进行排序
        Collections.sort(exhbvisits, (r1, r2) ->{
            if (r1.getUserid()==r2.getUserid()){
                return Long.compare(r1.gettrigger_timestamp(), r2.gettrigger_timestamp());
            } else {
                return Integer.compare(r1.getUserid(),r2.getUserid());
            }
        });
        int i = 0;
        long totalDuration = 0; // 统计总共的参观时间,此处单位是秒,最后转换成分钟
        while (i < exhbvisits.size()) {
            if(i<exhbvisits.size()-1){
                ExhibitionVisit first = exhbvisits.get(i);
                ExhibitionVisit second = exhbvisits.get(i+1);
                //假如前2行是同一个用户而且enter和leave形成一对, 则计算这个用户这一次完整的参观有多少时长
                if (first.getUserid()==second.getUserid() &&
                        first.isEnter() && !second.isEnter()){

                    totalDuration += second.gettrigger_timestamp() - first.gettrigger_timestamp();
                    i=i+2;//配成一对的2行都处理过了
                    //System.out.println(first.getUserid()+"成功的配对"+totalDuration);
                }
                else {
                    totalDuration += 120;//走单的记录认为用户在展区里参观了120秒
                    i=i+1;
                    //System.out.println(first.getUserid()+"走单"+totalDuration);
                }
            }
            else{
                totalDuration += 120;//走单的记录认为用户在展区里参观了120秒
                //System.out.println(exhbvisits.get(i).getUserid()+"走单"+totalDuration);
                i=i+1;

            }
        }
        return totalDuration;
    }

    // 给定一个展区参观记录的列表,统计列表内这些数据的总共参观人次
    public static int exhbvisitNumStat(List<ExhibitionVisit> exhbvisits){
        // 对记录按用户ID和时间戳进行排序
        Collections.sort(exhbvisits, (r1, r2) ->{
            if (r1.getUserid()==r2.getUserid()){
                return Long.compare(r1.gettrigger_timestamp(), r2.gettrigger_timestamp());
            } else {
                return Integer.compare(r1.getUserid(),r2.getUserid());
            }
        });
        int i = 0;
        int totalNum = 0; // 统计总共的参观时间,此处单位是秒,最后转换成分钟
        while (i < exhbvisits.size()) {
            if(i<exhbvisits.size()-1){
                ExhibitionVisit first = exhbvisits.get(i);
                ExhibitionVisit second = exhbvisits.get(i+1);
                //假如前2行是同一个用户而且enter和leave形成一对, 则计算这个用户这一次完整的参观有多少时长
                if (first.getUserid()==second.getUserid() &&
                        first.isEnter() && !second.isEnter()){
                    totalNum += 1;
                    //totalDuration += second.gettrigger_timestamp() - first.gettrigger_timestamp();
                    i=i+2;//配成一对的2行都处理过了
                    //System.out.println(first.getUserid()+"成功的配对"+totalDuration);
                }
                else {
                    totalNum += 1;
                    //totalDuration += 120;//走单的记录认为用户在展区里参观了120秒
                    i=i+1;
                    //System.out.println(first.getUserid()+"走单"+totalDuration);
                }
            }
            else{
                totalNum += 1;
                //totalDuration += 120;//走单的记录认为用户在展区里参观了120秒
                //System.out.println(exhbvisits.get(i).getUserid()+"走单"+totalDuration);
                i=i+1;

            }
        }
        //return totalDuration;
        return totalNum;
    }


}
