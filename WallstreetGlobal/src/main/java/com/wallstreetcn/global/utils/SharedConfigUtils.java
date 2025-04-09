package com.wallstreetcn.global.utils;

import android.text.TextUtils;

import com.wallstreetcn.helper.utils.SharedPrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class SharedConfigUtils {

    public static boolean isAllowNoWifiPlay() {
        return true;
    }

    public static void saveMobileTxBytes(long upload) {
        SharedPrefsUtil.saveLong("MobileTxBytes", upload);
    }

    public static long getMobileTxBytes() {
        return SharedPrefsUtil.getLong("MobileTxBytes");
    }

    public static void saveMobileRxBytes(long upload) {
        SharedPrefsUtil.saveLong("MobileRxBytes", upload);
    }

    public static long getMobileRxBytes() {
        return SharedPrefsUtil.getLong("MobileRxBytes");
    }

    public static boolean showTaskNews() {
        return SharedPrefsUtil.getBoolean("config", "showTaskNews", true);
    }

    public static boolean isShowRecommendDot() {
        return SharedPrefsUtil.getBoolean("config", "ShowRecommendDot", true);
    }

    public static void setShowRecommendDot() {
        SharedPrefsUtil.save("config", "ShowRecommendDot", false);
    }

    public static String getServiceWeChat() {
        return getServiceValueKey("contact", "plus-answerqueen");
    }

    public static void setServiceInfo(String info) {
        SharedPrefsUtil.saveString("service_info", info);
    }

    public static String getServiceValueKey(String key, String def) {
        String info = SharedPrefsUtil.getString("service_info", "");
        if (TextUtils.isEmpty(info))
            return def;
        try {
            JSONObject object = new JSONObject(info);
            if (object.has(key)) {
                String value = object.getString(key);
                if (!TextUtils.isEmpty(value))
                    return value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return def;
    }

    public static void setShanyanEnable(boolean enable) {
        SharedPrefsUtil.saveBoolean("shanyan_enable", enable);
    }

    public static boolean getShanyanEnable() {
        return SharedPrefsUtil.getBoolean("shanyan_enable");
    }

    public static void setVipLiveThemeTitle(String title) {
        SharedPrefsUtil.saveString("vip_live_theme_title", title);
    }

    public static String getVipLiveThemeTitle() {
        return SharedPrefsUtil.getString("vip_live_theme_title", "");
    }

    public static void setVipLiveThemeUri(String uri) {
        SharedPrefsUtil.saveString("vip_live_theme_uri", uri);
    }

    public static void setAuthorConfig(String author) {
        SharedPrefsUtil.saveString("authorConfig", author);
    }

    public static String getVipLiveThemeUri() {
        return SharedPrefsUtil.getString("vip_live_theme_uri", "");
    }

    public static boolean isThemeFollowClose(String themeId) {
        return SharedPrefsUtil.getBoolean("theme_news_live_follow", themeId, false);
    }

    public static void setThemeFollowClose(String themeId) {
        SharedPrefsUtil.save("theme_news_live_follow", themeId, true);
    }
}
