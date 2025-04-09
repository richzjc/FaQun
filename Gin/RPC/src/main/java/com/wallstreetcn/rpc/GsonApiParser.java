package com.wallstreetcn.rpc;

import com.alibaba.fastjson.JSON;
import com.kronos.volley.ParseError;
import com.kronos.volley.toolbox.BaseApiParser;
import com.wallstreetcn.helper.utils.TLog;

/**
 * Created by zhangyang on 16/1/28.
 */
public class GsonApiParser implements BaseApiParser {
    private Class mClass;

    public GsonApiParser(Class mClass) {
        this.mClass = mClass;
    }

    @Override
    public Object parse(String content) throws ParseError {
        TLog.i("@@@", mClass.getSimpleName() + ": "  + content);
        return JSON.parseObject(content, mClass);
    }
}
