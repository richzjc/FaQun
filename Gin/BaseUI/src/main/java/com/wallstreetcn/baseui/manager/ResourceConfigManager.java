package com.wallstreetcn.baseui.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.SharedPrefsUtil;
import com.wallstreetcn.helper.utils.UtilsContextManager;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * Created by Leif Zhang on 16/9/22.
 * Email leifzhanggithub@gmail.com
 */
public class ResourceConfigManager {

    private static final String PREF_KEY = "nightModeState";
    public static final String NIGHT_SP_KEY = "night_mode_key";

    public final static int TYPE_SYSTEM = 1;
    public final static int TYPE_NIGHT = 2;
    public final static int TYPE_NORMAL = 3;

    private SharedPreferences mPrefs;
    private static final String LANGUAGE_TW = Locale.TRADITIONAL_CHINESE.toString();

    /**
     * Default behaviour is to automatically save the setting and restore it.
     */
    public ResourceConfigManager(AppCompatActivity activity) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        if (mPrefs.contains(PREF_KEY)) {
            int mode = mPrefs.getInt(PREF_KEY, Configuration.UI_MODE_NIGHT_UNDEFINED);
            if (mode == Configuration.UI_MODE_NIGHT_YES) {
                SharedPrefsUtil.saveInt(NIGHT_SP_KEY, TYPE_NIGHT);
            } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
                SharedPrefsUtil.saveInt(NIGHT_SP_KEY, TYPE_NORMAL);
            } else {
                SharedPrefsUtil.saveInt(NIGHT_SP_KEY, TYPE_SYSTEM);
            }
            mPrefs.edit().remove(PREF_KEY).apply();
        }
    }

    public static void saveNightType(int type) {
        SharedPrefsUtil.getPrefs().edit().putInt(NIGHT_SP_KEY, type).commit();
    }

    public static int getNightType() {
        return SharedPrefsUtil.getInt(NIGHT_SP_KEY, TYPE_SYSTEM);
    }

    public static int getUiNightMode() {
        int uiMode;
        Context context = UtilsContextManager.getInstance().getApplication();
        if (context == null || context.getResources() == null) {
            uiMode = Configuration.UI_MODE_NIGHT_NO;
        } else {
            int MAIN_ACTIVITY_UIMODE = context.getResources().getConfiguration().uiMode;
            uiMode = MAIN_ACTIVITY_UIMODE & Configuration.UI_MODE_NIGHT_MASK;
        }
        return uiMode;
    }

    public static boolean isNightMode() {
        return getUiNightMode() == Configuration.UI_MODE_NIGHT_YES;
    }

    public static String getNightTypeString() {
        int type = getNightType();
        if (type == TYPE_NIGHT) {
            return ResourceUtils.getResStringFromId(R.string.base_night_night);
        } else if (type == TYPE_NORMAL) {
            return ResourceUtils.getResStringFromId(R.string.base_night_normal);
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                return ResourceUtils.getResStringFromId(R.string.base_follow_system);
            } else {
                return ResourceUtils.getResStringFromId(R.string.base_night_normal);
            }
        }
    }

    public static Locale getLanguageMode() {
        String country = SharedPrefsUtil.getString("language", "");
        if (TextUtils.isEmpty(country)) {
            return Locale.getDefault();
        }
        if (TextUtils.equals(country, LANGUAGE_TW)) {
            return Locale.TRADITIONAL_CHINESE;
        }
        return Locale.SIMPLIFIED_CHINESE;
    }

    public boolean isUseDefault() {
        return TextUtils.isEmpty(SharedPrefsUtil.getString("language", ""));
    }

    public void saveLanguage(String language) {
        SharedPrefsUtil.saveString("language", language);
    }

    public void toggleLanguage() {
        String country = SharedPrefsUtil.getString("language", "");
        if (TextUtils.equals(country, LANGUAGE_TW)) {
            saveLanguage(Locale.SIMPLIFIED_CHINESE.toString());
        } else {
            saveLanguage(Locale.TRADITIONAL_CHINESE.toString());
        }
    }

    public void updateLanguage() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        AppManager.getAppManager().recreateAllActivity();
    }
}
