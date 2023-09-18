package com.example.demo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneOffset;

public class demo1 {

    public static void main(String[] args) throws IOException
    {
        LocalDate localDate = LocalDate.now(); // 当前日期



        // 将LocalDate转换为Instant 获取时间戳（以秒为单位）
        long timestampInSeconds = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().getEpochSecond();

        System.out.println("LocalDate: " + localDate);
        System.out.println("Timestamp (seconds): " + timestampInSeconds);


    }
}
