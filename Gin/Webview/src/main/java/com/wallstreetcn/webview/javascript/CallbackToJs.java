package com.wallstreetcn.webview.javascript;

/**
 * Created by zhangyang on 16/3/23.
 */
public class CallbackToJs {
    public static String getJavaScript(String callbackid, String args) {
        return String.format("javascript:window.__YutaAppCallback(%s,%s)", callbackid, args);
    }

    public static String getJavaScriptWithoutDelete(String callbackid, String args) {
        return String.format("javascript:window.__YutaAppCallbackWithoutDelete(%s,%s)", callbackid, args);
    }

}
