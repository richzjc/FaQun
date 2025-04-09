package com.wallstreetcn.helper.utils.snack;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.UtilsContextManager;


/**
 * Created by zhangyang on 16/1/13.
 */
public class MToastHelper {
    private static long TimeMills = 3 * 1000;

    public static void toast(String content) {
        showToast(content, 1000, 0, Gravity.BOTTOM);
    }

    private static String lastToast = "";
    private static long lastToastTime;

    public static void showToast(String message, int duration, int icon,
                                 int gravity) {
        try {
            if (message != null && !message.equalsIgnoreCase("")) {
                long time = System.currentTimeMillis();
                if (!message.equalsIgnoreCase(lastToast) || Math.abs(time - lastToastTime) > TimeMills) {
                    View view = LayoutInflater.from(UtilsContextManager.getInstance().getApplication()).inflate(
                            R.layout.helper_toast, null);
                    ((TextView) view.findViewById(R.id.title_tv)).setText(message);
                    if (icon != 0) {
                        ((ImageView) view.findViewById(R.id.icon_iv))
                                .setImageResource(icon);
                        view.findViewById(R.id.icon_iv)
                                .setVisibility(View.VISIBLE);
                    }
                    Toast toast = new Toast(UtilsContextManager.getInstance().getApplication());
                    toast.setView(view);
                    if (gravity == Gravity.TOP) {
                        toast.setGravity(gravity, 0, 0);
                    } else {
                        toast.setGravity(gravity, 0, 35);
                    }
                    toast.setDuration(duration);
                    toast.show();
                    lastToast = message;
                    lastToastTime = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(String message) {
        try {
            Context context = UtilsContextManager.getInstance().getApplication();
            View v = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
            Toast sToast = new Toast(context);
            sToast.setView(v);
            sToast.setText(message);
            sToast.setDuration(Toast.LENGTH_SHORT);
            sToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showToast(String message, int duration) {
        try {
            Context context = UtilsContextManager.getInstance().getApplication();
            View v = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
            Toast sToast = new Toast(context);
            sToast.setView(v);
            sToast.setText(message);
            sToast.setDuration(duration);
            sToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showToast(int resId, int gravity, int duration) {
        try {
            View view = LayoutInflater.from(UtilsContextManager.getInstance().getApplication()).inflate(resId, null);
            Toast toast = new Toast(UtilsContextManager.getInstance().getApplication());
            toast.setView(view);
            toast.setGravity(gravity, 0, 0);
            toast.setDuration(duration);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
