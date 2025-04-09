package com.wallstreetcn.helper.utils.text.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.LineBackgroundSpan;

/**
 * Created by  Leif Zhang on 2018/3/14.
 * Email leifzhanggithub@gmail.com
 */

public class BackgroundColorSpanWithPaddingAndLineSpacing implements LineBackgroundSpan {
    private float roundedCornerSize;
    private int backgroundColor;
    private int paddingSize;
    private RectF rect;

    public BackgroundColorSpanWithPaddingAndLineSpacing(int backgroundColor, int paddingSize, float roundedCornerSize) {
        super();
        this.backgroundColor = backgroundColor;
        this.paddingSize = paddingSize;
        this.roundedCornerSize = roundedCornerSize;
        this.rect = new RectF();
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int currentLineNumber) {
        final int textWidth = Math.round(p.measureText(text, start, end));
        final int paintColor = p.getColor();

        rect.set(left - paddingSize / 2, top - paddingSize / 4, left + textWidth + paddingSize / 2, top + p.getTextSize() + paddingSize / 2);
        p.setColor(backgroundColor);
        c.drawRoundRect(rect, roundedCornerSize, roundedCornerSize, p);
        p.setColor(paintColor);
    }
}
