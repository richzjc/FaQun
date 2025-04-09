package com.wallstreetcn.global.utils;

import android.text.TextUtils;
import com.wallstreetcn.helper.utils.TLog;

import com.wallstreetcn.baseui.manager.ResourceConfigManager;

public class I18nUtils {

    private static final String LAN_ZH = "zh";
    private static final String LAN_TW = "zh-tw";
    private static final String LOCAL_LAN_TW = "zh_tw";

    /**
     * 头部加X-Language (zh, en, zh-tw)
     * <p>
     * zh_CN_#Hans
     * zh_TW_#Hant
     * zh_MO_#Hant
     * zh_HK_#Hant
     * zh_TW
     * zh_CN
     *
     * @return
     */
    public static String getI18nLanguage() {
        String lan = ResourceConfigManager.getLanguageMode().toString().toLowerCase();
        TLog.i("I18nUtils", lan);
        if (lan.contains("zh_") && !lan.contains("zh_cn")) {
            return LAN_TW;
        }
        return LAN_ZH;
    }

    public static boolean languageIsTw() {
        return TextUtils.equals(LAN_TW, getI18nLanguage());
    }
}
