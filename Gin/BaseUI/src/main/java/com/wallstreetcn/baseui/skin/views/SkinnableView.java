package com.wallstreetcn.baseui.skin.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.skin.core.ViewsMatch;
import com.wallstreetcn.baseui.skin.model.AttrsBean;

public class SkinnableView extends View implements ViewsMatch {

    private AttrsBean attrsBean;

    public SkinnableView(Context context) {
        this(context, null);
    }

    public SkinnableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        attrsBean = new AttrsBean();

        // 根据自定义属性，匹配控件属性的类型集合，如：background
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.all_view,
                defStyleAttr, 0);
        // 存储到临时JavaBean对象
        attrsBean.saveViewResource(typedArray, R.styleable.all_view);
        // 这一句回收非常重要！obtainStyledAttributes()有语法提示！！
        typedArray.recycle();
    }

    @Override
    public void skinnableView() {
        // 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.all_view[R.styleable.all_view_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = attrsBean.getViewResource(key);
        if (backgroundResourceId > 0) {
            // 兼容包转换
            Drawable drawable = ContextCompat.getDrawable(getContext(), backgroundResourceId);
            setBackground(drawable);
        }
    }
}
