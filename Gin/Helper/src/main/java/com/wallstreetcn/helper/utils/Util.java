package com.wallstreetcn.helper.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.router.RouterHelper;
import com.wallstreetcn.helper.utils.system.EquipmentUtils;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangjianchuan on 2016/6/23.
 */
public class Util {

    public static int getFontIndex() {
        return SharedPrefsUtil.getInt("font", 15); // 默认中字体
    }

    public static void setFontIndex(int fontIndex) {
        SharedPrefsUtil.saveInt("font", fontIndex);
    }

    public static int getFontIndexValue() {
        return SharedPrefsUtil.getInt("fontIndex", 2);
    }

    public static void setFontIndexValue(int fontIndexValue) {
        SharedPrefsUtil.saveInt("fontIndex", fontIndexValue);
    }

    public static String weekdayConvert(Calendar calendar) {
        String weekDay = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_one_text);
                break;
            case 3:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_two_text);
                break;
            case 4:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_three_text);
                break;
            case 5:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_four_text);
                break;
            case 6:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_five_text);
                break;
            case 7:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_six_text);
                break;
            case 1:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_day_text);
                break;
            default:
                break;
        }
        return ResourceUtils.getResStringFromId(R.string.helper_week_text) + weekDay;
    }

    public static boolean getIsNightMode() {
        if (SharedPrefsUtil.getInt("night_mode_key", 1) == 2) {
            return true;
        } else if (SharedPrefsUtil.getInt("night_mode_key", 1) == 3) {
            return false;
        } else if (SharedPrefsUtil.getInt("night_mode_key", 1) == 1 && UtilsContextManager.getInstance().getApplication() != null) {
            int currentNightMode = UtilsContextManager.getInstance().getApplication().getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
        } else {
            return false;
        }
    }

    public static Boolean getIsNoImage() {
        return SharedPrefsUtil.getBoolean("config", "no_image", false); // 默认任何环境浏览图片
    }


    public static Boolean isConnectWIFI() {
        try {
            ConnectivityManager connectManager = (ConnectivityManager) getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectManager.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI && info.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPayment() {
        String metaDate = ResourceUtils.getMetaDateFromName("WALL_PAYMENT");
        return TextUtils.equals("PAYMENT", metaDate);
    }

    public static boolean isGooglePlayChannel() {
        String metaData = ResourceUtils.getMetaDateFromName("WALL_CHANNEL_TYPE");
        return TextUtils.equals("WALL_GOOGLE", metaData);
    }

    private static boolean isProHuawei() {
        String metaData = ResourceUtils.getMetaDateFromName("WALL_TYPE");
        return TextUtils.equals("WALL_PRO_HUAWEI", metaData);
    }

    public static boolean isWscnVip() {
        String metaData = ResourceUtils.getMetaDateFromName("WALL_TYPE");
        return TextUtils.equals("WALL_VIP_APP", metaData);
    }

    public static boolean isGlobalNews() {
        String metaData = ResourceUtils.getMetaDateFromName("WALL_TYPE");
        return TextUtils.equals("WALL_GLOBAL_APP", metaData);
    }

    public static boolean isNormalApp() {
        String metaData = ResourceUtils.getMetaDateFromName("WALL_TYPE");
        return TextUtils.equals("WALL_APP_NORMAL", metaData);
    }

    public static boolean canReusable() {
        return true;
    }

    public static boolean isPayCharge() {
        return isGooglePlayChannel();
    }


    public static boolean isHwChannel() {
        return TextUtils.equals("wscn15", EquipmentUtils.getChannel());
    }

    private static String getKeyUnderReview() {
        return EquipmentUtils.getChannel() + "_" + EquipmentUtils.getVersionCode();
    }

    public static void setUnderReview(boolean underReview) {
        SharedPrefsUtil.saveBoolean(getKeyUnderReview(), underReview);
    }

    public static boolean getUnderReview() {
        return SharedPrefsUtil.getBoolean(getKeyUnderReview(), true);
    }

    public static boolean isVivoChannelAndUnderReview() {//vivo 渠道审核中
        if (isChannel("vivo")) return getUnderReview();
        else return false;
    }

    public static boolean isBaiduChannelAndUnderReview() {
        if (isChannel("baidu")) return getUnderReview();
        else return false;
    }

    public static boolean isWscnVipHwUnderReview() {
        if (!isWscnVip()) return false;
        if (!isHwChannel()) return false;
        return getUnderReview();
    }

    private static boolean isChannel(String channel) {
        return TextUtils.equals(channel, EquipmentUtils.getChannel());
    }

    private static Context getApplication() {
        return UtilsContextManager.getInstance().getApplication();
    }

    /**
     * 关键字高亮显示(此处特别处理了关键字的每一个字符匹配;若无此要求，可以去掉for循环)
     *
     * @param target 需要高亮的关键字
     * @param text   需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target, int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        for (int i = 0; i < target.length(); i++) {
            Pattern p = Pattern.compile(Character.toString(target.charAt(i)));
            Matcher m = p.matcher(text);
            while (m.find()) {
                span = new ForegroundColorSpan(color);// 需要重复！
                spannable.setSpan(span, m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannable;
    }

    private static int statusBarHeight = 0;

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight != 0)
            return statusBarHeight;
        Activity activity = RouterHelper.getActivity(context);
        if (activity != null) {
            ViewCompat.setOnApplyWindowInsetsListener(activity.getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
                @NonNull
                @Override
                public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                    statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
                    TLog.e("statausBarHeight", "" + statusBarHeight);
                    return insets;
                }
            });
            if (statusBarHeight <= 0)
                return ScreenUtils.dip2px(30f);
            else
                return statusBarHeight;
        } else {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return context.getResources().getDimensionPixelSize(resourceId);
            }
            return ScreenUtils.dip2px(30);
        }
    }

    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    public static Drawable getDrawableByName(String name, Context context) {
        try {
            int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            return ContextCompat.getDrawable(context, resId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ColorDrawable(Color.TRANSPARENT);
        }
    }

    public static String getStringByName(String name, Context context) {
        try {
            int resId = context.getResources().getIdentifier(name, "string", context.getPackageName());
            return ResourceUtils.getResStringFromId(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getProcessName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public static String getMD5Signature(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] publicKey = md.digest(cert);
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toLowerCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
//                hexString.append(":");
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
