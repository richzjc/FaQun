package com.wallstreetcn.global.router;

import android.net.Uri;
import android.text.TextUtils;
import com.wallstreetcn.helper.utils.data.CrashReport;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import androidx.fragment.app.Fragment;

/**
 * Created by zhangjianchuan on 2016/9/26.
 */
public class ClassFactory {

    private static HashMap<String, Class> classMap = new HashMap<String, Class>();

    public static void put(String type, Class fragmentClass) {
        if (!TextUtils.isEmpty(type) && fragmentClass != null)
            classMap.put(type, fragmentClass);
    }

    public static HashMap<String, Class> getClassMap() {
        return classMap;
    }


    public static boolean checkAccept(String type) {
        try {
            HashMap<String, Class> map = ClassFactory.getClassMap();
            Uri uri = Uri.parse(type);
            String host = uri.getHost();
            String router = String.format("%s%s", uri.getHost(), uri.getEncodedPath());
            return map.containsKey(router);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Fragment getFragment(String type) {
        if (classMap.containsKey(type)) {
            Class fragment = classMap.get(type);
            try {
                return (Fragment) fragment.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                Throwable throwable = new Throwable("ClassFactory.getFragment 返回为空， type = " + type + "; errorMsgs = " + e.getMessage());
                CrashReport.postCatchedException(throwable);
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Throwable throwable = new Throwable("ClassFactory.getFragment 返回为空， type = " + type + "; errorMsgs = " + e.getMessage());
                CrashReport.postCatchedException(throwable);
                return null;
            }
        } else {
            return null;
        }
    }

    public static Constructor<?> getClassConstructor(String type, Class<?>... args) {
        if (classMap.containsKey(type)) {
            Class newClass = classMap.get(type);
            try {
                return newClass.getConstructor(args);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
