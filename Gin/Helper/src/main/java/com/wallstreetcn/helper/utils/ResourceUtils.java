package com.wallstreetcn.helper.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.wallstreetcn.helper.R;

/**
 * Created by zhangyang on 16/3/15.
 */
public class ResourceUtils {
    public static int getId(Context context, String name) {
        Resources res = context.getResources();
        return res.getIdentifier(name, "id", context.getPackageName());
    }

    public static int getStringId(Context context, String name) {
        Resources res = context.getResources();
        return res.getIdentifier(name, "string", context.getPackageName());
    }


    public static int getDrawableId(Context context, String name, String packageName) {
        Resources res = context.getResources();
        return res.getIdentifier(name, "drawable", packageName);
    }

    public static int getColorId(Context context, String name) {
        Resources res = context.getResources();
        return res.getIdentifier(name, "color", context.getPackageName());
    }


    public static int getLayoutId(Context context, String name) {
        Resources res = context.getResources();
        return res.getIdentifier(name, "layout", context.getPackageName());
    }

    public static int getDrawableId(Context context, String name) {
        Resources res = context.getResources();
        return res.getIdentifier(name, "drawable", context.getPackageName());
    }

    public static Drawable getResDrawableFromID(int resId) {
        return ContextCompat.getDrawable(getApplication(), resId);
    }


    public static String getDrawablePath(@DrawableRes int id) {
        Resources resources = getApplication().getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

    public static boolean getResBooleanFromId(int resId) {
        return getApplication().getResources().getBoolean(resId);
    }

    public static int[] getResIntArrayFromId(int resId) {
        return getApplication().getResources().getIntArray(resId);
    }

    public static String[] getResStringArrayFromId(int resId) {
        return getApplication().getResources().getStringArray(resId);
    }

    public static Integer getInteger(int resId) {
        return getApplication().getResources().getInteger(resId);
    }

    public static TypedArray obtainTypedArray(int resId) {
        return getApplication().getResources().obtainTypedArray(resId);
    }

    public static String getResStringFromId(int resId) {
        Resources resources = getApplication().getResources();
        String appName = getAppName();
        String value = resources.getString(resId);
        String replaceZh = resources.getString(R.string.helper_main_app_name_zh);
        String replaceZhTw = resources.getString(R.string.helper_main_app_name_zh_rtw);
        value = value.replaceAll(replaceZh, appName);
        value = value.replaceAll(replaceZhTw, appName);
        return value;
    }

    public static String getResStringFromId(int resId, Object... formatArgs) {
        Resources resources = getApplication().getResources();
        String appName = getAppName();
        String value = resources.getString(resId, formatArgs);
        String replaceZh = resources.getString(R.string.helper_main_app_name_zh);
        String replaceZhTw = resources.getString(R.string.helper_main_app_name_zh_rtw);
        value = value.replaceAll(replaceZh, appName);
        value = value.replaceAll(replaceZhTw, appName);
        return value;
    }

    public static int getColor(int colorId) {
        return ContextCompat.getColor(getApplication(), colorId);
    }


    public static String getMetaDateFromName(String name) {
        try {
            ApplicationInfo appInfo = getApplication().getPackageManager()
                    .getApplicationInfo(getApplication().getPackageName(),
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean getBooleanFromResource(int id) {
        return getApplication().getResources().getBoolean(id);
    }

    private static Context getApplication() {
        if (UtilsContextManager.getInstance().getActivityContext() == null) {
            return UtilsContextManager.getInstance().getApplication();
        }
        return UtilsContextManager.getInstance().getActivityContext();
    }

    public static synchronized String getAppName() {
        try {
            Context context = getApplication();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
