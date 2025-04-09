package com.wallstreetcn.rpc;

import com.wallstreetcn.rpc.host.UrlPacker;

import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

/**
 * Created by  Leif Zhang on 2017/9/14.
 * Email leifzhanggithub@gmail.com
 */

public class IpUtils {
    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        String ipLine = "";
        try {
            String url = UrlPacker.pack("apiv1/clientip");
            RpcStringRequest request = new RpcStringRequest(url);
            Response response = VolleyQueue.getInstance().sync(request);
            int responseCode = response.code();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Pattern pattern = Pattern
                        .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Matcher matcher = pattern.matcher(response.body().string());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipLine;
    }
}
