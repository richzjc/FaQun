package com.wallstreetcn.helper.utils.text.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;

import com.wallstreetcn.helper.utils.system.ScreenUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Created by Leif Zhang on 2017/1/16.
 * Email leifzhanggithub@gmail.com
 */

public class BackgroundSpan extends ReplacementSpan {

    private Drawable drawable;
    private int textColor;

    public BackgroundSpan(Context context, int drawableId) {
        this(ContextCompat.getDrawable(context, drawableId), Integer.MAX_VALUE);
    }

    public BackgroundSpan(Context context, int drawableId, int textColor) {
        this(ContextCompat.getDrawable(context, drawableId), textColor);
    }

    public BackgroundSpan(Drawable drawable, int textColor) {
        super();
        this.drawable = drawable;
        this.textColor = textColor;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        if (textColor != Integer.MAX_VALUE) {
            paint.setColor(textColor);
        }
        int newTop = (int) ((top + bottom) / 2 - paint.getTextSize() / 2 - ScreenUtils.dip2px(2));
        Rect rect = new Rect((int) x, newTop, (int) (x + measureText(paint, text, start, end)), bottom);
        drawable.setBounds(rect);
        drawable.draw(canvas);
        canvas.drawText(text, start, end, x, y, paint);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}
