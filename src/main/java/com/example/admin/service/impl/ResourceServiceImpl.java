package com.example.admin.service.impl;


import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.example.admin.entity.Resource.*;
import com.example.admin.entity.Visit.*;
import com.example.admin.mapper.ExhibitionVisitMapper;
import com.example.admin.service.ResourceService;
import com.example.admin.mapper.ResourceMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.admin.utils.blobstorage;
import com.azure.storage.blob.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.io.FileOutputStream;
import java.time.OffsetDateTime;
import com.example.admin.utils.string_proc;
import static java.lang.Math.min;

@Service
public class ResourceServiceImpl implements ResourceService{

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ExhibitionVisitMapper exhibitionVisitMapper;


    @Override
    @Transactional
    public String gettempSAS(){
        return blobstorage.gettempSAS(blobstorage.getclient());
    }


    @Override
    @Transactional
    public List<ExhbSrc> getAllResource() {
        List<ExhbSrc> exhbSrcList = new ArrayList<>(); // 最终返回的结果
        List<String> exhbidlist = resourceMapper.getallExhbid(); // 所有的展区ID
        List<Product> prodlist = resourceMapper.getallProduct(); // 所有的产品ID
        List<ExhbMovie> exhbMovieList = resourceMapper.getallExhbMovie(); // 所有的展区电影
        List<DM> dmList = resourceMapper.getallDM(); // 所有的DM,按照文件顺序排序
        List<ProdMovie> prodMovieList = resourceMapper.getallProdMovie(); // 所有的产品电影,按照文件顺序排序
        List<exhb2prod> exhb2prodList = exhibitionVisitMapper.getexhb2prod(); // 展区到展品的对应字典
        Map<String,List<DM>>prod2dmlistdict = new HashMap<>(); // 从产品ID到DM列表
        Map<String,List<ProdMovie>>prod2movielistdict= new HashMap<>(); // 从产品ID到电影列表
        Map<String,ExhbMovie>exhb2moviedict = new HashMap<>(); // 从展区ID到电影
        Map<String, List<String>> exhb2proddict = new HashMap<>(); // 生成一个从展区ID找到对应展品ID的字典
        String SAS = "?"+gettempSAS(); // 获得临时密钥
        for (String exhbid:exhbidlist){
            exhb2proddict.put(exhbid, new ArrayList<>());
            exhb2moviedict.put(exhbid,null);
        }
        for (exhb2prod item:exhb2prodList){
            exhb2proddict.get(item.getExhbid()).add(item.getProdid());
        }
        for (Product product:prodlist){
            prod2dmlistdict.put(product.getProduct_id(),new ArrayList<>());
            prod2movielistdict.put(product.getProduct_id(),new ArrayList<>());
        }
        for (DM dm:dmList){
            prod2dmlistdict.get(dm.getProdid()).add(dm);
        }
        for (ProdMovie prodMovie:prodMovieList){
            prod2movielistdict.get(prodMovie.getProdid()).add(prodMovie);
        }
        for (ExhbMovie exhbMovie:exhbMovieList){
            exhb2moviedict.put(exhbMovie.getExhibition_id(),exhbMovie);
        }

        //for (String exhbid : exhb2proddict.keySet()) {
        for (String exhbid: exhbidlist){
            //Integer value = exhb2proddict.get(key);
            //System.out.println("Key: " + key + ", Value: " + value);
            List<String> tempprodlist = exhb2proddict.get(exhbid); // 展区ID找到展品ID列表
            List<ExhbMovie> tempExhbMovielist = new ArrayList<>();
            List<ProdSrc> tempprodsrclist = new ArrayList<>();
            ExhbMovie tempexhbmovie = exhb2moviedict.get(exhbid);
            if (tempexhbmovie != null){
                tempExhbMovielist.add(tempexhbmovie);//目前每一个展区只会有一个影片
            }
            for (String prodid:tempprodlist){
                tempprodsrclist.add(new ProdSrc(prodid,prod2dmlistdict.get(prodid),prod2movielistdict.get(prodid)));
            }
            ExhbSrc exhbSrc = new ExhbSrc(exhbid,tempExhbMovielist,tempprodsrclist);
            exhbSrc.setSAS(SAS);
            exhbSrcList.add(exhbSrc);
        }
        return exhbSrcList;
        /*
        List<ExhbSrc> exhbSrcList = new ArrayList<>();
        for (String exhbid:exhbidlist){
            List<Product> prodlist = resourceMapper.getProdListbyExhbid(exhbid);
            List<ExhbMovie> exhbMovieList = resourceMapper.getExhbMovieURLbyExhbid(exhbid);
            List<ProdMovie> prodMovieList = resourceMapper.getProdMovieURLbyExhbid(exhbid);
            List<ProdSrc> prodSrcList = new ArrayList<>();
            for (Product prod:prodlist){
                String prodid = prod.getProduct_id();
                List<DM> dmList = resourceMapper.getDMURLbyProdid(prodid);
                prodSrcList.add(new ProdSrc(prodid, dmList));
            }
            exhbSrcList.add(new ExhbSrc(exhbid,exhbMovieList,prodMovieList,prodSrcList));
        }
        return exhbSrcList;
        */
    }

