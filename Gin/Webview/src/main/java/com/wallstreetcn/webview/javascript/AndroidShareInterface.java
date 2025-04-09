package com.wallstreetcn.webview.javascript;

import android.content.Intent;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.UtilsContextManager;
import com.wallstreetcn.helper.utils.snack.MToastHelper;
import com.wallstreetcn.webview.FaqunConstKt;
import com.wallstreetcn.webview.R;
import com.wallstreetcn.webview.model.JavascriptFunction;


/**
 * Created by zhangyang on 16/6/27.
 */
public class AndroidShareInterface implements JavascriptFunction {
    private String share_title, share_content, share_imgUrl;
    private boolean isLoadingFinish = false;
    private WebView mWebView;


    public AndroidShareInterface(WebView webView) {
        this.mWebView = webView;
        mWebView.addJavascriptInterface(this, "faqun");
    }



    public void onWebViewLoadingStart() {
        isLoadingFinish = false;
    }

    public void onWebViewLoadingFinish() {
        mWebView.loadUrl("javascript:window.share_obj.getTitle(document.querySelector" +
                "(\"meta[name='wscn-share-title']\").content);");

        mWebView.loadUrl("javascript:window.share_obj.getContent(document.querySelector" +
                "(\"meta[name='wscn-share-content']\").content);");

        mWebView.loadUrl("javascript:window.share_obj.getImgUrl(document.querySelector" +
                "(\"link[rel='wscn-share-image']\").href);");

        mWebView.loadUrl("javascript:window.share_obj.getImgUrl(document.querySelector" +
                "(\"meta[name='wscn-share-image']\").content);");
    }


    @JavascriptInterface
    public void responseFaQun(String text){
        FaqunConstKt.setFaqunText(text);
        if(TextUtils.isEmpty(FaqunConstKt.getFaqunText()))
            MToastHelper.showToast("输入的文本为空");
        else{
            try {
                Intent intent = new Intent();
                intent.setClass(UtilsContextManager.getInstance().getApplication(), Class.forName("com.faqun.SecondActivity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                UtilsContextManager.getInstance().getApplication().startActivity(intent);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @JavascriptInterface
    public AndroidShareInterface getTitle(String title) {
        share_title = TextUtils.isEmpty(title) ? ResourceUtils.getResStringFromId(R.string.webview_wall_sits_app) : title;
        return this;
    }

    @JavascriptInterface
    public AndroidShareInterface getContent(String content) {
        isLoadingFinish = true;
        share_content = TextUtils.isEmpty(content) ? ResourceUtils.getResStringFromId(R.string.webview_invite_discuss_wits) : content;
        return this;
    }

    @JavascriptInterface
    public AndroidShareInterface getImgUrl(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl))
            share_imgUrl = imgUrl;
        return this;
    }

    public String getShareTitle() {
        return share_title;
    }

    public String getShareContent() {
        return share_content;
    }

    public String getShareImgUrl() {
        return share_imgUrl;
    }

    public boolean isLoadingFinish() {
        return isLoadingFinish;
    }
}
