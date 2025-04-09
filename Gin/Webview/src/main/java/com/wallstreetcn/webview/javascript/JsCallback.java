package com.wallstreetcn.webview.javascript;

import com.wallstreetcn.webview.Widget.WSCNWebView;

import org.json.JSONObject;

/**
 * Created by zhangyang on 16/3/16.
 */
public interface JsCallback {
    void onCallBack(WSCNWebView view, String function, JSONObject args);

    void setCallBackId(String callBackId);

    String getCallBackId();
}
