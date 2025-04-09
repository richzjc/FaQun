package com.wallstreetcn.rpc.host;

import android.text.TextUtils;

import com.wallstreetcn.rpc.ServerAPI;

/**
 * Created by zhangyang on 16/6/23.
 */
public class HostManager {
    private static String BaseUrl = ServerAPI.sBase;

    public static void setBaseUrl(String BASEURL) {
        HostManager.BaseUrl = BASEURL;
    }

    public static String getBaseUrl() {
        return BaseUrl;
    }


    private static String environment = "prod";

    public static void setEnvironment(String environment) {
        HostManager.environment = environment;
    }

    public static String getEnvironment() {
        return TextUtils.isEmpty(environment) ? "prod" : environment;
    }
}
