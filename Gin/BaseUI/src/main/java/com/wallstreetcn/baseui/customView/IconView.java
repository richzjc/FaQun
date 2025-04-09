package com.wallstreetcn.baseui.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.widget.CustomTextView;

/**
 * Created by micker on 16/6/16.
 */
public class IconView extends CustomTextView {
    private Typeface customTypeFace;

    public IconView(Context context) {
        super(context);
        initTypeFace();
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeFace();
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace();
    }

    protected void initTypeFace() {
        Typeface typeFace = ResourcesCompat.getFont(getContext(), com.wallstreetcn.baseui.R.font.iconfont);
        setCustomTypeFace(typeFace);
    }

    public void setCustomTypeFace(Typeface customTypeFace) {
        this.customTypeFace = customTypeFace;
        if (null != customTypeFace)
            setTypeface(this.customTypeFace);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
