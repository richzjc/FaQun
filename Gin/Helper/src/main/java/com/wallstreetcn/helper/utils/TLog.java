package com.wallstreetcn.helper.utils;

import android.util.Log;

import com.wallstreetcn.helper.BuildConfig;

/**
 * 作者： 巴掌 on 15/7/6 13:16
 * 邮箱： wangyuwei@wallstreetcn.com
 */
public class TLog {

    public static final String LOG_TAG = "@WSCN";
    public static boolean DEBUG = BuildConfig.DEBUG;

    private TLog() {

    }

    public static final void v(String log) {
        if (DEBUG)
            Log.v(LOG_TAG, log);
    }

    public static final void d(String log) {
        if (DEBUG)
            Log.d(LOG_TAG, log);
    }

    public static final void i(String log) {
        if (DEBUG)
            Log.i(LOG_TAG, log);
    }

    public static final void w(String log) {
        if (DEBUG)
            Log.w(LOG_TAG, log);
    }

    public static final void e(String log) {
        if (DEBUG)
            Log.e(LOG_TAG, "" + log);
    }


    public static final void v(String tag, String log) {
        if (DEBUG)
            Log.v(tag, log);
    }

    public static final void d(String tag, String log) {
        if (DEBUG)
            Log.d(tag, log);
    }

    public static final void i(String tag, String log) {
        if (DEBUG)
            Log.i(tag, log);
    }

    public static final void w(String tag, String log) {
        if (DEBUG)
            Log.w(tag, log);
    }

    public static final void e(String tag, String log) {
        if (DEBUG)
            Log.e(tag, "" + log);
    }

    public static void e(String tag, String msg, Throwable e) {
        if (DEBUG)
            Log.e(tag, msg, e);
    }
}