package com.wallstreetcn.helper.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wallstreetcn.helper.utils.snack.MToastHelper;

import java.util.List;

/**
 * Created by richzjc on 17/12/6.
 */

public class InstallUtil {

    public static boolean isInstallWX(Activity context, boolean needToast) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
        if (needToast && intent == null)
            MToastHelper.showToast("请先安装微信");
        return intent != null;
    }

    public static boolean isInstallWX(Activity context) {
//        try {
//            context.getPackageManager().getPackageInfo("com.tencent.mm", 0);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            MToastHelper.showToast("请先安装微信");
//            return false;
//        }
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
        return intent != null;
    }

    public static boolean isInstallQQ(Activity context) {
        try {
            context.getPackageManager().getPackageInfo("com.tencent.mobileqq", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            MToastHelper.showToast("请先安装QQ");
            return false;
        }

//        PackageManager manager = context.getPackageManager();
//        List<PackageInfo> pInfoList = manager.getInstalledPackages(0);
//        if (pInfoList != null) {
//            for (PackageInfo packageInfo : pInfoList) {
//                String pn = packageInfo.packageName;
//                if (TextUtils.equals(pn, "com.tencent.mobileqq"))
//                    return true;
//            }
//        }
//        MToastHelper.showToast("请先安装QQ");
//        return false;
    }

}
