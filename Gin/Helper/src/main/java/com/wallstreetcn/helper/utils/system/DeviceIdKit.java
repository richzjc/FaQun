package com.wallstreetcn.helper.utils.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.wallstreetcn.helper.utils.SharedPrefsUtil;
import com.wallstreetcn.helper.utils.TLog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 产品经理为较全面统计，隐私政策未同意，生成匿名id
 * 安装期间有效
 * 暂且这样干
 */
public class DeviceIdKit {

    private static final String TAG = "DeviceIdKit";

    public static String getDeviceId(Context context) {
        String deviceId = "";
        try {
            SharedPreferences sp = SharedPrefsUtil.getPrefs(context);
            String anonymous_device_id = sp.getString("ANONYMOUS_DEVICE_ID", "");
            if (TextUtils.isEmpty(anonymous_device_id)) {
                long installTimeHash = getInstallTimeHash(context);
                TLog.i(TAG, "install time hash:" + installTimeHash);
                long leastSigBits = System.currentTimeMillis();
                TLog.i(TAG, "current time (leastSigBits):" + leastSigBits);
                String uid = new UUID(installTimeHash, leastSigBits).toString();
                TLog.d(TAG, "UUID:" + uid);
                deviceId = md5(uid);
                sp.edit().putString("ANONYMOUS_DEVICE_ID", deviceId).apply();
            } else {
                deviceId = anonymous_device_id;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TLog.e(TAG, "Get device id exception!!!");
        }
        TLog.i(TAG, "DeviceIdKit  deviceId:" + deviceId);
        return deviceId;
    }

    private static long getInstallTimeHash(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            long installTime = pInfo.firstInstallTime;
            TLog.i(TAG, "getInstallTime:" + installTime);
            return Long.hashCode(installTime);
        } catch (Exception e) {
            e.printStackTrace();
            long ctime = System.currentTimeMillis();
            return Long.hashCode(ctime);
        }
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}
