package com.wallstreetcn.rpc;

import android.os.Bundle;
import android.text.TextUtils;
import com.wallstreetcn.helper.utils.TLog;

import com.kronos.volley.Request;
import com.kronos.volley.Response;
import com.kronos.volley.VolleyError;
import com.kronos.volley.toolbox.FileUploadRequest;
import com.kronos.volley.toolbox.NetResponse;
import com.kronos.volley.toolbox.StringRequest;

import androidx.annotation.Nullable;

/**
 * Created by Leif Zhang on 16/8/4.
 * Email leifzhanggithub@gmail.com
 */
public abstract class CustomFileApi<T> extends AbstractApi<T> {
    public CustomFileApi() {
        super();
    }

    public CustomFileApi(Bundle bundle) {
        super(bundle);
    }

    public CustomFileApi(ResponseListener<T> responseListener) {
        super(responseListener);
    }

    public CustomFileApi(ResponseListener<T> responseListener, Bundle bundle) {
        super(responseListener, bundle);
    }

    @Override
    public StringRequest getRequest() {
        String url = getRealUrl();
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        FileUploadRequest request = new FileUploadRequest(url);
        request.setRequestListener(new Response.Listener<NetResponse>() {
            @Override
            public void onResponse(NetResponse response) {
                try {
                    if(responseListener != null)
                    responseListener.onSuccess(response.getData(), response.isCache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    onError(error.networkResponse.statusCode, error.networkResponse.errorResponseString);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (responseListener != null) {
                        responseListener.onErrorResponse(600, e.getMessage());
                    }
                }
            }
        }).setMethod(Method()).setHeader(getHeader()).setApiParser(getParser())
                .setCacheTime(cacheTime).setIsRefreshNeed(isNeedRefresh);
        request.setFilePath(getFileName());

        request.setMediaType(getMediaType());
        return request;
    }

    @Nullable
    public abstract String getMediaType();

    public abstract String getFileName();

    @Override
    public int Method() {
        return Request.Method.POST;
    }

}
