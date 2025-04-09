package com.wallstreetcn.baseui.widget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

public class MarqueeTextView extends AppCompatTextView {

    public MarqueeTextView(Context context) {
        this(context, true);
    }

    public MarqueeTextView(Context context, boolean isMarquee) {
        super(context);
        init(isMarquee);

    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(true);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(true);
    }

    private boolean isMarquee;
    private void init(boolean isMarquee) {
        this.isMarquee = isMarquee;
        if (isMarquee) {
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
            setMarqueeRepeatLimit(-1);
            setTextSize(16);
        } else {
            setEllipsize(TextUtils.TruncateAt.END);
            setTextSize(14);
        }
        setMaxLines(1);
        setSingleLine();
        setFocusableInTouchMode(true);
        setFocusable(true);
    }


    @Override
    public boolean isFocused() {
        return isMarquee;
    }
}