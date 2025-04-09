package com.wallstreetcn.baseui.manager;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * Created by zhangyang on 16/5/16.
 */
public class BundleManager {
    private static BundleManager instance;
    private Bundle mBundle;
    private boolean isLogin;

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    // 采用双重检查加锁实例化单件
    public static BundleManager getInstance() {
        // 第一次检查
        if (instance == null) {
            synchronized (BundleManager.class) {
                // 第二次检查
                if (instance == null) {
                    instance = new BundleManager();
                }
            }
        }
        return instance;
    }

    private BundleManager() {
        mBundle = new Bundle();
    }

    public void putParcelable(String key, Parcelable value) {
        mBundle.putParcelable(key, value);
    }

    public void remove(String key) {
        mBundle.remove(key);
    }

    public <T extends Parcelable> T getParcelable(String key) {
        if (mBundle.containsKey(key))
            return mBundle.getParcelable(key);
        else
            return null;
    }

    public void putString(String key, String value) {
        mBundle.putString(key, value);
    }

    public String getString(String key) {
        return mBundle.getString(key);
    }
}
