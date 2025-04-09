package com.wallstreetcn.helper.utils.text.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by richzjc on 18/3/5.
 */

public class ItalicSpan extends ReplacementSpan{

    private int mWidth;

    public ItalicSpan(){

    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        mWidth = (int) paint.measureText(text, start, end);
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setTextSkewX(-0.25f);
        canvas.drawText(text, start, end, x, y, paint);
    }
}
