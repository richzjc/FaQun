package com.wallstreetcn.global.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.wallstreetcn.global.R;
import com.wallstreetcn.helper.utils.ResourceUtils;

public class AlertDialogUtils {



    public static void showDialogTips(Context context, String message, DialogInterface.OnClickListener listener
            , String sure, String cancel) {
        if(TextUtils.isEmpty(message))return;
        AlertDialog.Builder ab = new AlertDialog.Builder(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog_Alert)
                .setMessage(message)
                .setPositiveButton(sure, ((dialog, which) -> {
                    if (listener != null)
                        listener.onClick(dialog, which);
                }))
                .setNegativeButton(cancel, (dialog, which) -> {
                    if (listener != null)
                        listener.onClick(dialog, which);
                });
        ab.setCancelable(false);
        ab.show();
    }
}
