package com.wallstreetcn.baseui.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.core.content.res.ResourcesCompat;

import com.wallstreetcn.baseui.R;

/**
 * Created by wscn on 16/12/19.
 */

public class CheckTVIconView extends AppCompatCheckedTextView {

    private Typeface customTypeFace;

    public CheckTVIconView(Context context) {
        super(context);
        initTypeFace();
    }

    public CheckTVIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeFace();
    }

    public CheckTVIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace();
    }

    private void initTypeFace() {
        //Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "wscniconfont/iconfont.ttf");
        Typeface typeFace = ResourcesCompat.getFont(getContext(), com.wallstreetcn.baseui.R.font.iconfont);
        setCustomTypeFace(typeFace);
    }

    public void setCustomTypeFace(Typeface customTypeFace) {
        this.customTypeFace = customTypeFace;
        if (null != customTypeFace)
            setTypeface(this.customTypeFace);
    }
}
