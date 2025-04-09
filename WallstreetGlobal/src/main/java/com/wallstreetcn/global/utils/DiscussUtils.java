package com.wallstreetcn.global.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscussUtils {

    public static String getDiscussTitle(String content) {
        if (TextUtils.isEmpty(content))
            return "";
        try {
            Pattern pattern = Pattern.compile("#(.*?)#");// 匹配的模式
            Matcher m = pattern.matcher(content);
            while (m.find()) {
                String text = "#" + m.group(1) + "#";
                if (content.startsWith(text))
                    return text;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
