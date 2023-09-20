package com.example.admin.service.impl;

import com.example.admin.entity.Visit.*;
import com.example.admin.entity.Resource.*;
import com.example.admin.mapper.ResourceMapper;
import com.example.admin.service.ExhibitionVisitService;
import com.example.admin.mapper.ExhibitionVisitMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ExhibitionVisitServiceImpl implements ExhibitionVisitService{

    @Autowired
    private ExhibitionVisitMapper exhibitionVisitMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    @Transactional
    public void createDataExhb(ExhibitionVisit exhibitionVisit) {
        exhibitionVisitMapper.insertExhibitionVisit(exhibitionVisit);
    }

    @Override
    @Transactional
    public void createDataProd(ProductVisit productVisit) {
        exhibitionVisitMapper.insertProductVisit(productVisit);
    }

    public List<Product_data> ProdgetDatabyDate(Long startDate, Long endDate){
        List<ProductVisit> visits = exhibitionVisitMapper.ProdgetDatabyDate(startDate, endDate);//产品参观记录
        List<exhb2prod> exhb2prodList = exhibitionVisitMapper.getexhb2prod();// 展区到展品的对应字典
        List<Product> prodnamelist = resourceMapper.getallProduct(); //所有产品ID和产品名称
        List<String> prodids = new ArrayList<>();
        Map<String, String>id2name = new HashMap<>();
        for (exhb2prod item:exhb2prodList){
            prodids.add(item.getProdid()); //得到产品ID列表
        }
        for (Product product:prodnamelist){
            id2name.put(product.getProduct_id(),product.getProduct_name());//产品ID和产品名称的对应关系
        }
        Map<String, Integer> visitNum = new HashMap<>();
        Map<String, HashSet<Integer>> visitUser = new HashMap<>();
        // Map<String, List<ProductVisit>> visitProd = new HashMap<>();
        Map<String, Integer> visitTime = new HashMap<>();
        List<Product_data> visitdata = new ArrayList<>(); //汇总所有的产品的参观数据,然后分发给每个展区
        // 每一个产品的初始数据设置为0, 每个产品都不能落下
        for (String tempprodid:prodids){
            visitNum.put(tempprodid,0);
            visitUser.put(tempprodid, new HashSet<>());
            // visitProd.put(tempprodid, new ArrayList<>());
            visitTime.put(tempprodid,0);
        }
        // 针对每一个产品计算点击量和来访用户数量
        for (ProductVisit single:visits){
            // 处理点击量
            String tempprodid = single.getProdid();

            // 每次记录加一次点击量
            visitNum.put(tempprodid, visitNum.get(tempprodid)+1);
            // 处理访客量, 用HashSet集合来存储名字确保不重复
            int tempuserid = single.getUserid();

            visitUser.get(tempprodid).add(tempuserid);
            // 访问时间用duration来累加就好了
            visitTime.put(tempprodid, visitTime.get(tempprodid)+single.getDuration());
        }
        // 生成每一个产品的数据对象, 分别把数据都放进去
        for (String prodid:visitNum.keySet()){
            int tempnum = visitNum.get(prodid);
            int tempuser = visitUser.get(prodid).size();
            int temptime = visitTime.get(prodid)/60; // 秒换算成分钟
            visitdata.add(new Product_data(prodid,id2name.get(prodid),tempnum,tempuser,temptime));
        }
        return visitdata;

    }

    /*
    * 收集日期范围内的所有记录, 逐条的按照展区ID和用户ID放到字典里做累加记录
    * 访问时间的处理比较复杂
    * */
    @Override
    @Transactional
    public List<Exhibition_data> getDatabyDate(Long startDate, Long endDate) {
        List<ExhibitionVisit> visits = exhibitionVisitMapper.ExhbgetDatabyDate(startDate, endDate);//展区参观记录
        List<String> exhbids = exhibitionVisitMapper.getExhibitionList();//展区列表
        Map<String, Integer> visitNum = new HashMap<>();
        Map<String, HashSet<Integer>> visitUser = new HashMap<>();
        Map<String, List<ExhibitionVisit>> visitExhb = new HashMap<>();
        Map<String, Integer> visitTime = new HashMap<>();
        List<Exhibition_data> visitdata = new ArrayList<>();
        Map<String, Integer> visitdatadict= new HashMap<>(); // 记录每一个展区ID对应的是visitdata中的哪一个索引
        // 每一个展区的初始数据设置为0, 每个展区都不能落下
        for (String tempexhbid:exhbids){
            visitNum.put(tempexhbid,0);
            visitUser.put(tempexhbid, new HashSet<>());
            visitExhb.put(tempexhbid, new ArrayList<>());
        }
        // 针对每一个展区计算点击量和来访用户数量
        for (ExhibitionVisit single:visits){
            // 处理点击量
            String tempexhbid = single.getExhibition_id();
            //if (!visitNum.containsKey(tempexhbid)){
            //    visitNum.put(tempexhbid,0);
            //}
            // 每次进入的记录加一次点击量
            if (single.isEnter()){
                visitNum.put(tempexhbid, visitNum.get(tempexhbid)+1);
            }
            // 处理访客量, 用HashSet集合来存储名字确保不重复
            int tempuserid = single.getUserid();
            //if (!visitUser.containsKey(tempexhbid)){
            //    visitUser.put(tempexhbid, new HashSet<>());
            //}
            visitUser.get(tempexhbid).add(tempuserid);
            // 处理访问时间之前先把每个展区的访问记录区分开来,每一个展区的访问时间必须单独计算
            //if (!visitExhb.containsKey(tempexhbid)){
            //    visitExhb.put(tempexhbid, new ArrayList<>());
            //}
            visitExhb.get(tempexhbid).add(single);
        }
        // 针对每一个展区单独计算访问时长
        for (Map.Entry<String,List<ExhibitionVisit>> entry: visitExhb.entrySet()){
            String exhbid = entry.getKey();
            List<ExhibitionVisit> exhbvisits = entry.getValue();
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
                        System.out.println(first.getUserid()+"成功的配对"+totalDuration);
                    }
                    else {
                        totalDuration += 120;//走单的记录认为用户在展区里参观了120秒
                        i=i+1;
                        System.out.println(first.getUserid()+"走单"+totalDuration);
                    }
                }
                else{
                    totalDuration += 120;//走单的记录认为用户在展区里参观了120秒
                    System.out.println(exhbvisits.get(i).getUserid()+"走单"+totalDuration);
                    i=i+1;

                }
            }
            visitTime.put(exhbid, (int) (totalDuration/60));
            // 记录每一个展区的参观时长是多少分钟
        }
        // 对每一个展区下的每一个展品都做统计
        int exhbindex = 0;
        // 生成每一个展区的数据对象, 分别把数据都放进去
        for (String exhbid:visitNum.keySet()){
            int tempnum = visitNum.get(exhbid);
            int tempuser = visitUser.get(exhbid).size();
            int temptime = visitTime.get(exhbid);
            visitdata.add(new Exhibition_data(exhbid,tempnum,tempuser,temptime));
            visitdatadict.put(exhbid,exhbindex);
            exhbindex +=1;
        }
        List<exhb2prod> exhb2prodList = exhibitionVisitMapper.getexhb2prod();// 展区到展品的对应字典
        Map<String, String> proddict = new HashMap<>();//生成一个从展品找到对应展区的字典
        for (exhb2prod item:exhb2prodList){
            proddict.put(item.getProdid(),item.getExhbid());
        }
        List<Product_data> product_dataList = ProdgetDatabyDate(startDate,endDate);
        for (Product_data product_data:product_dataList){
            String exhbid = proddict.get(product_data.getProdid());// 产品ID找到展区ID
            int visitdataindex = visitdatadict.get(exhbid); //从字典里的索引找到这个展区ID对应的是列表里的哪一项
            visitdata.get(visitdataindex).getProduct_dataList().add(product_data);
        }
        return visitdata;
    }

    @Override
    @Transactional
    public void downloadDataExcel() {
        //下载Excel
    }
}

