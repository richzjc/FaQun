package com.wallstreetcn.baseui.widget.expand;

import android.content.Context;
import androidx.core.content.ContextCompat;

import android.graphics.text.LineBreaker;
import android.util.AttributeSet;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.widget.OpenCloseTextView;
import com.wallstreetcn.helper.utils.data.TraceUtils;

/**
 * Created by wscn on 17/2/27.
 */

public class ExpandTextView extends OpenCloseTextView {

    private int maxLine = 5;
    private IExpand iExpand;

    public ExpandTextView(Context context) {
        super(context);
        init(context);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mToExpandHintColor = ContextCompat.getColor(context,com.wallstreetcn.baseui.R.color.day_mode_theme_color_1478f0);
        mToShrinkHintColor = ContextCompat.getColor(context,com.wallstreetcn.baseui.R.color.day_mode_theme_color_1478f0);
        mMaxLinesOnShrink = maxLine;
        setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_CHARACTER);
    }

    @Override
    public void toggle() {
        int state = getExpandState();
        switch (state) {
            case STATE_SHRINK:
                if (iExpand != null)
                    setText(iExpand.getHtml(), BufferType.NORMAL);
                break;
            case STATE_EXPAND:
                if (iExpand != null) {
                    setText(iExpand.getHtmlWithoutP(), BufferType.NORMAL);
                    TraceUtils.onEvent(getContext(), "live_feed_collapse_click");
                }
                break;
        }
        super.toggle();
        if(iExpand != null)
            iExpand.setExpand(getExpandState() == STATE_EXPAND);
    }

    public void bindExpand(IExpand expand) {
        this.iExpand = expand;
        int state;
        if(iExpand.isExpand())
            state = STATE_EXPAND;
        else
            state = STATE_SHRINK;

        if(state == getExpandState()) {
            switch (state) {
                case STATE_SHRINK:
                    if (iExpand != null)
                        setText(iExpand.getHtmlWithoutP(), BufferType.NORMAL);
                    break;
                case STATE_EXPAND:
                    if (iExpand != null)
                        setText(iExpand.getHtml(), BufferType.NORMAL);
                    break;
            }
        }else{
            toggle();
        }
    }
}
