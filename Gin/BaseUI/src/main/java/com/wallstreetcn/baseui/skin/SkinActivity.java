package com.wallstreetcn.baseui.skin;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.skin.core.CustomAppCompatViewInflater;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.system.WindowHelper;
import com.yangl.swipeback.ui.HorizontalSwipeBackActivity;

/**
 * 换肤Activity父类
 * <p>
 * 用法：
 * 1、继承此类
 * 2、重写openChangeSkin()方法
 */
public class SkinActivity extends HorizontalSwipeBackActivity {

    private CustomAppCompatViewInflater viewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (openChangeSkin() && !isRecreate()) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            LayoutInflaterCompat.setFactory2(layoutInflater, this);
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (openChangeSkin() && !isRecreate()) {
            if (viewInflater == null) {
                viewInflater = new CustomAppCompatViewInflater(context);
            }
            viewInflater.setName(name);
            viewInflater.setAttrs(attrs);
            return viewInflater.autoMatch();
        }
        return super.onCreateView(parent, name, context, attrs);
    }


    protected boolean needCheckLightStatusBar() {
        return true;
    }

    /**
     * @return 是否开启换肤，增加此开关是为了避免开发者误继承此父类，导致未知bug
     */
    protected boolean openChangeSkin() {
        return true;
    }

    /**
     * 切换夜间模式 当返回为true,
     * 表示通过调用activity的recreate方法来实现
     * 当返回为false的时候 默认通过自己实现的changeSkin 来实现
     *
     * @return
     */
    protected boolean isRecreate() {
        return true;
    }

    protected void checkLightStatusBar() {
        boolean isStatusBarLight = getResources().getBoolean(R.bool.statusBarTextColor);
        WindowHelper.toggleLightStatusBar(this, isStatusBarLight);

    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1f;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    public Resources getFontScaleResource() {
        Resources originResource = getResources();
        Configuration config = new Configuration();
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setTo(originResource.getDisplayMetrics());
        Resources resources = new Resources(getAssets(), metrics, config);
        int fontIndex = Util.getFontIndexValue();
        Configuration configuration = resources.getConfiguration();
        if (fontIndex == 1)
            configuration.fontScale = 0.875f;
        else if (fontIndex == 3)
            configuration.fontScale = 1.125f;
        else if (fontIndex == 4)
            configuration.fontScale = 1.25f;
        else if (fontIndex == 5)
            configuration.fontScale = 1.375f;
        else if (fontIndex == 6)
            configuration.fontScale = 1.5f;
        else
            configuration.fontScale = 1f;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    protected void forNavigation() {
        TLog.e("navigation", getClass().getSimpleName());
        int color = ContextCompat.getColor(this, getNavBarColor());
        getWindow().setNavigationBarColor(color);
    }

    @ColorRes
    public int getNavBarColor() {
        return R.color.navbar_color;
    }
}
