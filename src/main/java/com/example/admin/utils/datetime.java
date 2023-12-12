package com.example.admin.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class datetime {
    private datetime(){} // 私有构造函数，防止实例化

    public static String timestamp2str(long timestamp){
        // 秒为单位的时间戳转换成2021-09-19 12:13:20的字符串
        // 一个时间戳，以秒为单位

        // 将时间戳（秒）转换为Instant对象
        Instant instant = Instant.ofEpochSecond(timestamp);

        // 使用ZoneId来指定时区，创建一个LocalDateTime对象
        ZoneId zoneId = ZoneId.systemDefault(); // 使用系统默认时区
        LocalDateTime dateTime = instant.atZone(zoneId).toLocalDateTime();
        return dateTime.toString();
    }

    public static long timestampafter7days(int timestamp){
        // 把一个时间戳延后7天,用于判断一周内是否有填写过调查问卷
        Instant initialInstant = Instant.ofEpochSecond(timestamp);
        Duration duration = Duration.ofDays(7);
        Instant newInstant = initialInstant.plus(duration);
        return newInstant.getEpochSecond();
    }

}
