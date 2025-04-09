package com.wallstreetcn.helper.utils.data;

import android.content.Context;
import android.text.TextUtils;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leif Zhang on 2016/10/17.
 * Email leifzhanggithub@gmail.com
 */
public class TraceUtils {

    public static void onEvent(Context context, String eventId, HashMap<String, String> map) {
        if (TextUtils.isEmpty(eventId)) {
            return;
        }

        try {
            JSONObject properties = new JSONObject();
            if(map != null){
                for (String s : map.keySet()) {
                    properties.put(s, map.get(s));
                }
            }
            SensorsDataAPI.sharedInstance().track(eventId, properties);
            // 强制发送数据
            SensorsDataAPI.sharedInstance().flush();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //友盟分类打点
    public static void onEvent(Context context, String eventId) {
        onEvent(context, eventId, new HashMap<String, String>());
    }

    public static void onEvent(Context context, String eventID, String trace) {
        try {
            JSONObject properties = new JSONObject();
            properties.put(eventID, trace);                    // 设置商品 ID
            SensorsDataAPI.sharedInstance().track(eventID, properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void onEvent(Context context, String eventId, String type, String typeName) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(typeName))
            map.put(type, typeName);
        onEvent(context, eventId, map);
    }


    public static void onResume(Context context) {

    }

    public static void onPause(Context context) {

    }

    public static void onPageStart(String s) {

    }

    public static void onPageEnd(String s) {

    }

    public static void reportError(Context context, Throwable throwable) {

    }

    /**
     * 获取友盟打点（广告）
     *
     * @param title
     * @param suffix
     * @return
     */
    public static String getTraceInfo(String title, String suffix) {
        return title + "_" + new java.text.SimpleDateFormat("yyyy/MM/dd").
                format(System.currentTimeMillis()) + "_" + suffix;
    }

    public static String getTraceInfo(String title, String suffix, int position) {
        return getTraceInfo(title, suffix) + "_第" + position + "位";
    }


}
