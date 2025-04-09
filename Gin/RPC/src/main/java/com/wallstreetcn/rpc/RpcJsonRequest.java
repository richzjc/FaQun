package com.wallstreetcn.rpc;

import com.kronos.volley.NetworkResponse;
import com.kronos.volley.ParseError;
import com.kronos.volley.Request;
import com.kronos.volley.Response;
import com.kronos.volley.VolleyError;
import com.kronos.volley.toolbox.HttpHeaderParser;
import com.kronos.volley.toolbox.JsonRequest;
import com.kronos.volley.toolbox.NetResponse;
import com.kronos.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by Leif Zhang on 2016/12/12.
 * Email leifzhanggithub@gmail.com
 */

public class RpcJsonRequest extends JsonRequest implements IGetResponseTime{
    public Response.Listener<NetResponse> requestListener;
    private long responseTime = 0L;

    public RpcJsonRequest(String url) {
        super(url);
    }

    @Override
    public Request setRequestListener(Response.Listener<NetResponse> mListener) {
        this.requestListener = mListener;
        return super.setRequestListener(mListener);
    }

    @Override
    public String getCacheKey() {
        if (requestBody != null) {
            return String.format("%s_%s", getUrl(), requestBody.toString());
        } else {
            return getUrl();
        }
    }

    private Map<String, String> requestBody;

    public void setResponseTime(long responseTime){
        this.responseTime = responseTime;
    }

    @Override
    public StringRequest setRequestBody(Map<String, String> requestBody) {
        this.requestBody = requestBody;
        return super.setRequestBody(requestBody);
    }

    @Override
    protected Response<NetResponse> parseNetworkResponse(NetworkResponse response) throws ParseError {
        NetResponse netResponse = null;
        try {
            String parsed = new String(response.data, "UTF-8");
            JSONObject jsonObject = new JSONObject(parsed);
            if (jsonObject.has("code")) {
                int code = jsonObject.optInt("code");
                if (code != 20000) {
                    throw new ParseError(new NetworkResponse(code,
                            response.data, response.headers, response.notModified));
                } else {
                    parsed = jsonObject.optString("data");
                }
            }
            try {
                Object o = getApiParser().parse(parsed);
                netResponse = new NetResponse(response.isCache, o);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ParseError(response);
            }
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }
        return Response.success(netResponse, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public long getResponseTime() {
        return responseTime;
    }
}
