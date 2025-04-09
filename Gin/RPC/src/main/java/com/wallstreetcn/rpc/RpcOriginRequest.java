package com.wallstreetcn.rpc;

import com.kronos.volley.AuthFailureError;
import com.kronos.volley.NetworkResponse;
import com.kronos.volley.ParseError;
import com.kronos.volley.Request;
import com.kronos.volley.Response;
import com.kronos.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * Created by zhangyang on 16/6/21.
 */
public class RpcOriginRequest extends Request<String> implements IGetResponseTime{
    private long responseTime = 0L;
    public Response.Listener<String> requestListener;

    private JSONObject jsonObject;

    public RpcOriginRequest(String url) {
        super(url);
    }

    public void setRequestBody(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (jsonObject != null)
            return jsonObject.toString().getBytes();
        return super.getBody();
    }

    public void setResponseTime(long responseTime){
        this.responseTime = responseTime;
    }

    @Override
    public String getCacheKey() {
        String json = "";
        if (jsonObject != null) {
            json = jsonObject.toString();
        }
        return super.getCacheKey() + json;
    }

    public void setRequestListener(Response.Listener<String> requestListener) {
        this.requestListener = requestListener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) throws ParseError {
        String parsed = "";
        parsed = new String(response.data, StandardCharsets.UTF_8);
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        requestListener.onResponse(response);
    }

    @Override
    public long getResponseTime() {
        return responseTime;
    }
}
