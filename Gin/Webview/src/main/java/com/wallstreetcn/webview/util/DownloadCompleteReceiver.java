package com.wallstreetcn.webview.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wallstreetcn.helper.utils.snack.MToastHelper;

public class DownloadCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && TextUtils.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE, intent.getAction())){
            MToastHelper.showToast("下载完成");
            context.unregisterReceiver(this);
        }
    }
}
