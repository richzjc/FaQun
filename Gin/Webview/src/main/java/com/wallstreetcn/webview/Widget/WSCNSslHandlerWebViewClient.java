package com.wallstreetcn.webview.Widget;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wallstreetcn.helper.utils.data.CrashReport;

public class WSCNSslHandlerWebViewClient extends WebViewClient {

    @Override
    public final void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        StringBuilder builder = new StringBuilder();
        builder.append("ssl证书验证失败");
        if (error != null)
            builder.append(" url = " + error.getUrl());

        Throwable throwable = new Throwable(builder.toString());
        CrashReport.postCatchedException(throwable);
    }
}
