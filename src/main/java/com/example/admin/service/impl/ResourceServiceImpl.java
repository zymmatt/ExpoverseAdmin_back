package com.example.admin.service.impl;


import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.example.admin.entity.Resource.*;
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

@Service
public class ResourceServiceImpl implements ResourceService{

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    @Transactional
    public List<ExhbSrc> getAllResource() {
        List<String> exhbidlist = resourceMapper.getallExhbid();
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


    @Override
    @Transactional
    public String gettempSAS(){
        return blobstorage.gettempSAS(blobstorage.getclient());
    }

    @Override
    @Transactional
    public void uploadDMdict(ProdUpdate prodUpdate) throws IOException {
        String prodid = prodUpdate.getProdid();
        List<UploadDM> uploadDMs = prodUpdate.getUploadDMlist();
        List<URL> urlList = new ArrayList<>();
        int file_no = 1;
        for (UploadDM uploadDM:uploadDMs){
            if(uploadDM.getsrc().startsWith("data:")){
                //上传的新图片需要传进azure blob storage数据库中
                String base64Image = uploadDM.getsrc();
                String imagename = uploadDM.getName();
                String outputPath = String.format("%s.jpg",imagename);
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

    @Override
    @Transactional
    public String updateExhbMovie(MultipartFile file, String exhbid, String name) throws IOException {
        BlobContainerClient containerClient = blobstorage.getclient();
        BlobClient blobClient = containerClient.getBlobClient(name);
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
        BlobContainerClient containerClient = blobstorage.getclient();
        BlobClient blobClient = containerClient.getBlobClient(name);
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


}
