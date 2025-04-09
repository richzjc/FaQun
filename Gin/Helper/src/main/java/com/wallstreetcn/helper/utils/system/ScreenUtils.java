package com.wallstreetcn.helper.utils.system;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.UtilsContextManager;

/**
 * Created by zhangyang on 16/7/6.
 */
public class ScreenUtils {

    public static int getScreenWidth() {
        DisplayMetrics dm = getApplication().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics dm = getApplication().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    private static Context getApplication() {
        return UtilsContextManager.getInstance().getApplication();
    }

    public static int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) Math.ceil(dpValue * scale + 0.5f);
    }

    public static float density() {
        return getApplication().getResources().getDisplayMetrics().density;
    }

    public static float perInch(){
        // 获取 DisplayMetrics 对象
        DisplayMetrics displayMetrics = new DisplayMetrics();
        UtilsContextManager.getInstance().getActivityContext().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // 获取屏幕宽度（像素）
        int screenWidthPixels = displayMetrics.widthPixels;

        // 获取水平像素密度（xdpi）
       return displayMetrics.xdpi;
    }


    // 将px值转换为sp值
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }



    public static int[] getDisplayWH(Context context) {
        int screenWidth, screenHeight;
        int[] wh = new int[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
        screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）

        wh[0] = screenWidth;
        wh[1] = screenHeight;
        return wh;
    }

    public static int sp2px(float spValue) {
        final float fontScale = getApplication().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int sp2px(float spValue, Resources resources) {
        final float fontScale = resources.getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int px2dip(int px) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static float getSystemBarHeight(Activity activity) {
        Rect rectangle = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }


    public static float getY(View v) {
        if (v != null) {
            return v.getTop() + getParentY(v.getParent());
        }
        return 0;
    }

    private static float getParentY(ViewParent parent) {
        if (parent instanceof ViewGroup) {
            return ((ViewGroup) parent).getTop() + getParentY(parent.getParent());
        }
        return 0;
    }


    /**
     * 状态栏高度
     */
    public static int getStatusBarHeight() {
        return Util.getStatusBarHeight(getApplication());
    }

}
