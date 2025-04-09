package com.wallstreetcn.helper.utils.text;

import android.text.TextUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者： 巴掌 on 15/11/2 14:11
 */
public class TextUtil {

    private static boolean regexCheck(String text, String regex) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(regex)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.trim());
        return matcher.matches();
    }

    public static boolean regexCheckEmail(String text) {
        final String EMAIL_REGEX = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        return regexCheck(text, EMAIL_REGEX);
    }

    /**
     * 去掉换行符之类的空白文本
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (!TextUtils.isEmpty(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            return dest;
        }
        return str;
    }


    public static String format(String format, Object... args) {
        return String.format(Locale.CHINA, format, args);
    }

    public static String elect(String... strings) {
        for (String s : strings) {
            if (!TextUtils.isEmpty(s))
                return s;
        }
        return "";
    }

    public static String replace(String selectText) {
        int index = selectText.indexOf("\n\n");
        while (index >= 0) {
            selectText = selectText.replace("\n\n", "\n");
            index = selectText.indexOf("\n\n");
        }
        return selectText;
    }
}
