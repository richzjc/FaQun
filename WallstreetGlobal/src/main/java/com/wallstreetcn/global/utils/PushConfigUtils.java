package com.wallstreetcn.global.utils;

import com.wallstreetcn.helper.utils.SharedPrefsUtil;

public class PushConfigUtils {

    private static final String PUSH_ADAPTER_TOGGLE = "PushToggle";

    public static void setPushStartVersion(String version) {
        SharedPrefsUtil.saveString(PUSH_CONFIG, "wscn_push_config_start_version", version);
    }

    public static String getPushStartVersion() {
        return SharedPrefsUtil.getString(PUSH_CONFIG, "wscn_push_config_start_version", "");
    }

    public static boolean isPushEnable() {
        return true;
    }

    public static boolean getPushEnable(){
        if(PushDeviceUtils.isMiuiOrEmUui()){
            return SharedPrefsUtil.getBoolean(PUSH_ADAPTER_TOGGLE, false);
        }else{
            return SharedPrefsUtil.getBoolean(PUSH_ADAPTER_TOGGLE, true);
        }

    }
    public static void setPushEnable(boolean isEnable) {
        SharedPrefsUtil.save(PUSH_ADAPTER_TOGGLE, isEnable);
    }

    //个性化推送配置
    private static final String PUSH_CONFIG = "wscn_push_config";

    public static boolean getThemePush() {
        return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "theme_push_config", true);//默认开启
    }

    public static void setMarketPush(boolean open) {
        SharedPrefsUtil.save(PUSH_CONFIG, "market_push_config", open);
    }

    public static boolean getMarketPush() {
        if(PushDeviceUtils.isMiuiOrEmUui()){
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "market_push_config", false);//默认开启
        }else{
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "market_push_config", true);//默认开启
        }
    }

    public static void setPremiumTopicPush(boolean open) {
        SharedPrefsUtil.save(PUSH_CONFIG, "premium_topic_push_config", open);
    }

    public static void setAuthorPush(boolean open) {
        SharedPrefsUtil.save(PUSH_CONFIG, "author_push_config", open);
    }

    public static void setVipPush(boolean open) {
        SharedPrefsUtil.save(PUSH_CONFIG, "vip_push_config", open);
    }

    public static void setWachDepth(boolean open){
        SharedPrefsUtil.save(PUSH_CONFIG, "wscn_depth_config", open);
    }

    public static void setKeywordsPush(boolean open) {
        SharedPrefsUtil.save(PUSH_CONFIG, "keywords_push_config", open);
    }

    public static boolean getPremiumTopicPush() {
        if(PushDeviceUtils.isMiuiOrEmUui()){
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "premium_topic_push_config", false);//默认开启
        }else{
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "premium_topic_push_config", true);//默认开启
        }
    }

    public static boolean getVipPush() {
        if(PushDeviceUtils.isMiuiOrEmUui()){
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "vip_push_config", false);//默认开启
        }else{
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "vip_push_config", true);//默认开启
        }
    }

    public static boolean getWacnDepth() {
        if(PushDeviceUtils.checkEMUI()){
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "wscn_depth_config", false);//默认开启
        }else{
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "wscn_depth_config", true);//默认开启
        }

    }

    public static boolean getKeywordsPush() {
        if(PushDeviceUtils.isMiuiOrEmUui()){
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "keywords_push_config", false);//默认开启
        }else{
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "keywords_push_config", true);//默认开启
        }
    }

    public static boolean getAuthorPush() {
        if(PushDeviceUtils.isMiuiOrEmUui()){
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "author_push_config", false);//默认开启
        }else{
            return SharedPrefsUtil.getBoolean(PUSH_CONFIG, "author_push_config", true);//默认开启
        }
    }

    public static String[] levels = new String[]{"wscn_high", "wscn_middle", "wscn_low"};

    public static int DEFAULT_FREQ = 1;

    public static String getPushFrequency() {
        return SharedPrefsUtil.getString(PUSH_CONFIG, "wscn_push_config_frequency", levels[DEFAULT_FREQ]);
    }

    public static void setPushConfigFrequency(String frequency) {
        //wscn_high，wscn_low，wscn_middle 如果关闭推送就是空
        SharedPrefsUtil.getPrefs(PUSH_CONFIG).edit().putString("wscn_push_config_frequency", frequency).commit();
    }
}
