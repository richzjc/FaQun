package com.wallstreetcn.rpc.host;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;

import com.wallstreetcn.helper.utils.TLog;

/**
 * Created by zhangyang on 16/6/23.
 */
public class UrlPacker {
    public static String pack(@Nullable String host) {
        return pack(host, null);
    }

    public static String pack(@Nullable String host, Map<String, String> params) {
        String url = String.format("%s/%s", HostManager.getBaseUrl(), host);
        if (!checkUrl(url)) {
            url += "?";
        } else if (!url.endsWith("&")){
            url += "&";
        }
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    TLog.e("dddddd " + value);
                    e.printStackTrace();
                }
                url += String.format("%s=%s&", entry.getKey(), value);
            }
            return url.substring(0, url.length() - 1);
        } else {
            return url;
        }
    }

    public static String packNoHost(@Nullable String url, Map<String, String> params) {
        if (!checkUrl(url)) {
            url += "?";
        } else if (!url.endsWith("&")) {
            url += "&";
        }
        if (params != null && !params.isEmpty()) {
            StringBuilder urlBuilder = new StringBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                urlBuilder.append(String.format("%s=%s&", entry.getKey(), value));
            }
            url = urlBuilder.toString();
            return url.substring(0, url.length() - 1);
        } else {
            return url;
        }
    }

    private static boolean checkUrl(String url) {
        Uri uri = Uri.parse(url);
        Set<String> para = uri.getQueryParameterNames();
        return para.size() > 0;
    }
}
