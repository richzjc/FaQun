package com.wallstreetcn.helper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.wallstreetcn.helper.BuildConfig;

import java.util.Set;

/**
 * Created by zhangjianchuan on 2016/6/23.
 */
public class SharedPrefsUtil {

    public static String SHARED_PREFS_FILE_NAME = "shared_preference_config";
    public static final String CHECK_HUAWEI_PRO="CHECK_HUAWEI_PRO";
    public static final String SHARE_PREFS_LOGIN = "login";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPrefs() {
        return getPrefs(UtilsContextManager.getInstance().getApplication());
    }

    public static SharedPreferences getPrefs(String preName) {
        return getPrefs(UtilsContextManager.getInstance().getApplication(), preName);
    }

    public static SharedPreferences getPrefs(Context context, String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    //Integers
    public static void saveInt(Context context, String prefName, String key, int value) {
        getPrefs(context, prefName).edit().putInt(key, value).apply();
    }


    public static int getInt(Context context, String prefName, String key, int defaultValue) {
        return getPrefs(context, prefName).getInt(key, defaultValue);
    }

    //Booleans
    public static void save(String key, boolean value) {
        getPrefs().edit().putBoolean(key, value).apply();
    }


    public static void save(String prefName, String key, boolean value) {
        getPrefs(prefName).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return getPrefs().getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getPrefs().getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(String prefName, String key, boolean defaultValue) {
        return getPrefs(prefName).getBoolean(key, defaultValue);
    }

    //Strings
    public static void saveString(String prefName, String key, String value) {
        getPrefs(prefName).edit().putString(key, value).apply();
    }

    public static String getString(String prefName, String key, String defaultValue) {
        return getPrefs(prefName).getString(key, defaultValue);
    }

    public static void saveString(String key, String value) {
        getPrefs().edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return getPrefs().getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return getPrefs().getString(key, defaultValue);
    }

    //Integers
    public static void saveInt(String prefName, String key, int value) {
        getPrefs(prefName).edit().putInt(key, value).apply();
    }

    public static int getInt(String prefName, String key, int defaultValue) {
        return getPrefs(prefName).getInt(key, defaultValue);
    }

    public static void saveInt(String key, int value) {
        getPrefs().edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return getPrefs().getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return getPrefs().getInt(key, defaultValue);
    }

    //Floats
    public static void saveFloat(String key, float value) {
        getPrefs().edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key) {
        return getPrefs().getFloat(key, 0);
    }

    public static float getFloat(String key, float defaultValue) {
        return getPrefs().getFloat(key, defaultValue);
    }

    //Longs
    public static void saveLong(String key, long value) {
        getPrefs().edit().putLong(key, value).apply();
    }

    public static void saveLong(String prefName, String key, long value) {
        getPrefs(prefName).edit().putLong(key, value).apply();
    }

    public static void saveLongRightNow(String prefName, String key, long value) {
        getPrefs(prefName).edit().putLong(key, value).commit();
    }

    public static long getLong(String prefName, String key, long defaultValue) {
        return getPrefs(prefName).getLong(key, defaultValue);
    }

    public static long getLong(String key) {
        return getPrefs().getLong(key, 0);
    }

    public static long getLong(String key, long defaultValue) {
        return getPrefs().getLong(key, defaultValue);
    }

    //StringSets
    public static void saveStringSet(String key, Set<String> value) {
        getPrefs().edit().putStringSet(key, value).apply();
    }

    public static Set<String> getStringSet(String key) {
        return getPrefs().getStringSet(key, null);
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        return getPrefs().getStringSet(key, defaultValue);
    }

    public static void saveBoolean(String key, Boolean value){
        getPrefs().edit().putBoolean(key, value).apply();
    }

    public static Boolean getIsNightMode() {
      return  Util.getIsNightMode();
    }

    public static boolean getIsGreenColor() {
        return getBoolean("config", "isGreenColor", false);
    }

    public static String getDevelopHtmlIp() {
        if (BuildConfig.DEBUG && getBoolean("develop_html_ip_enable", false)) {
            return getString("develop_html_ip", "");
        }
        return "";
    }
}