    @Override
    @Transactional
    public List<Product> getProdListbyExhbid(String exhbid) {
        return resourceMapper.getProdListbyExhbid(exhbid);
    }

    @Override
    @Transactional
    public List<DM> getDMURLbyProdid(String prodid) {
        return resourceMapper.getDMURLbyProdid(prodid);
    }

    @Override
    @Transactional
    public List<ExhbMovie> getExhbMovieURLbyExhbid(String exhbid) {
        return resourceMapper.getExhbMovieURLbyExhbid(exhbid);
    }

    @Override
    @Transactional
    public List<ProdMovie> getProdMovieURLbyExhbid(String exhbid) {
        return resourceMapper.getProdMovieURLbyExhbid(exhbid);
    }

    // 上传的字典里有这些参数  src name file_no
    // src 以data:打头说明是新图片, name是xxxxxx.jpg, file_no=-1
    // src 以https://打头说明是老图片, name是不加SAS的url地址, file_no是原来这张图片在应用端存储的顺序位置
    @Override
    @Transactional
    public void uploadDMdict(ProdUpdate prodUpdate) throws IOException {
        String prodid = prodUpdate.getProdid();
        List<UploadDM> uploadDMs = prodUpdate.getUploadDMlist();
        List<DM> dmlist = resourceMapper.getDMURLbyProdid(prodid);
        HashMap<String,Integer> DMdict = new HashMap<>();
        List<URL> urlList = new ArrayList<>();
        for (DM dm:dmlist){
            DMdict.put(dm.getUrl(),dm.getDownload());
        }
        for (int i=0;i<uploadDMs.size();i++){
            UploadDM uploadDM = uploadDMs.get(i);
            int file_no = uploadDM.getFile_no(); // 这个新的文件应该存在应用端的第几个顺序
            // for (UploadDM uploadDM:uploadDMs){
            if(uploadDM.getsrc().startsWith("data:")){
                //上传的新图片需要传进azure blob storage数据库中
                String base64Image = uploadDM.getsrc();
                String outputPath = uploadDM.getName();
                // String outputPath = String.format("%s.jpg",imagename);
                /*
                try {
                    String base64Data = base64Image.substring(base64Image.indexOf(',') + 1);
                    base64Data = base64Data.replaceAll("\r|\n", "");
                    base64Data = base64Data.trim();
                    // 解码Base64数据
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

                    // 指定文件路径和名称
                    String outputPath = String.format("%s.jpg",imagename);

                    // 将解码后的数据写入文件
                    try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                        fos.write(decodedBytes);
                        System.out.println("图片已成功保存到 " + outputPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Base64数据无效");
                    System.err.println(e.getMessage());
                }
                */
                BlobContainerClient containerClient = blobstorage.getclient();
                BlobClient blobClient = containerClient.getBlobClient(outputPath);
                //BlobClient oldblobClient = containerClient.getBlobClient(oldUrl);
                String base64Data = base64Image.substring(base64Image.indexOf(',') + 1);
                base64Data = base64Data.replaceAll("\r|\n", "");
                base64Data = base64Data.trim();
                // 将Base64数据解码为字节数组
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                // 将字节数组转换为二进制
                //InputStream inputStream = file.getInputStream();
                blobClient.upload(BinaryData.fromBytes(imageBytes));
                //oldblobClient.deleteIfExists(); // 删除已有的旧资源
                System.out.println("上传了新图片"+outputPath);
                String accountName = blobstorage.accountName();
                String containerName = blobstorage.containerName();
                String tempsrc = String.format("https://%s.blob.core.windows.net/%s/%s",
                        accountName,containerName,outputPath);
                // https://expoverseazureblobdb.blob.core.windows.net/test-ctn1/121glj390n980.jpg
                URL tempurl = new URL(tempsrc,"image",prodid,file_no);// file_no在前端都已经指定好了
                // tempurl.setDownload(1); // 新资源的版本号从1开始
                tempurl.setReal_no(i+1); // 真实展示的顺序
                if (file_no<=dmlist.size()){ // 如果是初始的dm列表范围内的, 就增加对应位置的download
                    tempurl.setDownload(dmlist.get(file_no-1).getDownload()+1);
                }
                else{
                    tempurl.setDownload(1); // 如果是在dm列表范围以外新增的, 就把download设置为1
                }
                urlList.add(tempurl);

            }
            else{
                System.out.println(uploadDM.getsrc());

                URL tempurl = new URL(uploadDM.getName(),"image", prodid, file_no);
                tempurl.setReal_no(i+1); // 真实展示的顺序
                // 在老的DM列表中包含了新DM列表中的某一个
                if (DMdict.containsKey(uploadDM.getName())){
                    int oldDownload = DMdict.get(uploadDM.getName());
                    tempurl.setDownload(oldDownload);
                    DMdict.remove(uploadDM.getName());
                }
                urlList.add(tempurl); // 老资源不用修改已有的blob数据也不用修改download版本号
            }
        }
        // 对于提交的新DM列表中没有包含的旧有DM，可以直接删除
        BlobContainerClient containerClient = blobstorage.getclient();
        // System.out.println("多余的DM url");
        // System.out.println(DMdict.toString());
        //  https://expoverseazureblobdb.blob.core.windows.net/test-ctn1/11r4jdl3sq400.jpg
        for (String url:DMdict.keySet()){
            String name = string_proc.extractFileName(url); //11r4jdl3sq400.jpg
            BlobClient blobClient = containerClient.getBlobClient(name);
            blobClient.deleteIfExists(); //
        }
        resourceMapper.deleteurlbyprodid(prodid);
        for (URL url:urlList){
            resourceMapper.inserturlbyprodid(url);
        }
    }

