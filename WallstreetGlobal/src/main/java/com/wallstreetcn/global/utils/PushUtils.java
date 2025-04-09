package com.wallstreetcn.global.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;
import com.wallstreetcn.helper.utils.TLog;

import com.wallstreetcn.baseui.manager.AppManager;

import java.util.Arrays;

/**
 * Created by ichongliang on 2017/12/20.
 */

public class PushUtils {

    public static boolean isMutePush = false;

    public static boolean supportEmuiPushVersion(String emuiVersion) {
        try {
            emuiVersion = emuiVersion.substring(emuiVersion.indexOf("_") + 1, emuiVersion.length());
            TLog.i("PushFactory", emuiVersion);
            String[] versions = emuiVersion.split("\\.");
            TLog.i("PushFactory", Arrays.toString(versions));
            return Integer.valueOf(versions[0]) > 4 || (Integer.valueOf(versions[0]) == 4 && Integer.valueOf(versions[1]) > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void openNotification(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        context.startActivity(intent);
    }

    public static String getSystemNotificationOn() {
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity == null)
            return "";
        return getNotificationEnable(activity) ? "on" : "off";
    }

    public static boolean getNotificationEnable(Context context) {
        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        return compat.areNotificationsEnabled();
    }
}
