package com.wallstreetcn.rpc;

import com.kronos.volley.Response;
import com.kronos.volley.VolleyError;
import com.kronos.volley.toolbox.NetResponse;

import java.util.List;

public class ResponseListListener implements Response.Listener<NetResponse>, Response.ErrorListener {
    private List<Response.Listener<NetResponse>> responseList;
    private List<Response.ErrorListener>         errorList;

    public ResponseListListener(List<Response.Listener<NetResponse>> responseList, List<Response.ErrorListener> errorList) {
        this.responseList = responseList;
        this.errorList = errorList;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        for (Response.ErrorListener errorListener : errorList) {
            if (errorListener != null)
                errorListener.onErrorResponse(error);
        }
    }

    @Override
    public void onResponse(NetResponse response) {
        for (Response.Listener<NetResponse> listener : responseList) {
            if (listener != null)
                listener.onResponse(response);
        }
    }
}
