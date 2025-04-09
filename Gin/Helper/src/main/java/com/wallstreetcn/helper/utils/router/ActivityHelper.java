package com.wallstreetcn.helper.utils.router;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.snack.MToastHelper;

import java.util.Map;

/**
 * Created by zhangyang on 16/2/3.
 */
public class ActivityHelper {

    public static void startActivityNotInActivity(Context context, Class targetActivity, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivity(final Activity activity, final Class targetActivity) {
        startActivity(activity, targetActivity, null, true);
    }

    public static void startActivity(Activity activity, Class targetActivity, Bundle bundle) {
        startActivity(activity, targetActivity, bundle, true);
    }

    public static void startActivity(Activity activity, Class targetActivity, Map<String, String> map, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, targetActivity);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent, activity);
    }

    public static void startActivity(Activity activity, Class targetActivity, Bundle bundle, boolean needAni) {
        if (!DoubleClickHelper.doubleClickCheck())
            return;
        Intent intent = new Intent();
        intent.setClass(activity, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent, activity, needAni);
    }

    public static void startActivity(Activity activity, Uri uri, Bundle bundle) {
        if (!DoubleClickHelper.doubleClickCheck())
            return;

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent, activity);
    }

    public static void startActivity(Activity activity, String action, Bundle bundle) {
        if (!DoubleClickHelper.doubleClickCheck())
            return;
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent, activity);
    }

    public static void startActivityForResult(Activity activity, Class targetActivity, int result) {
        startActivityForResult(activity, targetActivity, result, null);
    }


    public static void startActivityForResult(Activity activity, String action, Uri uri, int result) {
        startActivityForResult(activity, action, uri, result, null);
    }

    public static void startActivityForResult(Activity activity, String action, Uri uri, int result, Bundle bundle) {
        if (!DoubleClickHelper.doubleClickCheck())
            return;
        Intent intent = new Intent(action, uri);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, activity, result);
    }

    public static void startActivityForResult(Activity activity, Class targetActivity, int result, Bundle bundle) {
        if (!DoubleClickHelper.doubleClickCheck())
            return;
        Intent intent = new Intent();
        intent.setClass(activity, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, activity, result);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int result, Bundle bundle) {
        if (intent == null || !DoubleClickHelper.doubleClickCheck())
            return;
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, activity, result);
    }

    public static void startActivity(Intent intent, Activity activity) {
        startActivity(intent, activity, true);
    }

    public static void startActivity(Intent intent, Activity activity, boolean needAni) {
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startActivityForResult(Intent intent, Activity activity, int resultCode) {
        try {
            activity.startActivityForResult(intent, resultCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startActivity(Context context, Class activity, Bundle bundle) {
        try {
            Intent intent = new Intent();
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            intent.setClass(context, activity);
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void intentWeBrowser(Activity activity, String url) {
        try {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Uri uri = Uri.parse(url);
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 启动到应用商店app详情界面
     */
    public static void launchAppDetail(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Util.isPayment()) {
                intent.setPackage("com.huawei.appmarket");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void launchLanguageSetting(Activity context) {
        try {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            context.startActivityForResult(intent, 1234);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
