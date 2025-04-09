package com.wallstreetcn.helper.utils.file;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.View.MeasureSpec;
import androidx.core.app.ActivityCompat;
import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.snack.MToastHelper;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QDUtil {

    public static Bitmap convertViewToBitmap(View v) {
        return convertViewToBitmap(v, 0, 0);
    }

    public static Bitmap convertViewToBitmap(View v, int left, int top) {
        try {
            if(v.getWidth() > 0 && v.getHeight() > 0 && v.getWidth() == ScreenUtils.getScreenWidth()){
                v.measure(MeasureSpec.makeMeasureSpec(ScreenUtils.getScreenWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(v.getHeight(), MeasureSpec.EXACTLY));
            }else{
                v.measure(MeasureSpec.makeMeasureSpec(ScreenUtils.getScreenWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            }

            v.layout(left, top, left + v.getMeasuredWidth(), top + v.getMeasuredHeight());
            Bitmap screenshot = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Config.ARGB_8888);
            v.draw(new Canvas(screenshot));
            return screenshot;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String saveShareViewToDisk(Context context, View shareView) throws Exception {
        Bitmap shareBitmap = convertViewToBitmap(shareView);
        if (shareBitmap == null) {
            return "";
        }

        return realSave(context, shareBitmap);
    }


    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    public static String realSave(Context context, Bitmap shareBitmap) {
        IOException e;
        String name = "shareTextImage-" + System.currentTimeMillis() + ".jpg";
        File file = new File(getShareImageCache(context).getAbsolutePath(), name);
        if (!file.exists()) {
            FileOutputStream out = null;
            try {
                FileOutputStream out2 = new FileOutputStream(file);
                try {
                    shareBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                    out2.flush();
                    out2.close();
                    if (out2 != null) {
                        try {
                            out2.close();
                            out = out2;
                        } catch (Throwable e2) {
                            out = out2;
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    out = out2;
                    try {
                        e.printStackTrace();
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable e22) {
                            }
                        }
                    } catch (Throwable th2) {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable e222) {
                            }
                        }
                    }
                } catch (Throwable th3) {
                    out = out2;
                    if (out != null) {
                        out.close();
                    }
                }
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                return "";
            }
        }
        try {
            if (file.length() > 0)
                return file.getAbsolutePath();
            else
                return "";
        } catch (Exception e1) {
            e1.printStackTrace();
            return "";
        }
    }


    private static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        return state != null && state.equals("mounted");
    }

    private static boolean checkSDCardMounted() {
        boolean result = hasSDCardMounted();
        if (!result) {
            MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.helper_no_sd_card_tips));
        }
        return result;
    }

    public static File getShareImageCache(Context context) {
        File cacheDir = null;
        if (cacheDir == null && checkSDCardMounted()) {
            cacheDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }else if(cacheDir == null)
            cacheDir = context.getCacheDir();

        File shareImage = new File(cacheDir.getAbsolutePath(), "/wscnShareImage");
        if (!shareImage.exists()) {
            shareImage.mkdirs();
        }
        return shareImage;
    }
}
