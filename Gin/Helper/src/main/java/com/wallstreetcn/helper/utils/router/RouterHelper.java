package com.wallstreetcn.helper.utils.router;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.appcompat.view.ContextThemeWrapper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leif Zhang on 16/8/1.
 * Email leifzhanggithub@gmail.com
 */
public class RouterHelper {

    public static String getSuffix(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }
        String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|txt|html|zip|java|doc|docx|PDF|pdf|xls|xlsx";
        Pattern pat = Pattern.compile("[\\w]+[\\.](" + suffixes + ")");
        Matcher mc = pat.matcher(url);
        String suffix = null;
        while (mc.find()) {
            suffix = mc.group();//截取文件名后缀名
        }
        if (!TextUtils.isEmpty(suffix)) {
            suffix = suffix.substring(suffix.indexOf("."));
        } else {
            suffix = ".temp";
        }

        return suffix;
    }


    public static Activity getActivity(Context context) {
        if (context instanceof Application) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }

        if (context instanceof ContextThemeWrapper) {
            return getActivity(((ContextThemeWrapper) (context)).getBaseContext());
        }

        if (context instanceof android.view.ContextThemeWrapper) {
            return getActivity(((android.view.ContextThemeWrapper) context).getBaseContext());
        }

        return null;
    }

}
