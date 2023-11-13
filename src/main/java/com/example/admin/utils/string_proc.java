package com.example.admin.utils;

public class string_proc {
    public static String extractFileName(String url) {
        // 找到最后一个斜杠的位置
        int lastSlashIndex = url.lastIndexOf("/");

        // 如果找到了斜杠，则从斜杠位置+1开始截取字符串，即为文件名
        if (lastSlashIndex != -1) {
            return url.substring(lastSlashIndex + 1);
        } else {
            // 如果没有斜杠，则返回原始字符串
            return url;
        }
    }

    public static boolean containsAtLeastTwoLetters(String input) {
        int letterCount = 0;
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                letterCount++;
                if (letterCount >= 2) {
                    return true;
                }
            }
        }
        return false;
    }


}
