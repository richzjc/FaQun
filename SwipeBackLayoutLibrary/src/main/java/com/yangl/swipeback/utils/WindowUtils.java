package com.yangl.swipeback.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.WindowManager;

import androidx.core.app.ActivityOptionsCompat;

import java.lang.reflect.Method;

/***
 * @Author: YangLang
 * @Version: v1.0
 * @Description: xxx
 */
public class WindowUtils {

    public static void convertActivityToTranslucent(Activity activity) {
        convertActivityToTranslucentAfterL(activity);
    }

    /**
     * Calling the convertToTranslucent method on platforms after Android 5.0
     */
    private static void convertActivityToTranslucentAfterL(Activity activity) {
        if (Build.VERSION.SDK_INT >= 30)
            activity.setTranslucent(true);
        else {
            try {
                Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
                getActivityOptions.setAccessible(true);
                Object options = getActivityOptions.invoke(activity);

                Class<?>[] classes = Activity.class.getDeclaredClasses();
                Class<?> translucentConversionListenerClazz = null;
                for (Class<?> clazz : classes) {
                    if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                        translucentConversionListenerClazz = clazz;
                    }
                }

                Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent",
                        translucentConversionListenerClazz, ActivityOptions.class);
                convertToTranslucent.setAccessible(true);
                convertToTranslucent.invoke(activity, null, options);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    public static boolean isTransparentStatusBar(Activity activity) {
        return (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
