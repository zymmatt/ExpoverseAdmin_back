package com.example.admin.service.impl;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.example.admin.entity.User.*;
import com.example.admin.entity.Visit.*;
import com.example.admin.entity.Resource.*;
import com.example.admin.mapper.ResourceMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.service.ExhibitionVisitService;
import com.example.admin.mapper.ExhibitionVisitMapper;
import com.example.admin.utils.statistics;
import com.example.admin.utils.datetime;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.admin.utils.blobstorage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ExhibitionVisitServiceImpl implements ExhibitionVisitService{

    @Autowired
    private ExhibitionVisitMapper exhibitionVisitMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void createDataExhb(PostExhbVisit postExhbVisit) {
        int loginid = postExhbVisit.getLoginid();
        int userid = postExhbVisit.getUserid();
        String exhbid =  postExhbVisit.getExhibition_id();
        boolean isenter = postExhbVisit.getEnter();
        String trigger_timestamp = postExhbVisit.getTrigger_timestamp();
        ExhibitionVisit exhibitionVisit= new ExhibitionVisit(loginid,userid,Long.parseLong(trigger_timestamp),
                                                             exhbid,isenter);
        exhibitionVisitMapper.insertExhibitionVisit(exhibitionVisit);
    }

    @Override
    @Transactional
    public void createDataProd(PostProdVisit postProdVisit) {
        int userid = postProdVisit.getUserid();
        int loginid = postProdVisit.getLoginid();
        Long trigger_timestamp = Long.parseLong(postProdVisit.getTrigger_timestamp());
        List<PostProdVisitSingle> productList = postProdVisit.getProduct();
        for (PostProdVisitSingle postProdVisitSingle:productList){
            String prodid = postProdVisitSingle.getProdid();
            int duration = postProdVisitSingle.getDuration();
            ProductVisit productVisit = new ProductVisit(loginid, userid, prodid,
                                                         duration, trigger_timestamp);
            exhibitionVisitMapper.insertProductVisit(productVisit);
        }

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
            // 只有当这个展品ID已经被汇总表格存放过才能记录,防止参观ID中有一些意料之外的数据
            if (visitNum.containsKey(tempprodid)) {
                visitNum.put(tempprodid, visitNum.get(tempprodid) + 1);
                // 处理访客量, 用HashSet集合来存储名字确保不重复
                int tempuserid = single.getUserid();

                visitUser.get(tempprodid).add(tempuserid);
                // 访问时间用duration来累加就好了
                visitTime.put(tempprodid, visitTime.get(tempprodid) + single.getDuration());
            }
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
            long totalDuration = statistics.exhbvisitTimeStat(entry.getValue());
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
    // public String downloadvisitTimeDataExcel(HttpServletResponse response) throws IOException {
    public String downloadvisitTimeDataExcel() throws IOException {
        //统计访客参观时长  下载Excel
        List<Exhibition> exhibitionList = exhibitionVisitMapper.getAllExhibition();
        List<Product> productList = resourceMapper.getallProduct();
        List<Login> loginList = userMapper.getallLogin();
        List<ProductVisit> productVisitList = exhibitionVisitMapper.ProdgetDatabyDate(946684800L ,2524579200L); // 2000/1/1 到 2100/1/1
        List<ExhibitionVisit> exhvisit = exhibitionVisitMapper.ExhbgetDatabyDate(946684800L ,2524579200L);
        List<User> userList = userMapper.findAll();
        Map<String, List<String>> exhb2proddict = new HashMap<>(); // 展区ID到展品ID的字典
        Map<String, String> exhb2name = new HashMap<>(); // 展区ID到名字的字典
        Map<String, String> prod2name = new HashMap<>(); // 展品ID到名字的字典
        HashSet<Integer> userset = new HashSet<>(); // 登录的用户数量
        HashSet<Integer> loginset = new HashSet<>(); // 登录ID的集合
        Map<String, List<ProductVisit>> prodid2visitdict = new HashMap<>(); // 展品ID查询展品参观记录
        Map<String, Integer> prodid2col = new HashMap<>(); // 展品ID到要写入excel的哪一列
        Map<String, Integer> exhbid2col = new HashMap<>(); // 展区ID到要写入excel的哪一列
        Map<Integer, Map<String, Integer>> loginid2productvisit = new HashMap<>(); // 每一个登录ID在每个展品下的参观时长
        Map<Integer, Map<String, List<ExhibitionVisit>>> loginid2exhbvisit = new HashMap<>(); // 每一个登录ID在每个展区里的参观记录
        Map<Integer, Integer> loginid2userid = new HashMap<>(); // 登录ID查用户ID的字典
        Map<Integer, String> userid2name = new HashMap<>(); // 用户ID查用户名字的字典
        // loginList按照登录ID和触发时间戳排序, 最晚登录的要放在最前面
        Collections.sort(loginList, (r1, r2) ->{
            if (r1.getLoginid()==r2.getLoginid()){
                return -Long.compare(r1.getTrigger_timestamp(), r2.getTrigger_timestamp());
            } else {
                return -Integer.compare(r1.getLoginid(),r2.getLoginid());
            }
        });
        //int total_duration = 0; // 用户访问的时长
        for (Login login:loginList){
            int loginid = login.getLoginid();
            int userid = login.getUserid();
            loginset.add(loginid); //记录下来所有的loginID
            loginid2productvisit.put(loginid, new HashMap<>());
            loginid2exhbvisit.put(loginid, new HashMap<>());
            userset.add(userid);
            loginid2userid.put(loginid, userid);
        }
        for (User user:userList){
            int userid = user.getId();
            String name = user.getName();
            userid2name.put(userid,name);
        }

        for (Exhibition exhibition:exhibitionList){
            String exhbid = exhibition.getExhibition_id();
            exhb2proddict.put(exhbid, new ArrayList<>());
            exhb2name.put(exhbid,exhibition.getName());
            for (Integer loginid:loginset){
                loginid2exhbvisit.get(loginid).put(exhbid, new ArrayList<>());
            }
        }
        for (Product product: productList){
            String prodid = product.getProduct_id();
            String exhbid = product.getExhibition_id();
            String name = product.getProduct_name();
            exhb2proddict.get(exhbid).add(prodid);
            prod2name.put(prodid, name);
            prodid2visitdict.put(prodid,new ArrayList<>());
            for (Integer loginid:loginset){
                loginid2productvisit.get(loginid).put(prodid,0);
                //每个loginid下的每一个展品我们都初始设置参观时长是0
            }
        }

        // 对每一次展品参观的记录,都要对应到是哪一个loginid参观了哪一个展品多长时间,要做累加
        for (ProductVisit productVisit:productVisitList){
            prodid2visitdict.get(productVisit.getProdid()).add(productVisit);//展品ID记录对应的参观记录
            int loginid = productVisit.getLoginid();
            String prodid = productVisit.getProdid();
            int duration = productVisit.getDuration();
            loginid2productvisit.get(loginid).put(prodid,loginid2productvisit.get(loginid).get(prodid)+duration);
        }

        // 对每一次展区参观的记录,都要对应到是哪一个loginid参观了哪一个展区,把记录添加进去
        for (ExhibitionVisit exhibitionVisit:exhvisit){
            int loginid = exhibitionVisit.getLoginid();
            String exhbid = exhibitionVisit.getExhibition_id();
            // System.out.println(loginid);
            // System.out.println(exhbid);
            if (loginid2exhbvisit.containsKey(loginid)){
                loginid2exhbvisit.get(loginid).get(exhbid).add(exhibitionVisit);
            }
        }

        // 创建一个工作表
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Sheet1");

        // 总计区域的抬头合并单元格
        Row headerRow = sheet.createRow(0); // 总计的第一行,展区名字
        Row prodRow = sheet.createRow(1); // 总计的第二行,展品名字
        Row totaldataRow = sheet.createRow(2); // 记录总计数据在第三行
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
        headerRow.createCell(0).setCellValue("总计");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        headerRow.createCell(1).setCellValue("访客人数(人)");
        totaldataRow.createCell(1).setCellValue(userset.size()); // 集合不计算重复登录的用户
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        headerRow.createCell(2).setCellValue("访客次数(次)");
        totaldataRow.createCell(2).setCellValue(loginList.size());
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
        headerRow.createCell(3).setCellValue("平均访问展区数(个)");
        double avgvisitexhb = 0;
        if (loginList.size()>0){ // 可能没有登录
            avgvisitexhb = (double) exhvisit.size()/loginList.size();
        }
        totaldataRow.createCell(3).setCellValue(avgvisitexhb);
        // 用户区域的抬头合并单元格
        Row userheaderRow = sheet.createRow(3); // 用户区域的第一行,展区名字
        Row userprodRow = sheet.createRow(4); // 总计的第二行,展品名字
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 0));
        userheaderRow.createCell(0).setCellValue("登录序号");
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 1, 1));
        userheaderRow.createCell(1).setCellValue("姓名");
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 2, 2));
        userheaderRow.createCell(2).setCellValue("访问时间");
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 3, 3));
        userheaderRow.createCell(3).setCellValue("访问时长(秒)");
        //headerRow.createCell(3).setCellValue("平均访问时间(分钟/人)");
        //int avgminduration = total_duration/60/loginList.size(); // 秒转分钟再除以人次
        //totaldataRow.createCell(3).setCellValue(avgminduration);
        int exhbcol = 4;
        int prodcol = 4;
        // 总体数据中每一个展区每一个展品的参观时长

        //for (String exhbid : exhb2proddict.keySet()) {
        // 保证展区的排列顺序
        for (Exhibition exhibition: exhibitionList) {
            String exhbid = exhibition.getExhibition_id();
            if (exhb2proddict.get(exhbid).size()==0){ // 荣誉墙和专利墙都是没有产品的,此时直接统计展区的访问时长
                sheet.addMergedRegion(new CellRangeAddress(0, 1, exhbcol, exhbcol));
                headerRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                sheet.addMergedRegion(new CellRangeAddress(3, 4, exhbcol, exhbcol));
                userheaderRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                List<ExhibitionVisit> exhbvisits = exhibitionVisitMapper.getexhbvisitbyexhbid(exhbid);
                long totalDuration = statistics.exhbvisitTimeStat(exhbvisits);
                //totaldataRow.createCell(prodcol).setCellValue((int) (totalDuration/60));
                totaldataRow.createCell(prodcol).setCellValue(totalDuration);
                exhbid2col.put(exhbid,exhbcol);
                //总计的展品数据的第几列比用户的展品数据的第几列一样
                exhbcol+=1;
                prodcol+=1;
            } else if (exhb2proddict.get(exhbid).size()==1){ // 个别展区只有一个展品
                String prodid = exhb2proddict.get(exhbid).get(0);
                headerRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                userheaderRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                prodRow.createCell(prodcol).setCellValue(prod2name.get(prodid));
                userprodRow.createCell(prodcol).setCellValue(prod2name.get(prodid));
                // 统计唯一展品的访问时长
                double temp_prodduration = 0;

                for (ProductVisit productVisit:prodid2visitdict.get(prodid)){
                    temp_prodduration += productVisit.getDuration();
                }
                //totaldataRow.createCell(prodcol).setCellValue((int) (temp_prodduration/60));
                totaldataRow.createCell(prodcol).setCellValue(temp_prodduration);
                prodid2col.put(prodid,prodcol);
                //总计的展品数据的第几列比用户的展品数据的第几列一样
                exhbcol+=1;
                prodcol+=1;
            } else {
                sheet.addMergedRegion(new CellRangeAddress(0, 0,
                        exhbcol, exhbcol+exhb2proddict.get(exhbid).size()-1));
                headerRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                sheet.addMergedRegion(new CellRangeAddress(3, 3,
                        exhbcol, exhbcol+exhb2proddict.get(exhbid).size()-1));
                userheaderRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                exhbcol += exhb2proddict.get(exhbid).size();
                for (int tempi=0; tempi<exhb2proddict.get(exhbid).size(); tempi++){
                    String prodid = exhb2proddict.get(exhbid).get(tempi);
                    prodRow.createCell(prodcol).setCellValue(prod2name.get(prodid));
                    userprodRow.createCell(prodcol).setCellValue(prod2name.get(prodid));
                    // 逐个统计展品的访问时长
                    double temp_prodduration = 0;
                    for (ProductVisit productVisit:prodid2visitdict.get(prodid)){
                        temp_prodduration += productVisit.getDuration();
                    }
                    // totaldataRow.createCell(prodcol).setCellValue((int) (temp_prodduration/60));
                    totaldataRow.createCell(prodcol).setCellValue(temp_prodduration);
                    prodid2col.put(prodid,prodcol);
                    //总计的展品数据的第几列比用户的展品数据的第几列一样
                    prodcol+=1;
                }
            }
        }

        int loginRow = 5;
        // 每一次登录的loginid要逐行做统计
        for (Login login:loginList){
            int loginid = login.getLoginid();
            Row tempRow = sheet.createRow(loginRow);
            // 登录序号
            tempRow.createCell(0).setCellValue(loginid);
            // 姓名
            tempRow.createCell(1).setCellValue(userid2name.get(loginid2userid.get(loginid)));
            // 访问时间
            tempRow.createCell(2).setCellValue(datetime.timestamp2str(login.getTrigger_timestamp()));
            long totalseconds = 120L;
            if (login.getAlive_timestamp() != null){
                totalseconds = login.getAlive_timestamp()-login.getTrigger_timestamp();
            }
            // 访问时长 分钟
            // tempRow.createCell(3).setCellValue((int) (totalseconds/60));
            tempRow.createCell(3).setCellValue(totalseconds);
            // 所有展品下的访问时长
            for (String exhbid : exhb2proddict.keySet()) {
                if (exhb2proddict.get(exhbid).size()==0) { // 荣誉墙和专利墙都是没有产品的,此时直接统计展区的访问时长
                    int tempcell = exhbid2col.get(exhbid);
                    List<ExhibitionVisit> exhbvisits = loginid2exhbvisit.get(loginid).get(exhbid);
                    long totalDuration = statistics.exhbvisitTimeStat(exhbvisits);
                    tempRow.createCell(tempcell).setCellValue(totalDuration);
                    //tempRow.createCell(tempcell).setCellValue((int) (totalDuration/60));

                } else {
                    for (int tempi=0; tempi<exhb2proddict.get(exhbid).size(); tempi++){
                        String prodid = exhb2proddict.get(exhbid).get(tempi);
                        int tempcell = prodid2col.get(prodid);
                        int visitSeconds = loginid2productvisit.get(loginid).get(prodid);
                        tempRow.createCell(tempcell).setCellValue(visitSeconds);
                        //tempRow.createCell(tempcell).setCellValue((int)(visitSeconds/60));
                    }
                }
            }
            loginRow += 1;
        }

        String rawFileName = "访问时长统计.xlsx";
        byte[] bytes = blobstorage.workbookToByteArray(workbook);
        BlobContainerClient containerClient = blobstorage.getclient();
        BlobClient blobClient = containerClient.getBlobClient(rawFileName);
        blobClient.deleteIfExists();// 删除旧的
        blobClient.upload(BinaryData.fromBytes(bytes));
        // System.out.println("上传了新文件"+rawFileName);
        String accountName = blobstorage.accountName();
        String containerName = blobstorage.containerName();
        String tempSAS = blobstorage.gettempSAS(blobstorage.getclient());
        return String.format("https://%s.blob.core.windows.net/%s/%s?%s",
                accountName,containerName,rawFileName,tempSAS);
        /*
        response.reset();
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" +
                    URLEncoder.encode(rawFileName, String.valueOf(StandardCharsets.UTF_8)));
            response.setHeader("Access-Control-Allow-Origin", "*");
            //response.setHeader("content_type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("content_type","application/vnd.ms-excel");
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // 保存工作簿到文件
        FileOutputStream fos = new FileOutputStream(rawFileName);
        workbook.write(fos);
        fos.close();
        // 关闭工作簿
        workbook.close();

         */

    }

    @Override
    @Transactional
    //public void downloadvisitNumDataExcel(HttpServletResponse response) throws IOException {
    public String downloadvisitNumDataExcel() throws IOException {
        //统计访客参观人次  下载Excel
        List<Exhibition> exhibitionList = exhibitionVisitMapper.getAllExhibition();
        List<Product> productList = resourceMapper.getallProduct();
        List<Login> loginList = userMapper.getallLogin();
        List<ProductVisit> productVisitList = exhibitionVisitMapper.ProdgetDatabyDate(946684800L ,2524579200L); // 2000/1/1 到 2100/1/1
        List<ExhibitionVisit> exhvisit = exhibitionVisitMapper.ExhbgetDatabyDate(946684800L ,2524579200L);
        List<User> userList = userMapper.findAll();
        Map<String, List<String>> exhb2proddict = new HashMap<>(); // 展区ID到展品ID的字典
        Map<String, String> exhb2name = new HashMap<>(); // 展区ID到名字的字典
        Map<String, String> prod2name = new HashMap<>(); // 展品ID到名字的字典
        HashSet<Integer> userset = new HashSet<>(); // 登录的用户数量
        HashSet<Integer> loginset = new HashSet<>(); // 登录ID的集合
        Map<String, List<ProductVisit>> prodid2visitdict = new HashMap<>(); // 展品ID查询展品参观记录
        Map<String, Integer> prodid2col = new HashMap<>(); // 展品ID到要写入excel的哪一列
        Map<String, Integer> exhbid2col = new HashMap<>(); // 展区ID到要写入excel的哪一列
        Map<Integer, Map<String, Integer>> loginid2productvisit = new HashMap<>(); // 每一个登录ID在每个展品下的参观次数
        Map<Integer, Map<String, List<ExhibitionVisit>>> loginid2exhbvisit = new HashMap<>(); // 每一个登录ID在每个展区里的参观记录
        Map<Integer, Integer> loginid2userid = new HashMap<>(); // 登录ID查用户ID的字典
        Map<Integer, String> userid2name = new HashMap<>(); // 用户ID查用户名字的字典
        // loginList按照登录ID和触发时间戳排序
        Collections.sort(loginList, (r1, r2) ->{
            if (r1.getLoginid()==r2.getLoginid()){
                return -Long.compare(r1.getTrigger_timestamp(), r2.getTrigger_timestamp());
            } else {
                return -Integer.compare(r1.getLoginid(),r2.getLoginid());
            }
        });
        long total_duration = 0L; // 所有登录人次访问的总时长
        for (Login login:loginList){
            int loginid = login.getLoginid();
            int userid = login.getUserid();
            loginset.add(loginid); //记录下来所有的loginID
            loginid2productvisit.put(loginid, new HashMap<>());
            loginid2exhbvisit.put(loginid, new HashMap<>());
            userset.add(userid);
            loginid2userid.put(loginid, userid);
            Long starttime = login.getTrigger_timestamp();
            Long endtime = login.getAlive_timestamp();
            Long tempduration = 120L;
            if (starttime!= null && endtime != null) {
                tempduration = endtime-starttime;
            }
            total_duration+=tempduration;
        }
        for (User user:userList){
            int userid = user.getId();
            String name = user.getName();
            userid2name.put(userid,name);
        }
        for (Exhibition exhibition:exhibitionList){
            String exhbid = exhibition.getExhibition_id();
            exhb2proddict.put(exhbid, new ArrayList<>());
            exhb2name.put(exhbid,exhibition.getName());
            for (Integer loginid:loginset){
                loginid2exhbvisit.get(loginid).put(exhbid, new ArrayList<>());
            }
        }
        for (Product product: productList){
            String prodid = product.getProduct_id();
            String exhbid = product.getExhibition_id();
            String name = product.getProduct_name();
            exhb2proddict.get(exhbid).add(prodid);
            prod2name.put(prodid, name);
            prodid2visitdict.put(prodid,new ArrayList<>());
            for (Integer loginid:loginset){
                loginid2productvisit.get(loginid).put(prodid,0);
                //每个loginid下的每一个展品我们都初始设置参观次数是0
            }
        }
        // 对每一次展品参观的记录,都要对应到是哪一个loginid参观了哪一个展品共多少次
        for (ProductVisit productVisit:productVisitList){
            String prodid = productVisit.getProdid();
            // 要确定字典里面事先已经放入了这个展品ID,防止意料之外的展品参观记录的放入
            if (prodid2visitdict.containsKey(prodid)) {
                prodid2visitdict.get(prodid).add(productVisit);//展品ID记录对应的参观记录
                int loginid = productVisit.getLoginid();
                // int duration = productVisit.getDuration();
                loginid2productvisit.get(loginid).put(prodid, loginid2productvisit.get(loginid).get(prodid) + 1);
            }
        }
        // 对每一次展区参观的记录,都要对应到是哪一个loginid参观了哪一个展区,把记录添加进去
        for (ExhibitionVisit exhibitionVisit:exhvisit){
            int loginid = exhibitionVisit.getLoginid();
            String exhbid = exhibitionVisit.getExhibition_id();
            loginid2exhbvisit.get(loginid).get(exhbid).add(exhibitionVisit);
        }
        // 创建一个工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        // 总计区域的抬头合并单元格
        Row headerRow = sheet.createRow(0); // 总计的第一行,展区名字
        Row prodRow = sheet.createRow(1); // 总计的第二行,展品名字
        Row totaldataRow = sheet.createRow(2); // 记录总计数据在第三行
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
        headerRow.createCell(0).setCellValue("总计");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        headerRow.createCell(1).setCellValue("访客人数(人)");
        totaldataRow.createCell(1).setCellValue(userset.size()); // 集合不计算重复登录的用户
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        headerRow.createCell(2).setCellValue("访客次数(次)");
        totaldataRow.createCell(2).setCellValue(loginList.size());
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
        headerRow.createCell(3).setCellValue("平均访问时间(秒/人次)");
        float avgvisittime = 0;
        if (loginList.size()>0){ // 参观人次可能为0
            avgvisittime = (float)(total_duration/loginList.size());
        }
        //totaldataRow.createCell(3).setCellValue(avgvisittime);
        totaldataRow.createCell(3).setCellValue(Math.round(avgvisittime * 100.0) / 100.0);
        // 用户区域的抬头合并单元格
        Row userheaderRow = sheet.createRow(3); // 用户区域的第一行,展区名字
        Row userprodRow = sheet.createRow(4); // 总计的第二行,展品名字
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 0));
        userheaderRow.createCell(0).setCellValue("登录序号");
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 1, 1));
        userheaderRow.createCell(1).setCellValue("姓名");
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 2, 2));
        userheaderRow.createCell(2).setCellValue("访问时间");
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 3, 3));
        userheaderRow.createCell(3).setCellValue("访问时长(秒)");
        //headerRow.createCell(3).setCellValue("平均访问时间(分钟/人)");
        //int avgminduration = total_duration/60/loginList.size(); // 秒转分钟再除以人次
        //totaldataRow.createCell(3).setCellValue(avgminduration);
        int exhbcol = 4;
        int prodcol = 4;
        // 总体数据中每一个展区每一个展品的参观人次
        // 保证展区的排列顺序
        // for (String exhbid : exhb2proddict.keySet()) {
        for (Exhibition exhibition: exhibitionList) {
            String exhbid = exhibition.getExhibition_id();
            if (exhb2proddict.get(exhbid).size()==0){ // 荣誉墙和专利墙都是没有产品的,此时直接统计展区的访问时长
                sheet.addMergedRegion(new CellRangeAddress(0, 1, exhbcol, exhbcol));
                headerRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                sheet.addMergedRegion(new CellRangeAddress(3, 4, exhbcol, exhbcol));
                userheaderRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                List<ExhibitionVisit> exhbvisits = exhibitionVisitMapper.getexhbvisitbyexhbid(exhbid);
                int totalNum = statistics.exhbvisitNumStat(exhbvisits);
                totaldataRow.createCell(prodcol).setCellValue(totalNum);
                exhbid2col.put(exhbid,exhbcol);
                //总计的展品数据的第几列比用户的展品数据的第几列一样
                exhbcol+=1;
                prodcol+=1;
            } else if (exhb2proddict.get(exhbid).size()==1){ // 个别展区只有一个展品
                String prodid = exhb2proddict.get(exhbid).get(0);
                headerRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                userheaderRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                prodRow.createCell(prodcol).setCellValue(prod2name.get(prodid));
                userprodRow.createCell(prodcol).setCellValue(prod2name.get(prodid));
                // 统计唯一展品的访问人次
                int temp_num = 0;

                for (ProductVisit productVisit:prodid2visitdict.get(prodid)){
                    //temp_prodduration += productVisit.getDuration();
                    temp_num += 1;
                }
                totaldataRow.createCell(prodcol).setCellValue(temp_num);
                prodid2col.put(prodid,prodcol);
                //总计的展品数据的第几列比用户的展品数据的第几列一样
                exhbcol+=1;
                prodcol+=1;
            } else {
                sheet.addMergedRegion(new CellRangeAddress(0, 0,
                        exhbcol, exhbcol+exhb2proddict.get(exhbid).size()-1));
                headerRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                sheet.addMergedRegion(new CellRangeAddress(3, 3,
                        exhbcol, exhbcol+exhb2proddict.get(exhbid).size()-1));
                userheaderRow.createCell(exhbcol).setCellValue(exhb2name.get(exhbid));
                exhbcol += exhb2proddict.get(exhbid).size();
                for (int tempi=0; tempi<exhb2proddict.get(exhbid).size(); tempi++){
                    String prodid = exhb2proddict.get(exhbid).get(tempi);
                    prodRow.createCell(prodcol).setCellValue(prod2name.get(prodid));
                    userprodRow.createCell(prodcol).setCellValue(prod2name.get(prodid));
                    // 统计唯一展品的访问人次
                    int temp_num = 0;
                    for (ProductVisit productVisit:prodid2visitdict.get(prodid)){
                        // temp_prodduration += productVisit.getDuration();
                        temp_num += 1;
                    }
                    totaldataRow.createCell(prodcol).setCellValue(temp_num);
                    prodid2col.put(prodid,prodcol);
                    //总计的展品数据的第几列比用户的展品数据的第几列一样
                    prodcol+=1;
                }
            }
        }

        int loginRow = 5;
        // 每一次登录的loginid要逐行做统计
        for (Login login:loginList){
            int loginid = login.getLoginid();
            Row tempRow = sheet.createRow(loginRow);
            // 登录序号
            tempRow.createCell(0).setCellValue(loginid);
            // 姓名
            tempRow.createCell(1).setCellValue(userid2name.get(loginid2userid.get(loginid)));
            // 访问时间
            tempRow.createCell(2).setCellValue(datetime.timestamp2str(login.getTrigger_timestamp()));
            long totalseconds = 120L;
            if (login.getAlive_timestamp() != null){
                totalseconds = login.getAlive_timestamp()-login.getTrigger_timestamp();
            }
            // 访问时长 分钟
            // float tempminute = (float) (totalseconds/60);
            // tempRow.createCell(3).setCellValue(Math.round(tempminute * 100.0) / 100.0);
            //tempRow.createCell(3).setCellValue((int) (totalseconds/60));
            tempRow.createCell(3).setCellValue(totalseconds);
            // 所有展品下的访问时长
            for (String exhbid : exhb2proddict.keySet()) {
                if (exhb2proddict.get(exhbid).size()==0) { // 荣誉墙和专利墙都是没有产品的,此时直接统计展区的访问时长
                    int tempcell = exhbid2col.get(exhbid);
                    List<ExhibitionVisit> exhbvisits = loginid2exhbvisit.get(loginid).get(exhbid);
                    int totalNum = statistics.exhbvisitNumStat(exhbvisits);
                    tempRow.createCell(tempcell).setCellValue(totalNum);
                } else {
                    for (int tempi=0; tempi<exhb2proddict.get(exhbid).size(); tempi++){
                        String prodid = exhb2proddict.get(exhbid).get(tempi);
                        int tempcell = prodid2col.get(prodid);
                        int visitNum = loginid2productvisit.get(loginid).get(prodid);
                        tempRow.createCell(tempcell).setCellValue(visitNum);
                    }
                }
            }
            loginRow += 1;
        }

        String rawFileName = "访问人次统计.xlsx";
        byte[] bytes = blobstorage.workbookToByteArray(workbook);
        BlobContainerClient containerClient = blobstorage.getclient();
        BlobClient blobClient = containerClient.getBlobClient(rawFileName);
        blobClient.deleteIfExists();// 删除旧的
        blobClient.upload(BinaryData.fromBytes(bytes));
        // System.out.println("上传了新文件"+rawFileName);
        String accountName = blobstorage.accountName();
        String containerName = blobstorage.containerName();
        String tempSAS = blobstorage.gettempSAS(blobstorage.getclient());
        return String.format("https://%s.blob.core.windows.net/%s/%s?%s",
                accountName,containerName,rawFileName,tempSAS);
        // response.reset();
        /*
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" +
                    URLEncoder.encode(rawFileName, String.valueOf(StandardCharsets.UTF_8)));
            response.setHeader("Access-Control-Allow-Origin", "*");
            //response.setHeader("content_type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("content_type","application/vnd.ms-excel");
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 保存工作簿到文件
        FileOutputStream fos = new FileOutputStream(rawFileName);
        workbook.write(fos);
        fos.close();
        // 关闭工作簿
        workbook.close();

         */
    }
}

