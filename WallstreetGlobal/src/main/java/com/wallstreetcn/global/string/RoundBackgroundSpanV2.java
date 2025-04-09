package com.wallstreetcn.global.string;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.style.LineHeightSpan;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Create by lisao
 * <p>
 * 鉴于 RoundBackgroundColorSpan bug太多了，代码太乱了 重写一个功能更强大的
 * <p>
 * 新特性
 * 可以自由设置文字padding
 * 可以自由设置span end margin
 * 可以单独设置边框
 * 可以设置边框大小
 * <p>
 * 重新修正了 RoundBackgroundColorSpan 测量逻辑
 * 重新修正了 RoundBackgroundColorSpan 绘画逻辑
 * 重新修正了 RoundBackgroundColorSpan 不同文字大小时显示bug
 */
public class RoundBackgroundSpanV2 extends ReplacementSpan implements LineHeightSpan {

    private Paint mTextPaint;

    private Paint mStrokePaint;

    private Paint mFillPaint;

    private int mLineHeight = 0;//行高

    private int mMeasureSize = 0;//测量出的占位大小

    private float mRadius = 0;

    private float mPaddingHorizontal = 0;//横向的padding

    private float mSpanEndMargin = 0;

    private float mPaddingVertical = 0;

    private RoundBackgroundSpanV2() {

    }

    public RoundBackgroundSpanV2(Builder builder) {
        if (builder.textSize <= 0) {
            throw new IllegalArgumentException("textSize must > 0");
        }

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(builder.textSize);
        mTextPaint.setColor(builder.textColor);

        if (builder.strokeWidth > 0) {
            mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mStrokePaint.setStrokeWidth(builder.strokeWidth);
            mStrokePaint.setColor(builder.strokeColor);
            mStrokePaint.setStyle(Paint.Style.STROKE);
        }
        //填充颜色
        if (builder.fillcolor != 0) {
            mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mFillPaint.setStyle(Paint.Style.FILL);
            mFillPaint.setColor(builder.fillcolor);
        }
        //4分之一文字左右大小
        if (builder.paddingHorizontal == -1) {
            mPaddingHorizontal = mTextPaint.getTextSize() / 3f;
        } else {
            mPaddingHorizontal = builder.paddingHorizontal;
        }
        if (builder.spanEndMargin == -1) {
            mSpanEndMargin = mTextPaint.getTextSize() / 4f;
        } else {
            mSpanEndMargin = builder.spanEndMargin;
        }
        if (builder.paddingVertical == -1) {
            mPaddingVertical = mTextPaint.getTextSize() / 3f;
        } else {
            mPaddingVertical = builder.paddingVertical;
        }

        this.mRadius = builder.radius;

        Typeface typeface = builder.typeface;
        if (typeface != null) {
            mTextPaint.setTypeface(typeface);
        }
    }

    @Override
    public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int lineHeight, Paint.FontMetricsInt fm) {
        mLineHeight = fm.bottom - fm.top;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        mMeasureSize = (int) (mTextPaint.measureText(text, start, end) + 2 * mPaddingHorizontal);
        return (int) (mMeasureSize + mSpanEndMargin);
    }


    Rect rectBounds = new Rect();


    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        //测量背景占位大小

        float t = y + paint.getFontMetrics().ascent;
        float b = y + paint.getFontMetrics().descent;
        float centerY = (t + b) / 2f;
        mTextPaint.getTextBounds(text.toString(), 0, text.length(), rectBounds);
        float tt = centerY - rectBounds.height() / 2f - mPaddingVertical;
        float bb = centerY + rectBounds.height() / 2f + mPaddingVertical;

        //RectF r = new RectF(x, y + paint.getFontMetrics().ascent, x + mMeasureSize, y + paint.getFontMetrics().descent);
        RectF r = new RectF(x, tt, x + mMeasureSize, bb);
        //画背景色
        if (mFillPaint != null) {
            canvas.drawRoundRect(r, mRadius, mRadius, mFillPaint);
        }
        //画边框
        if (mStrokePaint != null) {
            int rl = (int) (r.left + mStrokePaint.getStrokeWidth() / 2f);
            int rt = (int) (r.top + mStrokePaint.getStrokeWidth() / 2f);
            int rr = (int) (r.right - mStrokePaint.getStrokeWidth() / 2f);
            int rb = (int) (r.bottom - mStrokePaint.getStrokeWidth() / 2f);
            canvas.drawRoundRect(rl, rt, rr, rb, mRadius, mRadius, mStrokePaint);
        }
        //画文字
        //1.确定基线位置
        //2.确定开始位置
        //float baseline = y - (r.height() - (mTextPaint.getFontMetrics().bottom - mTextPaint.getFontMetrics().top)) / 2 + (paint.descent() - mTextPaint.descent());
        float yyyy = (r.top + r.bottom) / 2f - (mTextPaint.getFontMetrics().descent + (mTextPaint.getFontMetrics().ascent)) / 2f;
        float baseStart = x + mPaddingHorizontal;
        canvas.drawText(text, start, end, baseStart, yyyy, mTextPaint);
    }


    public static class Builder {
        //文字大小
        private float textSize;
        //文字颜色
        private int textColor;
        //边框颜色
        private int strokeColor;
        //边框宽度
        private int strokeWidth;
        //填充颜色
        private int fillcolor;
        //半径
        private float radius;
        //文字横向padding
        private float paddingHorizontal = -1;
        //span结尾margin
        private float spanEndMargin = -1;
        //背景矩形垂直内边距
        private float paddingVertical = -1;

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

        public Builder setStrokeColor(int color) {
            this.strokeColor = color;
            return this;
        }

        public Builder setStrokeWidth(int width) {
            this.strokeWidth = width;
            return this;
        }

        public Builder setStrokeWidth(float width) {
            this.strokeWidth = (int) width;
            return this;
        }

        public Builder setFillColor(int color) {
            this.fillcolor = color;
            return this;
        }

        public Builder setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setPaddingHorizontal(int padding) {
            this.paddingHorizontal = padding;
            return this;
        }

        public Builder setPaddingHorizontal(float padding) {
            this.paddingHorizontal = padding;
            return this;
        }

        public Builder setMarginEnd(int margin) {
            this.spanEndMargin = margin;
            return this;
        }

        public Builder setMarginEnd(float margin) {
            this.spanEndMargin = margin;
            return this;
        }

        public Builder setPaddingVertical(float padding) {
            this.paddingVertical = padding;
            return this;
        }

        public Builder setPaddingVertical(int padding) {
            this.paddingVertical = padding;
            return this;
        }

        public Builder setTypeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        public RoundBackgroundSpanV2 build() {
            return new RoundBackgroundSpanV2(this);
        }

    }
}
