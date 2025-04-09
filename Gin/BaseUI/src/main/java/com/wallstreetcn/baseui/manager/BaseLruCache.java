package com.wallstreetcn.baseui.manager;

import android.os.Parcelable;
import android.util.LruCache;

/**
 * Created by wscn on 16/12/20.
 */

public class BaseLruCache {

    private static BaseLruCache baseLruCache;
    private LruCache<String, Parcelable> lruCache;

    public static BaseLruCache getInstance(){
        if(baseLruCache == null){
            baseLruCache = new BaseLruCache();
        }
        return baseLruCache;
    }

    private BaseLruCache(){
        lruCache = new LruCache<>(10 * 1024 * 1024);
    }

    public void set(String key, Parcelable parcelable){
        lruCache.put(key, parcelable);
    }

    public Parcelable get(String key){
        return lruCache.get(key);
    }
}
