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
import com.wallstreetcn.webview.Widget.WebViewCompat;
import com.wallstreetcn.webview.api.MiniProgramApi;
import com.wallstreetcn.webview.databinding.WscnFragmentWebviewBinding;
import com.wallstreetcn.webview.entity.MiniProgramEntity;
import com.wallstreetcn.webview.javascript.CustomCallBack;

import java.util.List;

public class WSCNWebViewFragment_ extends BaseFragment implements IRefreshListener {

    public NestedWebView webView;
    public WebLoadCallback loadCallback;
    protected PullToRefreshAdapterView mPullToRefreshLayout;
    protected String url = "";
    protected ViewGroup mainLayout;
    protected View mCustomView;
    protected WebChromeClient.CustomViewCallback mCustomCallback;
    protected LinearLayout netParent;
    private CustomCallBack callBack;

    private boolean isLoadFinish;
    private boolean isShow;

    public void setmCustomCallback(CustomCallBack callback) {
        this.callBack = callback;
        if (callback != null && webView != null)
            webView.addMethod(callBack);
    }

    @NetAvailable
    private void responseNetAvailable() {
        if (mPullToRefreshLayout.getVisibility() != View.VISIBLE) {
            mPullToRefreshLayout.setVisibility(View.VISIBLE);
            netParent.setVisibility(View.GONE);
            onRefresh();
        }
    }

    @NetLose
    private void responseNetLose() {
        if (mPullToRefreshLayout.getVisibility() != View.VISIBLE) {
            mPullToRefreshLayout.setVisibility(View.GONE);
            netParent.setVisibility(View.VISIBLE);
        }
    }

    private WscnFragmentWebviewBinding binding;

    @Override
    public View doGetContentView() {
        binding = WscnFragmentWebviewBinding.inflate(getLayoutInflater());
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
        if (webView != null && callBack != null)
            webView.addMethod(callBack);
        mPullToRefreshLayout = mViewQuery.findViewById(R.id.mPullToRefreshLayout);
        mPullToRefreshLayout.setCanRefresh(false);
        mainLayout = getActivity().findViewById(android.R.id.content);
        netParent = view.findViewById(R.id.net_parent);
        setListener();
        NetManager.bind(this);
        getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
            public void onResume() {
                checkMiniPrograme();
            }
        });
    }


    private void setListener() {
        mPullToRefreshLayout.setRefreshListener(this);
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
                mainLayout.addView(view);
                mCustomView = view;
//                fullscreen();
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                if (mCustomView != null) {
                    mainLayout.removeView(mCustomView);
                    mCustomView = null;
//                    exitFullscreen();
                }
            }
        });

        webView.setWebViewClient(client);
    }

    WSCNWebClient client = new WSCNWebClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(!webView.loadErrorFlag) {
                isLoadFinish = true;
                webView.setVisibility(View.VISIBLE);
                viewManager.showContentView();
                mPullToRefreshLayout.onRefreshComplete();
                if (loadCallback != null)
                    loadCallback.onLoadFinish();

                checkMiniPrograme();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request != null && request.isForMainFrame())
                viewManager.showLoadErrorView();
            mPullToRefreshLayout.onRefreshComplete();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mPullToRefreshLayout.onRefreshComplete();
        }
    };

    protected void checkMiniPrograme() {
        if (isLoadFinish && !isShow && getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            isShow = true;
            MiniProgramApi api = new MiniProgramApi(new ResponseListener<List<MiniProgramEntity>>() {
                @Override
                public void onSuccess(List<MiniProgramEntity> model, boolean isCache) {
                    if (model != null && model.size() > 0 && getArguments() != null) {
                        String url = getArguments().getString("url");
                        String host = Uri.parse(url).getHost();

                        for (MiniProgramEntity miniProgramEntity : model) {
                            if (!TextUtils.isEmpty(host) && host.contains(miniProgramEntity.regex)) {
                                WebViewCompat.loadJsFunction(webView, miniProgramEntity.js);
                            }
                        }
                    }
                }

                @Override
                public void onErrorResponse(int code, String error) {

                }
            });
            api.start();
        }
    }

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

    private void fullscreen() {
        if (getActivity().getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void exitFullscreen() {
        if (getActivity().getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mCustomView != null && mCustomCallback != null) {
            mCustomCallback.onCustomViewHidden();
        } else if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            webView.getClass().getMethod("onResume").invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        webView.reload();
    }

}
