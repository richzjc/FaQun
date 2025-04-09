package com.wallstreetcn.baseui.widget.pulltorefresh;

/**
 * Created by Leif Zhang on 16/8/18.
 * Email leifzhanggithub@gmail.com
 */
public class PTRHeaderConfig {
    public static String REFRESH_ICON = "";
    public static String REFRESH_PULL_TEXT = "";
    public static String REFRESH_RELEASE_TEXT = "";
    public static String REFRESH_REFRESHING_TEXT = "";
    public static float scale = 0.2f;
    private static Runnable mRunnable;

    public static void setPtrRunnable(Runnable runnable) {
        mRunnable = runnable;
    }

    public static Runnable getRunnable() {
        return mRunnable;
    }
}
