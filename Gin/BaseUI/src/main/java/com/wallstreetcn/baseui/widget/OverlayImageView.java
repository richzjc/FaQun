package com.wallstreetcn.baseui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.imageloader.WscnImageView;

/**
 * Created by Leif Zhang on 2016/11/1.
 * Email leifzhanggithub@gmail.com
 */
public class OverlayImageView extends WscnImageView {

    public OverlayImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(attrs);
    }

    public OverlayImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(attrs);
    }

    public OverlayImageView(Context context) {
        super(context);
        initPaint(null);
    }

    private void initPaint(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs,
                R.styleable.OverlayImageView, 0, 0);
        if (arr != null) {
            color = arr.getColor(R.styleable.OverlayImageView_overlayColor, Color.BLACK);
            alpha = arr.getInt(R.styleable.OverlayImageView_overlayAlpha, 25);
            int newColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
            ColorDrawable colorDrawable = new ColorDrawable(newColor);
            getHierarchy().setOverlayImage(colorDrawable);
            arr.recycle();
        }
        getHierarchy().setBackgroundImage(new ColorDrawable(ContextCompat.getColor(getContext(),com.wallstreetcn.baseui.R.color.day_mode_divide_line_color_e6e6e6)));
    }

    private int alpha, color;

    public void setPaintColor(int color) {
        this.color = color;
        int newColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
        ColorDrawable colorDrawable = new ColorDrawable(newColor);
        getHierarchy().setOverlayImage(colorDrawable);
    }

    public void setPaintAlpha(int alpha) {
        this.alpha = alpha;
        int newColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
        ColorDrawable colorDrawable = new ColorDrawable(newColor);
        getHierarchy().setOverlayImage(colorDrawable);
    }
}
