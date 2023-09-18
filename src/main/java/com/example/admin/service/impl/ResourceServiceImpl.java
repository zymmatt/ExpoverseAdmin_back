package com.example.admin.service.impl;

import com.azure.core.util.BinaryData;
import com.example.admin.entity.Resource.*;
import com.example.admin.service.ResourceService;
import com.example.admin.mapper.ResourceMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

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
    public List<Product> getAllProduct() {

        return null;
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
    public String gettempSAS(){
        String accountName = "expoverseazureblobdb";
        String accountKey = "MkEhnlU9L67tVooxRCcY4XmUL5yMSKfY1Mba9xjj3OpcrLg18g4R3rDXQgacHKa2AMg4QuocPUaP+AStXFGjyQ==";
        String connectionString = String.format("DefaultEndpointsProtocol=https;AccountName=%s;" +
                "AccountKey=%s;EndpointSuffix=core.windows.net",accountName, accountKey);
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().
                connectionString(connectionString).buildClient();
        String containerName = "test-ctn1"; // 替换为您的容器名称
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);
        // Assign read permissions to the SAS token
        BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);
        BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                .setStartTime(OffsetDateTime.now().minusMinutes(5));
        return containerClient.generateSas(sasSignatureValues);
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
                String accountName = "expoverseazureblobdb";
                String accountKey = "MkEhnlU9L67tVooxRCcY4XmUL5yMSKfY1Mba9xjj3OpcrLg18g4R3rDXQgacHKa2AMg4QuocPUaP+AStXFGjyQ==";
                String connectionString = String.format("DefaultEndpointsProtocol=https;AccountName=%s;" +
                        "AccountKey=%s;EndpointSuffix=core.windows.net",accountName, accountKey);
                BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().
                        connectionString(connectionString).buildClient();
                String containerName = "test-ctn1"; // 替换为您的容器名称
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
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


}
