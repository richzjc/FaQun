package com.wallstreetcn.webview;

import android.app.Activity;
import android.content.MutableContextWrapper;

import com.wallstreetcn.webview.Widget.WSCNWebView;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class WebPools {

    private final Queue<WSCNWebView> mWebViews;

    private Object lock = new Object();
    private static WebPools mWebPools = null;

    private static final AtomicReference<WebPools> mAtomicReference = new AtomicReference<>();
    private static final String TAG = WebPools.class.getSimpleName();

    private WebPools() {
        mWebViews = new LinkedBlockingQueue<>();
    }


    public static WebPools getInstance() {
        for (; ; ) {
            if (mWebPools != null)
                return mWebPools;
            if (mAtomicReference.compareAndSet(null, new WebPools()))
                return mWebPools = mAtomicReference.get();

        }
    }


    public void recycle(WSCNWebView webView) {
        recycleInternal(webView);
    }


    public WSCNWebView acquireWebView(Activity activity) {
        return acquireWebViewInternal(activity);
    }

    private WSCNWebView acquireWebViewInternal(Activity activity) {

        WSCNWebView mWebView = mWebViews.poll();

        if (mWebView == null) {
            synchronized (lock) {
                return new WSCNWebView(new MutableContextWrapper(activity));
            }
        } else {
            MutableContextWrapper mMutableContextWrapper = (MutableContextWrapper) mWebView.getContext();
            mMutableContextWrapper.setBaseContext(activity);
            return mWebView;
        }
    }


    private void recycleInternal(WSCNWebView webView) {
        try {
            if (webView.getContext() instanceof MutableContextWrapper) {

                MutableContextWrapper mContext = (MutableContextWrapper) webView.getContext();
                mContext.setBaseContext(mContext.getApplicationContext());
                mWebViews.offer(webView);
            }
            if (webView.getContext() instanceof Activity) {
//            throw new RuntimeException("leaked");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}