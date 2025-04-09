package com.wallstreetcn.webview.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.wallstreetcn.helper.utils.TLog;
import androidx.annotation.NonNull;

import com.wallstreetcn.helper.utils.UtilsContextManager;

import java.io.File;

import static android.os.Build.VERSION_CODES.N;

public final class Abi64WebViewCompat {
    //参见源码： com.android.webview.chromium.WebViewChromiumFactoryProvider
    private static final String CHROMIUM_PREFS_NAME = "WebViewChromiumPrefs";

    private static final String APP_WEB_VIEW_DIR_NAME = "app_webview";

    private static final String GPU_CACHE_DIR_NAME = "GPUCache";

    public static void obliterate() {
        if (Build.VERSION.SDK_INT < N) {
            return;
        }

        try {
            final Context appContext = UtilsContextManager.getInstance().getApplication();

            //移除：shared_prefs/WebViewChromiumPrefs.xml
            final SharedPreferences chromiumPrefs = appContext.getSharedPreferences(
                    CHROMIUM_PREFS_NAME,
                    Context.MODE_PRIVATE
            );
            chromiumPrefs.edit().clear().apply();

            //移除：app_webview 目录
            final File appWebViewDir = new File(
                    appContext.getDataDir() + File.separator
                            + APP_WEB_VIEW_DIR_NAME + File.separator
                            + GPU_CACHE_DIR_NAME
            );
            deleteRecursive(appWebViewDir);
        } catch (Exception e) {
            printInfo(e.getMessage());
        }
    }

    private static void deleteRecursive(@NonNull File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            printInfo("确实找到了这样的一个目录" + fileOrDirectory.getAbsolutePath());
            for (File child : fileOrDirectory.listFiles()) {
                printInfo("确实找到了这样的一个目录");
                deleteRecursive(child);
            }
        }
        boolean isSuccessDelete = fileOrDirectory.delete();
        printInfo("delete isSuccessDelete: " + isSuccessDelete + " fileName: " + fileOrDirectory);
    }

    private static void printInfo(@NonNull String message) {
        TLog.i("Abi64WebViewCompat", message);
    }
}
