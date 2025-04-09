package com.wallstreetcn.helper.utils.text.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.QuoteSpan;

import com.wallstreetcn.helper.utils.system.ScreenUtils;

import androidx.annotation.ColorInt;

/**
 * Created by richzjc on 18/2/26.
 */

public class BlockQuoteSpan extends QuoteSpan {

    Paint paint;

    public BlockQuoteSpan() {
        super();
    }

    public BlockQuoteSpan(int color) {
        super();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    public int getLeadingMargin(boolean first) {
        return ScreenUtils.dip2px(2) + ScreenUtils.dip2px(10);
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout layout) {
        if (paint != null) {
            c.drawRect(x, top, x + dir * ScreenUtils.dip2px(2), bottom, paint);
        } else {
            Paint.Style style = p.getStyle();
            int color = p.getColor();

            p.setStyle(Paint.Style.FILL);
            p.setColor(color);

            c.drawRect(x, top, x + dir * ScreenUtils.dip2px(2), bottom, p);

            p.setStyle(style);
            p.setColor(color);
        }
    }
}
