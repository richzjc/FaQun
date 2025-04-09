package com.wallstreetcn.webview.javascript;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyang on 16/3/17.
 */
public class JsMethodManager {
    private final static JsMethodManager ourInstance = new JsMethodManager();

    public static JsMethodManager getInstance() {
        return ourInstance;
    }

    private HashMap<String, CustomCallBack> methods;

    private JsMethodManager() {
        methods = new HashMap<>();
        args = new Bundle();
    }

    public HashMap<String, CustomCallBack> getMethods() {
        HashMap<String, CustomCallBack> copyHasMap = new HashMap<>();
        for (Map.Entry<String, CustomCallBack> entry : methods.entrySet()) {
            Class t = entry.getValue().getClass();
            try {
                CustomCallBack copyClass = (CustomCallBack) t.newInstance();
                copyHasMap.put(entry.getKey(), copyClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return copyHasMap;
    }

    public void setMethod(String name, CustomCallBack callBack) {
        methods.put(name, callBack);
    }

    private Bundle args;

    public void saveBundle(String key, Bundle bundle) {
        args.putBundle(key, bundle);
    }

    public Bundle getBundle(String key) {
        return args.getBundle(key);
    }

    public void remove(String key) {
        args.remove(key);
    }
}
