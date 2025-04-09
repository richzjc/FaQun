package com.wallstreetcn.webview.javascript;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.webview.Widget.WSCNWebView;

import org.json.JSONObject;

/**
 * Created by zhangyang on 16/3/22.
 */
public abstract class CustomCallBack implements JsCallback {
    protected String callbackFunctionName;
    private Activity mActivity;
    private String callBackId;
    protected WSCNWebView mWebView;

    public void attachWebView(WSCNWebView webView) {
        this.mWebView = webView;
    }

    public void attach(Activity context) {
        this.mActivity = context;
    }

    public void detach() {
        this.mActivity = null;
    }

    public String getCallbackFunctionName() {
        return callbackFunctionName;
    }

    public AppCompatActivity getActivity() {
        return (AppCompatActivity) mActivity;
    }

    @Override
    public void setCallBackId(String callBackId) {
        this.callBackId = callBackId;
    }

    @Override
    public String getCallBackId() {
        return callBackId;
    }

    public boolean redirectloadDownload() {
        return false;
    }

    public final void loadJs(JSONObject args, WSCNWebView view, Object data) {
        if (view != null && args != null)
            view.loadJavaScript(args.optString("callbackId"), JsBridge.getBridgeJson(true, data));
    }

    protected void notFountFunction(String function, JSONObject args) {
        TLog.e("未找到:" + callbackFunctionName + "." + function + " - " + args.toString());
    }
}