    @Override
    @Transactional
    public String updateExhbMovie(MultipartFile file, String exhbid, String name) throws IOException {
        List<ExhbMovie> exhbMovies = resourceMapper.getExhbMovieURLbyExhbid(exhbid);
        String oldurl = exhbMovies.get(0).getUrl();
        String oldname = string_proc.extractFileName(oldurl);
        BlobContainerClient containerClient = blobstorage.getclient();
        BlobClient blobClient = containerClient.getBlobClient(name);
        BlobClient oldClient = containerClient.getBlobClient(oldname);
        oldClient.deleteIfExists();// 删除旧的视频
        byte[] imageBytes = file.getBytes();
        blobClient.upload(BinaryData.fromBytes(imageBytes));
        System.out.println("上传了新视频"+name);
        String accountName = blobstorage.accountName();
        String containerName = blobstorage.containerName();
        String src = String.format("https://%s.blob.core.windows.net/%s/%s",
                accountName,containerName,name);
        resourceMapper.deleteurlbyexhbmovie(exhbid);
        URL url = new URL(src,"movie", "", 1);
        url.setExhibition_id(exhbid);
        resourceMapper.inserturlbyexhbmovie(url);
        return String.format("https://%s.blob.core.windows.net/%s/%s?%s",
                accountName,containerName,name,gettempSAS());
    }
    /*
    @Override
    @Transactional
    public void updateExhbMovie(MovieUpdate movieUpdate) throws IOException {
        String exhbid = movieUpdate.getExhbid();
        List<UploadMovie> uploadMovies = movieUpdate.getUploadMovielist();
        List<URL> urlList = new ArrayList<>();
        int file_no = 1;
        for (UploadMovie uploadMovie:uploadMovies){
            if(uploadMovie.getsrc().startsWith("data:")){
                //上传的新图片需要传进azure blob storage数据库中
                String base64Image = uploadMovie.getsrc();
                String imagename = uploadMovie.getName();
                String outputPath = String.format("%s.jpg",imagename);
                BlobContainerClient containerClient = blobstorage.getclient();
                BlobClient blobClient = containerClient.getBlobClient(outputPath);

                String base64Data = base64Image.substring(base64Image.indexOf(',') + 1);
                base64Data = base64Data.replaceAll("\r|\n", "");
                base64Data = base64Data.trim();
                // 将Base64数据解码为字节数组
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                // 将字节数组转换为二进制
                //InputStream inputStream = file.getInputStream();
                blobClient.upload(BinaryData.fromBytes(imageBytes));
                System.out.println("上传了新图片"+outputPath);
                String accountName = blobstorage.accountName();
                String containerName = blobstorage.containerName();
                String tempsrc = String.format("https://%s.blob.core.windows.net/%s/%s",
                        accountName,containerName,outputPath);
                // https://expoverseazureblobdb.blob.core.windows.net/test-ctn1/121glj390n980.jpg
                urlList.add(new URL(tempsrc,"image",prodid,file_no));
            }
            else{
                System.out.println(uploadDM.getsrc());
                urlList.add(new URL(uploadDM.getName(),"image", prodid, file_no));
            }
            file_no+=1;
        }
        resourceMapper.deleteurlbyprodid(prodid);
        for (URL url:urlList){
            resourceMapper.inserturlbyprodid(url);
        }
    }
    */


