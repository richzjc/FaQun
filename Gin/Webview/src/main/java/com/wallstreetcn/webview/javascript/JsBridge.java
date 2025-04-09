package com.wallstreetcn.webview.javascript;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.wallstreetcn.helper.utils.router.RouterHelper;
import com.wallstreetcn.webview.Widget.WSCNWebView;
import com.wallstreetcn.webview.Widget.WebViewCompat;
import com.wallstreetcn.webview.model.JavascriptFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyang on 16/3/15.
 */
public class JsBridge {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case INVOKE:
                    try {
                        handlerCallback(msg.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
            }
            return false;
        }
    });

    private static final String BridgeName = "__YutaJsBridge";
    private HashMap<String, CustomCallBack> callbacks;
    private CustomCallBack laterAddCallback;
    private static final int INVOKE = 0;
    private WSCNWebView webView;

    public JsBridge(WSCNWebView webView) {
        this.webView = webView;
        callbacks = new HashMap<>(JsMethodManager.getInstance().getMethods());
        for (Map.Entry<String, CustomCallBack> entry : callbacks.entrySet()) {
            entry.getValue().attach(RouterHelper.getActivity(webView.getContext()));
            entry.getValue().attachWebView(webView);
        }
        webView.addJavascriptInterface(new Js(), BridgeName);
    }

    public void attach(Context context) {
        for (Map.Entry<String, CustomCallBack> entry : callbacks.entrySet()) {
            entry.getValue().attach((Activity) context);
        }
    }

    public void loadJavaScript(String callbackId, String json) {
        WebViewCompat.loadJsFunction(webView, CallbackToJs.getJavaScript(callbackId, json));
    }

    public void loadJavaScriptWithoutDelete(String callbackId, String json) {
        WebViewCompat.loadJsFunction(webView, CallbackToJs.getJavaScriptWithoutDelete(callbackId, json));
    }

    public void loadError(String functionName, String json) {
        webView.loadUrl(CallbackToJs.getJavaScript(callbacks.get(functionName).getCallBackId(), json));
    }

    private class Js implements JavascriptFunction {
        @JavascriptInterface
        public void invoke(String name, String args) throws Exception {
            Message msg = Message.obtain();
            msg.what = INVOKE;
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("args", args);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }


    private void handlerCallback(Bundle bundle) throws Exception {
        String method = bundle.getString("name");
        String[] nameSpaces = method.split("\\.");
        if (nameSpaces.length < 3 && !TextUtils.equals("Yuta", nameSpaces[0]))
            return;
        String args = bundle.getString("args");
        String methodName = nameSpaces[1];
        if (callbacks.containsKey(methodName)) {
            CustomCallBack callback = callbacks.get(methodName);
            JSONObject argsObject = new JSONObject(args);
            callback.setCallBackId(argsObject.optString("callbackId"));

            if (TextUtils.equals(methodName, "Load")
                    && TextUtils.equals(nameSpaces[2], "loadDownload")
                    && laterAddCallback != null
                    && laterAddCallback.redirectloadDownload()) {
                laterAddCallback.onCallBack(webView, nameSpaces[2], argsObject);
            } else {
                callback.onCallBack(webView, nameSpaces[2], argsObject);
            }
        }
    }

    public void Detach() {
        if (webView == null)
            return;
        callbacks = new HashMap<>(JsMethodManager.getInstance().getMethods());
        for (Map.Entry<String, CustomCallBack> entry : callbacks.entrySet()) {
            entry.getValue().detach();
        }
        callbacks.clear();
        callbacks = null;
        laterAddCallback = null;
        webView = null;
    }

    public void addMethod(String methodName, CustomCallBack callBack) {
        callbacks.put(methodName, callBack);
        laterAddCallback = callBack;
        callBack.attach(RouterHelper.getActivity(webView.getContext()));
    }

    public static String getBridgeJson(boolean success, Object data) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (success) {
                jsonObject.put("message", "success");
            } else {
                jsonObject.put("message", "fail");
            }
            JSONObject resultObject = new JSONObject();
            if (data != null)
                resultObject.put("data", data);
            jsonObject.put("results", resultObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
