package com.wallstreetcn.helper.utils.system;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.UtilsContextManager;
import com.wallstreetcn.helper.utils.snack.MToastHelper;

/**
 * Created by zhangyang on 16/7/6.
 */
public class EquipmentUtils {

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getMobileType() {
        return Build.MODEL;
    }

    /**
     * 获取手机系统版本
     *
     * @return
     */
    public static String getMobileSystem() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取IMEI
     *
     * @return
     */
    public static String getIMEI() {
        //return DefaultDeviceIdUtilKt.anonymousDeviceId(UtilsContextManager.getInstance().getApplication());
        return DeviceIdKit.getDeviceId(UtilsContextManager.getInstance().getApplication());
    }

    public static String getSecureId(Context context){
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static String getAndroidId() {
        return android.os.Build.SERIAL;
    }

    private static Application getApplication() {
        return UtilsContextManager.getInstance().getApplication();
    }


    public static String getPackageName() {
        return getApplication().getPackageName();
    }

    public static String getVersionName() {
        try {
            PackageManager packManager = getApplication().getPackageManager();
            PackageInfo packInfo = packManager.getPackageInfo(
                    getApplication().getPackageName(), 0);
            return packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode() {
        try {
            PackageManager packManager = getApplication().getPackageManager();
            PackageInfo packInfo = packManager.getPackageInfo(
                    getApplication().getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getBrandName() {
        return android.os.Build.BRAND;
    }

    public static String getModelName() {
        return android.os.Build.MODEL;
    }

    public static String getBuildName() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getAppName() {
        return getApplication().getResources().getString(R.string.app_name);
    }

    public static String initPackerNgChannel() {
        Application application = getApplication();
        ChannelInfo channelInfo= WalleChannelReader.getChannelInfo(application);
        if (channelInfo != null) {
            return channelInfo.getChannel();
        }
        String market = TextUtils.isEmpty(channel) ? application.getResources()
                .getString(R.string.default_channel_id) : channel;
        return market;
    }

    public static String initPackerNgChannel(Context application) {
        ChannelInfo channelInfo= WalleChannelReader.getChannelInfo(application);
        if (channelInfo != null) {
            return channelInfo.getChannel();
        }
        String market = TextUtils.isEmpty(channel) ? application.getResources()
                .getString(R.string.default_channel_id) : channel;
        return market;
    }


    private static String channel = "";

    public static String getChannel() {
        if (TextUtils.isEmpty(channel))
            channel = initPackerNgChannel();
        return channel;
    }

    public static String getChannel(Context context) {
        if (TextUtils.isEmpty(channel))
            channel = initPackerNgChannel(context);
        return channel;
    }


    public static String getAppHttpHeader() {
        String platform = Util.isPayment() ? "wscnPro" : "wscn";
        if (Util.isGlobalNews())
            platform = "wscn-globalnews";
        return platform + "|" + getAppHeader();
    }

    public static String getAppHeader() {
        String header = "Android|" + getVersionName() + "|" + getBuildName();
        header += "|" + getChannel();
        header += "|" + getPackageName();
        return header;
    }

    public static boolean checkCanDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Intent drawOverlaysSettingsIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                drawOverlaysSettingsIntent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(drawOverlaysSettingsIntent);
                return false;
            }
        }
        return true;
    }

    public static boolean isInstallApp(Intent intent, Context context) {
        return context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

}
