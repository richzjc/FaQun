package com.wallstreetcn.baseui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.wallstreetcn.baseui.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by  Leif Zhang on 2018/3/16.
 * Email leifzhanggithub@gmail.com
 */

public class RecyclerViewLayout extends RelativeLayout {
    public RecyclerViewLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RecyclerViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(ContextCompat.getColor(context,com.wallstreetcn.baseui.R.color.day_mode_background_color1_ffffff));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}
