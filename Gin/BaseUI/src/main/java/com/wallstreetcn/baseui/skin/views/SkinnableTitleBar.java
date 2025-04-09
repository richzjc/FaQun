package com.wallstreetcn.baseui.skin.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.skin.core.ViewsMatch;
import com.wallstreetcn.baseui.skin.model.AttrsBean;
import com.wallstreetcn.baseui.widget.TitleBar;

public class SkinnableTitleBar extends TitleBar implements ViewsMatch {

    private AttrsBean attrsBean;

    public SkinnableTitleBar(Context context) {
        this(context, null);
    }

    public SkinnableTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        attrsBean = new AttrsBean();

        // 根据自定义属性，匹配控件属性的类型集合，如：background
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.skin_titlebar,
                defStyleAttr, 0);
        // 存储到临时JavaBean对象
        attrsBean.saveViewResource(typedArray, R.styleable.skin_titlebar);
        // 这一句回收非常重要！obtainStyledAttributes()有语法提示！！
        typedArray.recycle();
    }

    @Override
    public void skinnableView() {
        // 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.skin_titlebar[R.styleable.skin_titlebar_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = attrsBean.getViewResource(key);
        if (backgroundResourceId > 0) {
            // 兼容包转换
            Drawable drawable = ContextCompat.getDrawable(getContext(), backgroundResourceId);
            setBackground(drawable);
        }else{
            int resourId = getResources().getIdentifier("day_mode_toolbar_color", "color", getContext().getPackageName());
            if(resourId > 0){
                Drawable color = ContextCompat.getDrawable(getContext(), resourId);
                setBackground(color);
            }
        }

        key = R.styleable.skin_titlebar[R.styleable.skin_titlebar_titleTextColor];
        // 根据styleable获取控件某属性的resourceId
        int titleTextColor = attrsBean.getViewResource(key);
        if (titleTextColor > 0) {
            // 兼容包转换
            int color = ContextCompat.getColor(getContext(), titleTextColor);
            setTitleColor(color);
        }else{
            int resourId = getResources().getIdentifier("day_mode_text_dark_light_color", "color", getContext().getPackageName());
            if(resourId > 0){
                int color = ContextCompat.getColor(getContext(), resourId);
                setTitleColor(color);
            }
        }

        key = R.styleable.skin_titlebar[R.styleable.skin_titlebar_iconBackColor];
        // 根据styleable获取控件某属性的resourceId
        int iconBackColor = attrsBean.getViewResource(key);
        if (iconBackColor > 0) {
            // 兼容包转换
            int color = ContextCompat.getColor(getContext(), iconBackColor);
            setIconBackColor(color);
        }else{
            int resourId = getResources().getIdentifier("day_mode_text_color1_333333", "color", getContext().getPackageName());
            if(resourId > 0){
                int color = ContextCompat.getColor(getContext(), resourId);
                setIconBackColor(color);
            }
        }

        key = R.styleable.skin_titlebar[R.styleable.skin_titlebar_rightBtn1Color];
        // 根据styleable获取控件某属性的resourceId
        int right1Color = attrsBean.getViewResource(key);
        if (right1Color > 0) {
            // 兼容包转换
            int color = ContextCompat.getColor(getContext(), right1Color);
            setRightBtn1TextColor(color);
        }else{
            int resourId = getResources().getIdentifier("day_mode_text_color1_333333", "color", getContext().getPackageName());
            if(resourId > 0){
                int color = ContextCompat.getColor(getContext(), resourId);
                setRightBtn1TextColor(color);
            }
        }

        key = R.styleable.skin_titlebar[R.styleable.skin_titlebar_rightBtn2Color];
        // 根据styleable获取控件某属性的resourceId
        int right2Color = attrsBean.getViewResource(key);
        if (right2Color > 0) {
            // 兼容包转换
            int color = ContextCompat.getColor(getContext(), right2Color);
            setRightBtn2Color(color);
        }else{
            int resourId = getResources().getIdentifier("day_mode_text_color1_333333", "color", getContext().getPackageName());
            if(resourId > 0){
                int color = ContextCompat.getColor(getContext(), resourId);
                setRightBtn2Color(color);
            }
        }
    }
}
