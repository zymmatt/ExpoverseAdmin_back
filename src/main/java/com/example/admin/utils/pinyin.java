package com.example.admin.utils;
import net.sourceforge.pinyin4j.PinyinHelper;
public class pinyin
{
    public static boolean containsChinese(String str) {
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static String convertToPinyin(String str) {
        StringBuilder pinyin = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    pinyin.append(pinyinArray[0].substring(0, pinyinArray[0].length() - 1)); // 把拼音去掉了
                    System.out.println(pinyin);
                }
            } else {
                pinyin.append(c); // 非中文字符直接添加
            }
        }
        return pinyin.toString();
    }

    private static boolean isChinese(char c) {
        return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
    }

    public static void main(String[] args) {
        String input = "你好，Hello World!";

        if (containsChinese(input)) {
            String pinyin = convertToPinyin(input);
            System.out.println("包含中文，转换成拼音：" + pinyin);
        } else {
            System.out.println("不包含中文");
        }
    }
}
