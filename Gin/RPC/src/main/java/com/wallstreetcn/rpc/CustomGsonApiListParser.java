package com.wallstreetcn.rpc;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.kronos.volley.ParseError;
import com.kronos.volley.toolbox.BaseApiParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangyang on 16/4/21.
 */
public class CustomGsonApiListParser implements BaseApiParser {
    private Class mClass;
    private String jsonKey;

    public CustomGsonApiListParser(Class mClass) {
        this.mClass = mClass;
    }

    public CustomGsonApiListParser(Class mClass, String jsonKey) {
        this.mClass = mClass;
        this.jsonKey = jsonKey;
    }

    @Override
    public Object parse(String content) throws ParseError {
        if (TextUtils.isEmpty(jsonKey))
            return JSON.parseArray(content, mClass);
        else {
            try {
                JSONObject jobj = new JSONObject(content);
                String realContent = jobj.optString(jsonKey, "");
                return JSON.parseArray(realContent, mClass);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
