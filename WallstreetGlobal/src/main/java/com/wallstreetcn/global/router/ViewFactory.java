package com.wallstreetcn.global.router;

import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;

import com.wallstreetcn.baseui.model.BaseCustomModel;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by zhangjianchuan on 2016/9/26.
 */
public class ViewFactory {

    private static HashMap<String, Class<? extends BaseCustomModel>> classMap = new HashMap<>();

    public static void put(String type, Class<? extends BaseCustomModel> viewClass) {
        if (!TextUtils.isEmpty(type) && viewClass != null)
            classMap.put(type, viewClass);
    }

    public static BaseCustomModel getBaseCustomModel(String type) {
        if (classMap.containsKey(type)) {
            Class newClass = classMap.get(type);
            try {
                Constructor constructor = newClass.getConstructor(LifecycleOwner.class);
                Object[] array = new Object[1];
                array[0] = null;
                return (BaseCustomModel) constructor.newInstance(array);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
