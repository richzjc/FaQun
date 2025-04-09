package com.wallstreetcn.helper.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.wallstreetcn.helper.BuildConfig;
import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.system.EquipmentUtils;

public class UriUtil {

    public static String addParamsToUrl(String originUrl, String key, String value) {
        if (TextUtils.isEmpty(originUrl) || TextUtils.isEmpty(key))
            return originUrl;
        else {
            Uri uri = Uri.parse(originUrl);
            String url = uri.buildUpon().appendQueryParameter(key, value).build().toString();
            return url;
        }
    }

    public static String addScheme(String url) {
        if (url.isEmpty()) return "";
        Uri uri = Uri.parse(url);
        if (TextUtils.isEmpty(uri.getScheme()))
            url = "https://" + url;
        return url;
    }

    public final static String ORIGINAL_BASE_HOST = "wallstreetcn.com";
    public final static String ORIGINAL_BASE_HOST_SIT = "frontend-sit.wallstreetcn.com";
    public final static String VIP_HOST = "vip.jianshiapp.com";
    public final static String VIP_HOST_SIT = "vip-sit.jianshiapp.com";

    public static String urlToRouter(String sourceUrl) {
        String url = sourceUrl.trim();
        url = url.replaceFirst("ivanka.wallstreetcn.com", ORIGINAL_BASE_HOST);

        Uri sourceUri = Uri.parse(url);
        String sourceHost = sourceUri.getHost();

        if (TextUtils.isEmpty(sourceHost)) return url;
        String baseHost = ResourceUtils.getResStringFromId(R.string.replaced_router_host);
        if (sourceHost.contains(baseHost)) {
            url = url.replaceFirst(sourceHost, ORIGINAL_BASE_HOST);
        }
        if (sourceHost.contains(VIP_HOST)) {
            url = url.replaceFirst(sourceHost, ORIGINAL_BASE_HOST);
        }
        if (BuildConfig.DEBUG) {
            String ip = SharedPrefsUtil.getDevelopHtmlIp();
            if (TextUtils.equals(ip, String.format("%s:%s", sourceHost, sourceUri.getPort())))
                url = sourceUrl.replaceFirst(ip, ORIGINAL_BASE_HOST);
        }
        return url;
    }

    public static String urlToWebUrl(String sourceUrl) {
        String url = sourceUrl.trim();
        url = url.replaceFirst("ivanka.wallstreetcn.com", ORIGINAL_BASE_HOST);
        String sourceHost = Uri.parse(url).getHost();
        if (TextUtils.isEmpty(sourceHost))
            return sourceUrl;
        //子域名不替换
        // vip app wallstreetcn.com 替换为vip.jianshiapp.com
        boolean debug = EquipmentUtils.getVersionName().contains("debug");
        String wscnHost = debug ? ORIGINAL_BASE_HOST_SIT : ORIGINAL_BASE_HOST;
        String vipHost = debug ? VIP_HOST_SIT : VIP_HOST;
        if (Util.isWscnVip() && sourceHost.contains(ORIGINAL_BASE_HOST)) {
//            String host = ResourceUtils.getResStringFromId(R.string.replaced_router_host);
            sourceUrl = sourceUrl.replaceFirst(wscnHost, vipHost);
        } else if (sourceHost.contains(vipHost)) {
//            String host = ResourceUtils.getResStringFromId(R.string.replaced_router_host);
            sourceUrl = sourceUrl.replaceFirst(vipHost, wscnHost);
        }
        String scheme = Uri.parse(sourceUrl).getScheme();
        if (TextUtils.equals("wscn", scheme))
            sourceUrl = sourceUrl.replaceFirst(scheme, "https");
        return sourceUrl;
    }
}
