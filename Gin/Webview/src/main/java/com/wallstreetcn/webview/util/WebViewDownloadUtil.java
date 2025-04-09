package com.wallstreetcn.webview.util;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.URLUtil;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions3.RxPermissionsNew;
import com.wallstreetcn.helper.utils.snack.MToastHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class WebViewDownloadUtil {
    public static void startDownload(String url, String contentDescription, String mimeType, Context context) {
        if (TextUtils.isEmpty(url))
            return;
        requestPermission(url, contentDescription, mimeType, context);
    }

    private static void realDownload(String url, String contentDescription, String mimeType, Context context) {
        registerReceiver(context);
        MToastHelper.showToast("正在下载中...");
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverMetered(false);
        request.setVisibleInDownloadsUi(true);
        request.setAllowedOverRoaming(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        String fileName = URLUtil.guessFileName(url, contentDescription, null);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private static void requestPermission(final String url, String contentDescritpion, final String mimeType, final Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            realDownload(url, contentDescritpion, mimeType, context);
        } else if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            realDownload(url, contentDescritpion, mimeType, context);
        } else if (context instanceof Activity) {
            RxPermissionsNew.requestPermissions(
                            (FragmentActivity) context,
                            "该操作需获取本地文件读写权限，请确认下一步操作",
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            realDownload(url, contentDescritpion, mimeType, context);
                        }
                    });
        }
    }

    private static void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(new DownloadCompleteReceiver(), filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            context.registerReceiver(new DownloadCompleteReceiver(), filter);
        }

    }
}
