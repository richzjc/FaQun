package com.wallstreetcn.rpc;

import com.kronos.volley.toolbox.BaseApiParser;
import com.kronos.volley.toolbox.StringRequest;

import java.util.Map;

import io.reactivex.Observable;


/**
 * Created by zhangyang on 16/1/20.
 */
public interface BaseApi<T> {
    StringRequest getRequest();

    String getUrl();

    Map<String, String> getHeader();

    int Method();

    BaseApiParser getParser();

    void start();

    void cancel();

    T sync() throws Exception;

    Observable<T> observable();
}
