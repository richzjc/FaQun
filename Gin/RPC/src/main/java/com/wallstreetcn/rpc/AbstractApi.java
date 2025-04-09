package com.wallstreetcn.rpc;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;

import com.kronos.rx2adapter.RxVolleyAdapter;
import com.kronos.volley.Request;
import com.kronos.volley.toolbox.BaseApiParser;
import com.kronos.volley.toolbox.NetResponse;
import com.kronos.volley.toolbox.RequestFuture;
import com.kronos.volley.toolbox.StringRequest;
import com.richzjc.anotation_api.IGetBundle;
import com.richzjc.anotation_api.manager.ParameterManager;
import com.wallstreetcn.rpc.exception.ErrorUtils;
import com.wallstreetcn.rpc.host.UrlPacker;

import java.util.Map;

import io.reactivex.Observable;


/**
 * Created by Leif Zhang on 2017/5/16.
 * Email leifzhanggithub@gmail.com
 */

public abstract class AbstractApi<T> implements BaseApi<T>, IGetBundle {

    protected ResponseListener<T> responseListener;
    protected Bundle bundle;
    protected String realUrl;
    private final String TAG = getClass().getName();
    private StringRequest request;
    protected boolean isNeedToast = true;
    protected String cacheExtra;
    protected long cacheTime = 0;
    protected long responseTime = 0;
    protected boolean isNeedRefresh = false;
    protected boolean ignoreExpired = false;


    /**
     * 添加 监听变化代表 会取消请求回调
     *
     * @param lifecycle
     */
    public AbstractApi<T> addLifecycle(Lifecycle lifecycle) {
        if (lifecycle != null)
            lifecycle.addObserver((LifecycleEventObserver) (source, event) -> {
                if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                    if (request != null)
                        request.cancel();
                    AbstractApi.this.responseListener = null;
                }
            });
        return this;
    }

    public void setIgnoreExpired() {
        this.ignoreExpired = true;
    }

    public AbstractApi() {
        this(null, null);
    }

    public AbstractApi(Bundle bundle) {
        this(null, bundle);
    }

    public AbstractApi(ResponseListener<T> responseListener) {
        this(responseListener, null);
    }

    public AbstractApi(ResponseListener<T> responseListener, Bundle bundle) {
        this.responseListener = responseListener;
        if (bundle == null)
            bundle = new Bundle();
        this.bundle = bundle;
        ParameterManager.getInstance().loadParameter(this);
    }

    @Override
    public Bundle getBundle() {
        return bundle;
    }

    public void setResponseListener(ResponseListener<T> responseListener) {
        this.responseListener = responseListener;
    }

    public ResponseListener<T> getResponseListener() {
        return responseListener;
    }

    public void setCacheExtra(String cacheExtra) {
        this.cacheExtra = cacheExtra;
    }

    public void setNeedToast(boolean needToast) {
        isNeedToast = needToast;
    }

    public boolean isNeedToast() {
        return isNeedToast;
    }

    public final void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public final void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public final void setIsNeedRefresh(boolean isNeedRefresh) {
        this.isNeedRefresh = isNeedRefresh;
    }

    protected String getRealUrl() {
        if (TextUtils.isEmpty(realUrl)) {
            realUrl = getUrl();
            try {
                Uri uri = Uri.parse(realUrl);
                if (TextUtils.isEmpty(uri.getScheme())) {
                    realUrl = UrlPacker.pack(realUrl, Method() == Request.Method.GET ? getRequestBody() : null);
                } else {
                    realUrl = UrlPacker.packNoHost(realUrl, Method() == Request.Method.GET ? getRequestBody() : null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return realUrl;
    }

    public Map<String, String> getRequestBody() {
        return null;
    }

    @Override
    public Map<String, String> getHeader() {
        return null;
    }


    @Override
    public BaseApiParser getParser() {
        return new StringApiParser();
    }

    @Override
    public void start() {
        VolleyQueue.getInstance().addRequest(getRealRequest());
    }

    @Override
    public void cancel() {
        VolleyQueue.getInstance().removeRequest(getRealUrl());
    }

    public StringRequest getRealRequest() {
        if (request == null)
            request = getRequest();
        return request;
    }

    @Override
    public T sync() throws Exception {
        RequestFuture<NetResponse> requestFuture = RequestFuture.newFuture();
        StringRequest request = getRealRequest();
        request.setRequestListener(requestFuture).setErrorListener(requestFuture);
        requestFuture.setRequest(request);
        VolleyQueue.getInstance().addRequest(request);
        return requestFuture.get().getData();
    }

    @Override
    public Observable<T> observable() {
        Request request = getRealRequest();
        Observable<T> observable = RxVolleyAdapter.INSTANCE.getObservable((StringRequest) request)
                .map(netResponse -> netResponse).map(NetResponse::getData);
        VolleyQueue.getInstance().addRequest(request);
        return observable;
    }

    public void onError(int statusCode, String errorMessage) {
        if (responseListener != null) {
            responseListener.onErrorResponse(statusCode, errorMessage);
        }
        ErrorUtils.responseFailed(errorMessage, isNeedToast, statusCode, this);
    }
}
