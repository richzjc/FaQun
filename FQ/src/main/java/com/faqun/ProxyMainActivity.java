package com.faqun;

import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebViewClient;

import com.faqun.R;
import com.wallstreetcn.baseui.base.BaseActivity;
import com.wallstreetcn.baseui.manager.AppManager;
import com.wallstreetcn.helper.utils.router.DoubleClickHelper;
import com.wallstreetcn.helper.utils.snack.MToastHelper;
import com.wallstreetcn.webview.Widget.WSCNWebView;

public class ProxyMainActivity extends BaseActivity {

    @Override
    public View doGetContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_proxy_main, null);
        WSCNWebView webView = (WSCNWebView) view.findViewById(R.id.webview);
        WebViewClient client = new WebViewClient();
        webView.setWebViewClient(client);
        webView.loadUrl("https://www.shfangzhu.com/static/index.html");
        return view;
    }


    @Override
    public void doInitSubViews(View view) {
        setStatusBarTranslucentCompat();
        super.doInitSubViews(view);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if(!DoubleClickHelper.checkExitDoubleClick()){
            AppManager.getAppManager().AppExit();
        }else{
            MToastHelper.showToast("再按一次退出程序");
        }
    }
}