    @Override
    @Transactional
    public String updateProdMovie(MultipartFile file, String exhbid, String name) throws IOException {
        List<ProdMovie> prodMovies = resourceMapper.getProdMovieURLbyExhbid(exhbid);
        String oldurl = prodMovies.get(0).getUrl();
        String oldname = string_proc.extractFileName(oldurl);
        BlobContainerClient containerClient = blobstorage.getclient();
        BlobClient blobClient = containerClient.getBlobClient(name);
        BlobClient oldClient = containerClient.getBlobClient(oldname);
        oldClient.deleteIfExists(); // 删除旧的视频
        byte[] imageBytes = file.getBytes();
        blobClient.upload(BinaryData.fromBytes(imageBytes));
        System.out.println("上传了新视频"+name);
        String accountName = blobstorage.accountName();
        String containerName = blobstorage.containerName();
        String src = String.format("https://%s.blob.core.windows.net/%s/%s",
                accountName,containerName,name);
        resourceMapper.deleteurlbyprodmovie(exhbid);
        URL url = new URL(src,"movie", "", 1);
        url.setExhibition_id(exhbid);
        resourceMapper.inserturlbyprodmovie(url);
        return String.format("https://%s.blob.core.windows.net/%s/%s?%s",
                accountName,containerName,name,gettempSAS());
    }

    @Override
    @Transactional
    public String uploadDM(MultipartFile file) throws IOException{
        byte[] imageBytes = file.getBytes();
        return "data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(imageBytes);
    }
}
