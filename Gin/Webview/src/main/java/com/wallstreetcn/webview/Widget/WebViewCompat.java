package com.wallstreetcn.webview.Widget;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebView;

import com.wallstreetcn.webview.javascript.CallbackToJs;
import com.wallstreetcn.webview.javascript.JsBridge;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Leif Zhang on 16/8/30.
 * Email leifzhanggithub@gmail.com
 */
public class WebViewCompat {
    public static void loadJsFunction(WebView webView, String javascript) {
        if (webView == null)
            return;
       try {
           if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
               webView.evaluateJavascript(javascript, null);
           } else {
               webView.loadUrl(String.format("javascript:%s", javascript));
           }
       }catch (Exception e) {
           e.printStackTrace();
       }
    }

    @Deprecated()
    public static void changeFont(WebView webView) {
//        JSONObject json = new JSONObject();
//        try {
//            json.put("data", Util.getFontIndexValue());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        loadJsFunction(webView, String.format("window.changeFont(%s)", json.toString()));
    }

    public static void judgeSubscription(WebView webView, boolean followed, String callbackId, String themeId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isFollowed", followed?"follow" :"unfollow");
            jsonObject.put("themeId", themeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(callbackId)) {
            sendEventToYuTa(webView, "themeFollow", jsonObject);
        } else {
            WebViewCompat.loadJsFunction(webView, CallbackToJs.getJavaScript(callbackId, JsBridge.getBridgeJson(true, jsonObject)));
        }
//        JSONObject subscribeJson = new JSONObject();
//        try {
//            subscribeJson.put("isFollowed", followed);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        loadJsFunction(webView, String.format("window.judgeSubscription(%s)", subscribeJson.toString()));
    }

    public static void sendEventToYuTa(WebView webView, String functionName, Object params) {
        JSONObject json = new JSONObject();
        try {
            json.put("data", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadJsFunction(webView, String.format("window.__sendEventToYuTa(\"%s\", %s);", functionName, json.toString()));
    }

}
