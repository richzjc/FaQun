package com.wallstreetcn.baseui.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.wallstreetcn.baseui.R;

public class IconFontSpan extends ReplacementSpan {

    private TextPaint mTextPaint;

    public IconFontSpan(Context context, Builder builder) {
        if (builder.textSize <= 0) {
            throw new IllegalArgumentException("textSize must > 0");
        }
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(builder.textSize);
        mTextPaint.setColor(builder.textColor);

        Typeface typeface = builder.typeface;
        if (typeface != null) {
            mTextPaint.setTypeface(typeface);
        } else {
            typeface = ResourcesCompat.getFont(context, com.wallstreetcn.baseui.R.font.iconfont);
            if (typeface != null) {
                mTextPaint.setTypeface(typeface);
            }
        }

    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return (int) mTextPaint.measureText(text, start, end);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        text = text.subSequence(start, end);
        Paint p = mTextPaint;
        Paint.FontMetricsInt fm = p.getFontMetricsInt();
        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) / 2f - (bottom + top) / 2f), p);    //此处重新计算y坐标，使字体居中
    }

    public static class Builder {
        //文字大小
        private float textSize;
        //文字颜色
        private int textColor;

        private Typeface typeface;

        public Builder setTextSize(float size) {
            this.textSize = size;
            return this;
        }

        public Builder setTextSize(int size) {
            this.textSize = size;
            return this;
        }

        public Builder setTextColor(int color) {
            this.textColor = color;
            return this;
        }

        public Builder setTypeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        public IconFontSpan build(Context context) {
            return new IconFontSpan(context, this);
        }

    }

}
