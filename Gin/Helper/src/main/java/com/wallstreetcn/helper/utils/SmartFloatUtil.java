
package com.wallstreetcn.helper.utils;


import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import com.wallstreetcn.helper.utils.TLog;
import android.view.WindowManager;

import com.wallstreetcn.helper.utils.system.EquipmentUtils;

import java.lang.reflect.Method;

/**
 * 浮窗入口判断相关逻辑
 * Created by liuguangli on 16/3/2.
 */
public class SmartFloatUtil {
    private static final String TAG = "FloatWinEnter";
    public static final int HAS_AUTHOR = 1;
    private static final int NOT_AUTHOR = 2;
    private static final int NOT_SUPPORT_AUTHOR = 3;
    private static final String[] INPUT_METHOD_SPECIAL_MODELS = {"SAMSUNG"};
    ;
    private static Integer[] specialVersions;
    public static final String SPECIAL_MODELS[] = {
            "MI",
            "XIAOMI",
            "HUAWEI",
            "OPPO",
    };

    /**
     * 判断当前是否特殊机型
     *
     * @return
     */
    public static boolean isSpecialDevice() {
        String specialDevices[] = getSpecialDevices();
        String currentDevice = getBuildModel();
        if (currentDevice == null) {
            return false;
        }
        currentDevice = currentDevice.toUpperCase();
        for (String d : specialDevices) {
            if (currentDevice.contains(d)) {
                return true;
            }
        }
        return false;
    }

    private static String[] getSpecialDevices() {
        return SPECIAL_MODELS;
    }

    /**
     * 判断是否是特殊版本（14～17）
     *
     * @return
     */
    public static boolean isSpecialVersion() {
        int systemVersion = getSystemVersion();

        return systemVersion >= 14 && systemVersion <= 18;
    }

    /**
     * 判断是否开启浮窗权限
     *
     * @return
     */
    public static int hasAuthorFloatWin(Context context) {

        if (getSystemVersion() < 19) {
            return NOT_SUPPORT_AUTHOR;
        }
        try {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class c = appOps.getClass();
            Class[] cArg = new Class[3];
            cArg[0] = int.class;
            cArg[1] = int.class;
            cArg[2] = String.class;
            Method lMethod = c.getDeclaredMethod("checkOp", cArg);
            if (AppOpsManager.MODE_ALLOWED == (Integer) lMethod.invoke(appOps, 24, Binder.getCallingUid(), context.getPackageName())) {
                return HAS_AUTHOR;
            } else {
                return NOT_AUTHOR;
            }

        } catch (Exception e) {
            return NOT_SUPPORT_AUTHOR;
        }

    }


    /**
     * 浮窗(包含探测流程)
     *
     * @param context
     */
    public static int askType(final Context context) {
        TLog.d(TAG, "SharedStaticField.HOST_VISIBLE = false;");
        int winType = WindowManager.LayoutParams.TYPE_TOAST;
        if (isSpecialVersion()) {
            //Android 4.4 以下,google api 来看这个时候还没有对SYSTEM_PHONE类型的浮窗进行权限管理，使用 TYPE_PHONE
            winType = WindowManager.LayoutParams.TYPE_PHONE;

        } else {

            /**
             * Android 4.4 以上,有的厂商开始对SYSTEM_PHONE进行权限控制.
             * 好在Android 4.4 可以使用TOAST类型浮窗实现绕过权限,但是TOAST类型浮窗无法定位输入法,只能全屏幕显示输入法.
             * 方案:对特殊机型进行SYSTEM_PHONE权限检查,若已经授权使用SYSTEM_PHONE,若未授权使用TOAST类型浮窗
             */

            if ((isSpecialDevice() && hasAuthorFloatWin(context) == HAS_AUTHOR)) {
                winType = WindowManager.LayoutParams.TYPE_PHONE;

            } else if (isSpecialDevice() && hasAuthorFloatWin(context) != HAS_AUTHOR) {
                //特殊继续未授权
                winType = WindowManager.LayoutParams.TYPE_TOAST;
            } else if (isInputMethodSpecialDevice()) {
                //这种机型的Toast压根不支持输入法了,必须使用TYPE_PHONE
                TLog.e(TAG, "注意浮窗对输入法支持不友好");
                winType = WindowManager.LayoutParams.TYPE_TOAST;

            }
            TLog.d(TAG, "启动" + winType + "方式的浮窗");

        }
        return winType;
    }


    private static boolean isInputMethodSpecialDevice() {
        String specials[] = INPUT_METHOD_SPECIAL_MODELS;
        String currentDevice = getBuildModel();
        if (currentDevice != null) {
            currentDevice = currentDevice.toUpperCase();
        }
        for (String dev : specials) {
            if (currentDevice.contains(dev)) {
                return true;
            }
        }
        return false;
    }


    public static String getBuildModel() {

        return Build.MANUFACTURER + Build.MODEL;
    }

    /**
     * @return
     */
    public static int getSystemVersion() {

        return Build.VERSION.SDK_INT;
    }


    /**
     * 判断悬浮窗权限
     *
     */
    public static boolean isFloatWindowOpAllowed() {
        Context context = UtilsContextManager.getInstance().getApplication();
        final int version = Build.VERSION.SDK_INT;

        if (version >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else if (version >= 19) {
            return checkOp(context, 24);//24表示悬浮窗权限在AppOpsManager中
        } else {
            //0x8000000表示1000000000000000000000000000如果&第28位所得值为1则该位置被置为1，悬浮窗打开
            return (context.getApplicationInfo().flags & 0x8000000) == 1 << 27;
        }
    }

    protected static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Object object = invokeMethod(manager, "checkOp", op, Binder.getCallingUid(), getPackageName(context));
                return AppOpsManager.MODE_ALLOWED == (Integer) object;
            } catch (Exception e) {
                TLog.e("CheckMIUI", e.toString());
            }
        } else {
            TLog.e("CheckMIUI", "Below API 19 cannot invoke!");
        }
        return false;
    }

    public static String getPackageName(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo.packageName;
    }

    public static Object invokeMethod(AppOpsManager manager, String method, int op, int uid, String packageName) {
        Class c = manager.getClass();
        try {
            Class[] classes = new Class[]{int.class, int.class, String.class};
            Object[] x2 = {op, uid, packageName};
            Method m = c.getDeclaredMethod(method, classes);
            return m.invoke(manager, x2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void goAppSetting(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        context.startActivity(localIntent);
    }

    public static void requestFloatPermission(Context context) throws ActivityNotFoundException{
        if (EquipmentUtils.checkCanDrawOverlays(context))
            goAppSetting(context);
    }


}
