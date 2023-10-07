package com.example.demo;

import com.azure.identity.*;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

import java.io.*;
import java.time.OffsetDateTime;


public class testblob {
    public static void uploadblob(BlobContainerClient containerClient){
        // Create a local file in the ./data/ directory for uploading and downloading
        String localPath = "D:\\inventecWork\\expoverse\\台北展厅影片\\product\\";
        File directory = new File(localPath);
        File[] files = directory.listFiles();
        for (File file : files) {
            //String fileName = "InteCar_AIBox_1.jpg";
            String fileName = file.getName();
                    // Get a reference to a blob
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());

            // Upload the blob
            blobClient.uploadFromFile(localPath + fileName);
        }

    }

    public static void listblob(BlobContainerClient containerClient){
        for (BlobItem blobItem : containerClient.listBlobs()) {
            System.out.println("\t" + blobItem.getName());
        }
    }

    public static void downloadblob(BlobContainerClient containerClient,
                                    String fileName, String localPath){
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        System.out.println("\nDownloading blob to\n\t " + localPath + fileName);
        blobClient.downloadToFile(localPath + fileName);
    }

    public static String SASauthorize(BlobContainerClient containerClient){
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);

        // Assign read permissions to the SAS token
        BlobSasPermission sasPermission = new BlobSasPermission()
                .setReadPermission(true);

        BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                .setStartTime(OffsetDateTime.now().minusMinutes(5));

        return containerClient.generateSas(sasSignatureValues);

    }

    public static void main(String[] args) throws IOException
    {
        String accountName = "expoverseazureblobdb";
        String accountUrl = String.format("https://%s.blob.core.windows.net",accountName);
        String accountKey = "MkEhnlU9L67tVooxRCcY4XmUL5yMSKfY1Mba9xjj3OpcrLg18g4R3rDXQgacHKa2AMg4QuocPUaP+AStXFGjyQ==";
        String connectionString = String.format("DefaultEndpointsProtocol=https;AccountName=%s;" +
                "AccountKey=%s;EndpointSuffix=core.windows.net",accountName, accountKey);
        // 替换为您的Azure Blob Storage连接字符串

        String containerName = "test-ctn1"; // 替换为您的容器名称
        String blobName = "11p5gb505vqg0.jpg"; // 替换为要上传的Blob名称
        //DefaultAzureCredential defaultCredential = new DefaultAzureCredentialBuilder().build();
        //BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
        //        .endpoint(accountUrl)
        //        .credential(defaultCredential)
        //        .buildClient();
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().
                connectionString(connectionString).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.deleteIfExists(); //
        // BlobClient blobClient = containerClient.getBlobClient(blobName);
        // System.out.println(SASauthorize(containerClient));

        //uploadblob(containerClient);

        //listblob(containerClient);

        //System.out.println(connectionString);
        // downloadblob(containerClient,"5G-AMR.mp4","D:\\inventecWork\\expoverse\\");
        // Quickstart code goes here
    }

}
