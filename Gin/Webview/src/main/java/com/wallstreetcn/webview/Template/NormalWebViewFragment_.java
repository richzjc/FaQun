package com.wallstreetcn.webview.Template;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.richzjc.netannotation.NetAvailable;
import com.richzjc.netannotation.NetLose;
import com.richzjc.network.NetManager;
import com.wallstreetcn.baseui.base.BaseFragment;
import com.wallstreetcn.baseui.widget.pulltorefresh.IRefreshListener;
import com.wallstreetcn.baseui.widget.pulltorefresh.PullToRefreshAdapterView;
import com.wallstreetcn.rpc.ResponseListener;
import com.wallstreetcn.webview.R;
import com.wallstreetcn.webview.Widget.NestedWebView;
import com.wallstreetcn.webview.Widget.WSCNWebChromeClient;
import com.wallstreetcn.webview.Widget.WSCNWebClient;
import com.wallstreetcn.webview.Widget.WSCNWebView;
import com.wallstreetcn.webview.Widget.WebViewCompat;
import com.wallstreetcn.webview.api.MiniProgramApi;
import com.wallstreetcn.webview.databinding.WscnFragmentWebviewNormalBinding;
import com.wallstreetcn.webview.entity.MiniProgramEntity;
import com.wallstreetcn.webview.javascript.CustomCallBack;

import java.util.List;

public class NormalWebViewFragment_ extends BaseFragment {

    public WSCNWebView webView;
    public WebLoadCallback loadCallback;
    protected String url = "";
    protected WebChromeClient.CustomViewCallback mCustomCallback;

    private WscnFragmentWebviewNormalBinding binding;

    @Override
    public View doGetContentView() {
        binding = WscnFragmentWebviewNormalBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void destroyViewBinding() {
        binding = null;
    }


    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        webView = mViewQuery.findViewById(R.id.webView);
        setListener();
    }


    private void setListener() {
        webView.setWebChromeClient(new WSCNWebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 60) {
                    dismissDialog();
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (loadCallback != null)
                    loadCallback.onLoadTitle(title);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                mCustomCallback = callback;
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
            }
        });

        webView.setWebViewClient(client);
    }

    WSCNWebClient client = new WSCNWebClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
            viewManager.showContentView();
            if (loadCallback != null)
                loadCallback.onLoadFinish();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            viewManager.showLoadErrorView();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            viewManager.showLoadErrorView();
        }
    };

    @Override
    public void doInitData() {
        super.doInitData();
        Bundle bundle = getArguments();
        if (null != bundle) {
            url = bundle.getString("url", "");
        }
        if (!TextUtils.isEmpty(url)) {
            showDialog();
            if (url.contains("file:///android_assets")) {
                String[] array = url.split("/");
                if (array.length > 2) {
                    int length = array.length;
                    webView.loadHtml(array[length - 2], array[length - 1], "");
                } else {
                    webView.loadUrl(url);
                }
            } else {
                webView.loadUrl(url);
            }
        }
    }


    @Override
    public boolean onBackPressed() {
      if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onBackPressed();
    }



    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
