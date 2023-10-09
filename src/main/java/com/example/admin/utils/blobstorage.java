package com.example.admin.utils;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.*;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;


public class blobstorage {
    public static String accountName(){
        return "expoverseazureblobdb";
    }

    public static String containerName(){
        return "test-ctn1";
    }

    public static BlobContainerClient getclient(){
        String accountName = accountName();
        String accountKey = "MkEhnlU9L67tVooxRCcY4XmUL5yMSKfY1Mba9xjj3OpcrLg18g4R3rDXQgacHKa2AMg4QuocPUaP+AStXFGjyQ==";
        String connectionString = String.format("DefaultEndpointsProtocol=https;AccountName=%s;" +
                "AccountKey=%s;EndpointSuffix=core.windows.net",accountName, accountKey);
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().
                connectionString(connectionString).buildClient();
        String containerName = containerName(); // 替换为您的容器名称
        return blobServiceClient.getBlobContainerClient(containerName);
    }

    public static String gettempSAS(BlobContainerClient containerClient){
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);
        // Assign read permissions to the SAS token
        BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);
        BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                .setStartTime(OffsetDateTime.now().minusMinutes(5));
        return containerClient.generateSas(sasSignatureValues);
    }

    public static byte[] workbookToByteArray(Workbook workbook) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert workbook to byte array", e);
        }
    }



}
