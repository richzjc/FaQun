package com.wallstreetcn.webview.Widget;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import com.wallstreetcn.helper.BuildConfig;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.SharedPrefsUtil;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.image.ImageUtlFormatHelper;
import com.wallstreetcn.helper.utils.router.DoubleClickHelper;
import com.wallstreetcn.helper.utils.system.EquipmentUtils;
import com.wallstreetcn.helper.utils.system.ScreenUtils;
import com.wallstreetcn.rpc.VolleyQueue;
import com.wallstreetcn.rpc.host.HostManager;
import com.wallstreetcn.webview.javascript.CustomCallBack;
import com.wallstreetcn.webview.javascript.JsBridge;
import com.wallstreetcn.webview.util.WebViewDownloadUtil;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by micker on 16/7/1.
 */
public class WSCNWebView extends WebView {

    private JsBridge jsBridge;
    public boolean isDestory = false;
    public boolean loadErrorFlag = false;

    public WSCNWebView(Context context) {
        super(context);
        init();
    }

    public WSCNWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WSCNWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        initWebView();
        setBackgroundColor(ResourceUtils.getColor(ResourceUtils.getColorId(getContext(), "day_mode_background_color1_ffffff")));
        configSettings();
        setListener();
    }

    private void initWebView() {
        try {
            String process = Util.getProcessName(getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !TextUtils.isEmpty(process)) {
                setDataDirectorySuffix(process);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DoubleClickHelper.cleanDownTime();
            WebViewDownloadUtil.startDownload(url, contentDisposition, mimetype, getContext());
        });
    }

    private void configSettings() {
        jsBridge = new JsBridge(this);

        WebSettings webSetting = this.getSettings();
        webSetting.setAllowFileAccess(true);//加载缓存 h5 需要
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        // 使用默认的缓存策略，cache没有过期就用cache
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        String ua = webSetting.getUserAgentString();
        SharedPrefsUtil.saveString("WebViewUa", ua);
        String platformName = Util.isPayment() ? "WscnProApp" : "WscnApp";
        ua += String.format(" %s/%s Yuta/%s", platformName, EquipmentUtils.getVersionName(), "0.1.2");
        ua += String.format(" %s/%s", "wscnStatusBarHeight", String.valueOf(ScreenUtils.px2dip(Util.getStatusBarHeight(getContext()))));
        ua += String.format(" packageName/%s", getContext().getPackageName());
        ua += String.format(" channel/%s", EquipmentUtils.getChannel());
        ua += String.format(" environment/%s", HostManager.getEnvironment());
        ua += String.format(" device/%s", getDevice());

        String mode = " wscnTheme/day";
        if (Util.getIsNightMode())
            mode = " wscnTheme/night";
        ua += mode;
        webSetting.setUserAgentString(ua);
        webSetting.setSavePassword(false);
        //   if (EquipmentUtils.getVersionName().contains("-debug")) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }
        //  }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    private String getDevice() {
        return "(" + Build.BRAND + "-" + Build.MODEL + ")";
    }


    private WrapWebViewClient webViewClient;
    private WrapWebChromeClient webChromeClient;

    public Bitmap getIconBitmap() {
        return webChromeClient != null ? webChromeClient.iconBitmap : null;
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        webViewClient = new WrapWebViewClient(this, client);
        super.setWebViewClient(webViewClient);
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        webChromeClient = new WrapWebChromeClient(client);
        super.setWebChromeClient(webChromeClient);
    }

    public void setWebViewFontSize(int fontSize) {
        getSettings().setDefaultFontSize(fontSize);
    }

    public void setFontScale(int fontIndexValue) {
        if(fontIndexValue == 1){
            getSettings().setTextZoom(87);
        } else if (fontIndexValue == 2)
           getSettings().setTextZoom(100);
        else if (fontIndexValue == 3)
            getSettings().setTextZoom(112);
        else if (fontIndexValue == 4)
            getSettings().setTextZoom(125);
        else if(fontIndexValue == 5)
            getSettings().setTextZoom(137);
        else if(fontIndexValue == 6)
            getSettings().setTextZoom(150);
        else
            getSettings().setTextZoom(100);
    }

    public void refreshData() {
        webViewClient.reloadTemp(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        isDestory = true;
        jsBridge.Detach();
    }

    public String getShareTitle() {
        return webViewClient.getShareTitle();
    }

    public String getShareContent() {
        return webViewClient.getShareContent();
    }

    public void loadShareData() {
        webViewClient.loadShareData();
    }

    public String getShareImgUrl() {
        return webViewClient.getShareImgUrl();
    }

    public boolean isLoadingFinish() {
        return webViewClient.isLoadingFinish();
    }

    public void addMethod(CustomCallBack callBack) {
        jsBridge.addMethod(callBack.getCallbackFunctionName(), callBack);
    }

    public void loadJavaScript(String callbackId, String json) {
        jsBridge.loadJavaScript(callbackId, json);
    }

    @Deprecated
    public void loadJavaScriptWithoutDelete(String callbackId, String json) {
        jsBridge.loadJavaScriptWithoutDelete(callbackId, json);
    }

    public void loadHtmlFolder(final String folder, final String fileName, String routerUrl) {
        if (loadIpHtml(fileName, routerUrl)) {
            return;
        }
        loadUrl(addParamsToUrl("file://" + folder + File.separator + fileName, routerUrl));
    }

    public void loadHtml(final String assetFileFolder, final String fileName, String routerUrl) {
        loadErrorFlag = false;
        if (loadIpHtml(fileName, routerUrl)) {
            return;
        }
        loadUrl(addParamsToUrl("file:///android_asset/" + assetFileFolder + "/" + fileName, routerUrl));
    }

    public boolean loadIpHtml(String fileName, String routerUrl) {
        if (BuildConfig.DEBUG) {
            String ip = SharedPrefsUtil.getDevelopHtmlIp();
            if (!TextUtils.isEmpty(ip)) {
                return checkUrlContains(ip, fileName, routerUrl);
            }
        }
        return false;
    }

    int startScrollY = -1;
    boolean direction;
    OnScrollListener listener;
    private double readPercent = 0.01;

    public void setReadPercent(double percent) {
        if (percent > this.readPercent)
            this.readPercent = percent;
    }

    public double getReadPercent() {
        return (readPercent > 1 ? 1 : readPercent) * 100;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (listener != null)
            listener.onScrollNewY(t);

        if (startScrollY < 0) {
            startScrollY = t;
            direction = (t - oldt) > 0;
        }

        if (Math.abs(t - oldt) > 1 && listener != null)
            listener.onScroll(t - oldt);

        boolean flag = (t - oldt) > 0;
        if (flag != direction) {
            direction = flag;
            startScrollY = oldt;
        }
    }

    int originWidth = 0;
    int originHeight = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getMeasuredWidth() > 0)
            originWidth = getMeasuredWidth();

        if(getMeasuredHeight() > 0)
            originHeight = getMeasuredHeight();

        if(getMeasuredHeight() <= 0)
            setMeasuredDimension(getMeasuredWidth(), ScreenUtils.dip2px(0.5f));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_CANCEL) || (event.getAction() == MotionEvent.ACTION_UP)) {
            startScrollY = -1;
        }
        return super.onTouchEvent(event);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public interface OnScrollListener {
        void onScroll(int dy);

        void onScrollNewY(int y);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void loadUrl(@NonNull String url, @NonNull Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadUrl(String url) {
        if (ImageUtlFormatHelper.checkWhiteList(url)) {
            super.loadUrl(url, VolleyQueue.getInstance().getRequestHeader());
        } else {
            super.loadUrl(url);
        }
    }

    @Override
    public void reload() {
        loadErrorFlag = false;
        super.reload();
    }

    private boolean checkUrlContains(String ip, String fileName, String routerUrl) {
        if (true) {
            loadUrl(addParamsToUrl("http://" + ip + "/" + fileName, routerUrl));
            return true;
        }
        if (getContext() instanceof Activity) {
            Intent intent = ((Activity) getContext()).getIntent();
            if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("url"))) {
                String url = intent.getStringExtra("url");
                String host = Uri.parse(url).getHost();
                if (TextUtils.equals(host, ip)) {
                    loadUrl(addParamsToUrl(url, routerUrl));
                    return true;
                }
            }
        }
        return false;
    }

    private String addParamsToUrl(String url, String routerUrl) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }

        final Uri.Builder builder = Uri.parse(url).buildUpon();
        if (!TextUtils.isEmpty(routerUrl)) {
            Uri routerUri = Uri.parse(routerUrl);
            Set<String> queryParameterNames = routerUri.getQueryParameterNames();
            Iterator<String> iterator = queryParameterNames.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.appendQueryParameter(key, routerUri.getQueryParameter(key));
            }
        }
        return builder
                .appendQueryParameter("enabledownloadimage", "true")
                .build().toString();
    }

    @Override
    protected void onDetachedFromWindow() {
        destroy();
        super.onDetachedFromWindow();
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        ActionMode actionMode = super.startActionMode(callback);
        return resolveActionMode(actionMode);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        ActionMode actionMode = super.startActionMode(callback, type);
        return resolveActionMode(actionMode);
    }

    /**
     * 处理item，处理点击
     *
     * @param actionMode
     */
    private ActionMode resolveActionMode(ActionMode actionMode) {
        if (mActionModeCallback != null) {
            mActionModeCallback.onActionMode(actionMode);
        }
        return actionMode;
    }


    public interface ActionModeCallback {
        ActionMode onActionMode(ActionMode actionMode);
    }

    private ActionModeCallback mActionModeCallback;

    public void setActionModeCallback(ActionModeCallback mActionModeCallback) {
        this.mActionModeCallback = mActionModeCallback;
    }

}

