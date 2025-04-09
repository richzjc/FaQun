package com.wallstreetcn.baseui.skin.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.wallstreetcn.baseui.skin.views.SkinnableLinearLayout;
import com.wallstreetcn.baseui.skin.views.SkinnableTitleBar;
import com.wallstreetcn.baseui.skin.views.SkinnableView;
import java.lang.reflect.Constructor;

/**
 * 自定义控件加载器（可以考虑该类不被继承）
 */
public final class CustomAppCompatViewInflater {

    private String name; // 控件名
    private Context context; // 上下文
    private AttributeSet attrs; // 某控件对应所有属性

    public CustomAppCompatViewInflater(Context context) {
        this.context = context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttrs(AttributeSet attrs) {
        this.attrs = attrs;
    }

    /**
     * @return 自动匹配控件名，并初始化控件对象
     */
    public View autoMatch() {
        View view = null;
        switch (name) {
            case "LinearLayout":
                view = new SkinnableLinearLayout(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "View":
                view = new SkinnableView(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "com.wallstreetcn.baseui.widget.TitleBar":
                view = new SkinnableTitleBar(context, attrs);
                this.verifyNotNull(view, name);
                break;

            case "com.wallstreetcn.global.customView.SettingItemView":
                view = createSettingItemView(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "com.rm.rmswitch.RMSwitch":
                view = createSwitherView(context, attrs);
                this.verifyNotNull(view, name);
                break;
        }

        return view;
    }

    private View createSettingItemView(Context context, AttributeSet attrs) {
        View view = null;
        try {
            Class cls = Class.forName("com.wallstreetcn.global.widget.SkinnableSettingItemView");
            Constructor constructor = cls.getConstructor(Context.class, AttributeSet.class);
            view  = (View) constructor.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private View createSwitherView(Context context, AttributeSet attrs) {
        View view = null;
        try {
            Class cls = Class.forName("com.wallstreetcn.global.widget.SkinnableRmSwitcherView");
            Constructor constructor = cls.getConstructor(Context.class, AttributeSet.class);
            view  = (View) constructor.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 校验控件不为空（源码方法，由于private修饰，只能复制过来了。为了代码健壮，可有可无）
     *
     * @param view 被校验控件，如：AppCompatTextView extends TextView（v7兼容包，兼容是重点！！！）
     * @param name 控件名，如："ImageView"
     */
    private void verifyNotNull(View view, String name) {
        if (view == null) {
            throw new IllegalStateException(this.getClass().getName() + " asked to inflate view for <" + name + ">, but returned null");
        }
    }
}
