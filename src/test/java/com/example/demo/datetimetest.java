package com.example.demo;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class datetimetest {
    public static void main(String[] args) throws IOException
    {
        String startDate = "1698249226";
        long startday = Long.parseLong(startDate);
        LocalDateTime start = Instant.ofEpochSecond(startday).atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println(start);

    }
}

