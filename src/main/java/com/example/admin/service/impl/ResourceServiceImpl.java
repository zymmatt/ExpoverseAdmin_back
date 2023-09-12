package com.example.admin.service.impl;

import com.example.admin.entity.Resource.*;
import com.example.admin.service.ResourceService;
import com.example.admin.mapper.ResourceMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

import java.io.*;
import java.util.*;
import java.time.OffsetDateTime;


@Service
public class ResourceServiceImpl implements ResourceService{

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<product> getAllProduct() {

        return null;
    }

    @Override
    public List<product> getProdListbyExhbid(String exhbid) {
        return resourceMapper.getProdListbyExhbid(exhbid);
    }

    @Override
    public List<DM> getDMURLbyProdid(String prodid) {
        return resourceMapper.getDMURLbyProdid(prodid);
    }

    @Override
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

}
