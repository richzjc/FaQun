package com.wallstreetcn.helper.utils.router;

/**
 * Created by zhangyang on 16/5/14.
 */
public class DoubleClickHelper {
    private static long downTime;

    public static void cleanDownTime() {
        downTime = 0;
    }

    public static boolean doubleClickCheck() {
        if (Math.abs(downTime - System.currentTimeMillis()) > 600) {
            downTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    private static long exitTime;

    public static boolean checkExitDoubleClick() {
        if (Math.abs(exitTime - System.currentTimeMillis()) > 2000) {
            exitTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }
}
