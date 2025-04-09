package com.wallstreetcn.helper.utils.text.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import com.wallstreetcn.helper.utils.system.ScreenUtils;

/**
 * Created by  Leif Zhang on 2017/7/26.
 * Email leifzhanggithub@gmail.com
 */

public class RoundedBackgroundSpan extends ReplacementSpan {

    private int CORNER_RADIUS = 0;
    private int backgroundColor = 0;
    private int textColor = 0;
    private int space = 0;
    public RoundedBackgroundSpan(int backgroundColor) {
        this(backgroundColor, Color.WHITE, ScreenUtils.dip2px(2));
    }

    public RoundedBackgroundSpan(int backgroundColor, int textColor) {
        this(backgroundColor, textColor, ScreenUtils.dip2px(2));
    }

    public RoundedBackgroundSpan(int backgroundColor, int textColor, int radius) {
        super();
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.CORNER_RADIUS = radius;
        space = radius * 2;
    }


    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int newX = (int) (x + space);
        TextPaint textPaint = getCustomTextPaint(paint);
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        int paddTop = ScreenUtils.dip2px(2F);
        RectF rect = new RectF(x, top + paddTop, x + measureText(textPaint, text, start, end), bottom - paddTop);
        textPaint.setColor(backgroundColor);
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, textPaint);
        textPaint.setColor(textColor);
        canvas.drawText(text, start, end, newX, y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2), textPaint);    //此处重新计算y坐标，使字体居中
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        return Math.round(measureText(p, text, 0, text.length()));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end) + space * 2;
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint paint = new TextPaint(srcPaint);
        paint.setTextSize(srcPaint.getTextSize() * 0.8f);   //设定字体大小, sp转换为px
        return paint;
    }
}