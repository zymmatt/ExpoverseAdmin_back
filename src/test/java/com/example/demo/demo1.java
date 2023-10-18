package com.example.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.admin.entity.User.LanguageWord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;


public class demo1 {

    public static void main(String[] args) throws IOException
    {
        /*
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建一个工作表
        Sheet sheet = workbook.createSheet("Sheet1");
        // 创建一个单元格，并合并它
        // CellRangeAddress mergedRegion = ;
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 9));
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("姓名");
        headerRow.createCell(1).setCellValue("訪問日期");
        headerRow.createCell(2).setCellValue("訪問時間");
        headerRow.createCell(3).setCellValue("榮譽墻");
        headerRow.createCell(4).setCellValue("專利墻");
        headerRow.createCell(5).setCellValue("智慧醫療");
        Row headerRow2 = sheet.createRow(1);
        headerRow2.createCell(6).setCellValue("ATC63E");
        headerRow2.createCell(7).setCellValue("M215 T");

        // 保存工作簿到文件
        FileOutputStream fos = new FileOutputStream("excel_example.xlsx");
        workbook.write(fos);
        fos.close();

        // 关闭工作簿
        workbook.close();
        */
        // JSON 文件路径
        String filePath = "./LanguageData.json";

        try {
            // 创建 ObjectMapper 对象
            ObjectMapper objectMapper = new ObjectMapper();

            // 从文件中读取 JSON 数据并将其解析为 Object（通常是 Map 或 List）
            Object jsonData = objectMapper.readValue(new File(filePath), Object.class);
            // 将 JSON 数据原封不动地输出
            String jsonStr = objectMapper.writeValueAsString(jsonData);
            System.out.println(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
