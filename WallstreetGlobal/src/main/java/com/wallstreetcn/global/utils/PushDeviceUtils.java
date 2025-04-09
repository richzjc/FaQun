package com.wallstreetcn.global.utils;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import com.heytap.msp.push.HeytapPushManager;
import com.hihonor.push.sdk.HonorPushClient;
import com.wallstreetcn.helper.utils.UtilsContextManager;

import java.lang.reflect.Method;

/**
 * Created by Leif Zhang on 16/8/26.
 * Email leifzhanggithub@gmail.com
 */
public class PushDeviceUtils {

    // 检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    //检测EMUI
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    public static boolean isMIUI() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            String miui = (String) get.invoke(c, "ro.miui.ui.version.code");
            return !TextUtils.isEmpty(miui);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public static boolean checkEMUI() {
        try {
            String emuiVersion = getEmuiVersion();
            if (TextUtils.isEmpty(emuiVersion)) {
                return false;
            }
            return PushUtils.supportEmuiPushVersion(emuiVersion);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMiuiOrEmUui(){
        Application application = UtilsContextManager.getInstance().getApplication();;
        return isMIUI() || checkEMUI() || HonorPushClient.getInstance().checkSupportHonorPush(application) || HeytapPushManager.isSupportPush(application);
    }

    private static final String TAG = "PushLib";

    public static String getEmuiVersion() {
        String emuiVerion = "";
        Class<?>[] clsArray = new Class<?>[]{String.class};
        Object[] objArray = new Object[]{"ro.build.version.emui"};
        try {
            Class<?> SystemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method get = SystemPropertiesClass.getDeclaredMethod("get",
                    clsArray);
            String version = (String) get.invoke(SystemPropertiesClass,
                    objArray);
            Log.d(TAG, "get EMUI version is:" + version);
            if (!TextUtils.isEmpty(version)) {
                return version;
            }
        } catch (ClassNotFoundException e) {
            Log.e(TAG, " getEmuiVersion wrong, ClassNotFoundException");
        } catch (LinkageError e) {
            Log.e(TAG, " getEmuiVersion wrong, LinkageError");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, " getEmuiVersion wrong, NoSuchMethodException");
        } catch (NullPointerException e) {
            Log.e(TAG, " getEmuiVersion wrong, NullPointerException");
        } catch (Exception e) {
            Log.e(TAG, " getEmuiVersion wrong");
        }
        return emuiVerion;
    }
}
