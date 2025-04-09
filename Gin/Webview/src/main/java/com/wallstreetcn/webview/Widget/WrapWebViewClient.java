package com.wallstreetcn.webview.Widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.wallstreetcn.baseui.manager.AppManager;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.router.ActivityHelper;
import com.wallstreetcn.helper.utils.system.EquipmentUtils;
import com.wallstreetcn.webview.R;
import com.wallstreetcn.webview.javascript.AndroidShareInterface;

/**
 * Created by Leif Zhang on 16/8/9.
 * Email leifzhanggithub@gmail.com
 */
public class WrapWebViewClient extends WSCNWebClient {
    private WebViewClient webViewClient;
    private AndroidShareInterface javaScriptShareObj;

    public WrapWebViewClient(WebView view, WebViewClient webViewClient) {
        this.webViewClient = webViewClient;
        javaScriptShareObj = new AndroidShareInterface(view);
    }

    public void reloadTemp(WebView view) {
//        WebViewCompat.loadJsFunction(view, "window.__YutaAppOnPrepare()");
        WebViewCompat.sendEventToYuTa(view, "reload", true);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if (!TextUtils.equals(scheme, "wscn") && !TextUtils.equals("http", scheme)
                    && !TextUtils.equals("https", scheme)) {
                startOtherApp(view, url);
                return true;
            }
//            if (RouterHelper.openWithReturn(uri.toString(), view.getContext())) {
//                return true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webViewClient.shouldOverrideUrlLoading(view, url);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        try {
            Uri uri = request.getUrl();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                String scheme = uri.getScheme();
                if (!TextUtils.equals(scheme, "wscn") && !TextUtils.equals("http", scheme)
                        && !TextUtils.equals("https", scheme)) {
                    startOtherApp(view, uri.toString());
                    return true;
                }
            }
//            if (RouterHelper.openWithReturn(uri.toString(), view.getContext())) {
//                return true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webViewClient.shouldOverrideUrlLoading(view, request);
    }


    private void startOtherApp(final View view, final String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        final Context context = view.getContext();
        boolean isInstall = EquipmentUtils.isInstallApp(intent, context);
        if (isInstall) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), androidx.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
            builder.setCancelable(false);
            builder.setTitle(ResourceUtils.getResStringFromId(R.string.webview_goto_another_app));
            builder.setPositiveButton(ResourceUtils.getResStringFromId(R.string.webview_confirm_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityHelper.startActivity(intent, (Activity) context);
                    if (!AppManager.getAppManager().isSingleActivity())
                        ((Activity) context).finish();
                }
            });
            builder.setNegativeButton(ResourceUtils.getResStringFromId(R.string.webview_cancel_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        try {
            webViewClient.onPageFinished(view, url);
//            reloadTemp(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        javaScriptShareObj.onWebViewLoadingStart();
        webViewClient.onPageStarted(view, url, favicon);
    }

    public String getShareTitle() {
        return javaScriptShareObj.getShareTitle();
    }

    public String getShareContent() {
        return javaScriptShareObj.getShareContent();
    }

    public String getShareImgUrl() {
        return javaScriptShareObj.getShareImgUrl();
    }

    public boolean isLoadingFinish() {
        return javaScriptShareObj.isLoadingFinish();
    }

    public void loadShareData() {
        javaScriptShareObj.onWebViewLoadingFinish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if(view instanceof WSCNWebView)
            ((WSCNWebView) view).loadErrorFlag = true;
        webViewClient.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if(view instanceof WSCNWebView)
            ((WSCNWebView) view).loadErrorFlag = true;
        webViewClient.onReceivedError(view, errorCode, description, failingUrl);
    }
}
