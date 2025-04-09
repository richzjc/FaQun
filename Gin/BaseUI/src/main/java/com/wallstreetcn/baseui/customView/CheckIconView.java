package com.wallstreetcn.baseui.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.wallstreetcn.baseui.R;


/**
 * Created by chanlevel on 2016/11/29.
 */

public class CheckIconView extends IconView implements Checkable ,View.OnClickListener{
    private static final int DEFAULT_COLOR= Color.WHITE;

    public CheckIconView(Context context) {
        super(context);
    }

    public CheckIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CheckIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private String checkedTxt, unCheckedTxt;
    private int colorChecked, colorUnChecked;


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckIconView);
        checkedTxt = a.getString(R.styleable.CheckIconView_checkedTxt);
        unCheckedTxt = a.getString(R.styleable.CheckIconView_uncheckedTxt);
        colorChecked = a.getColor(R.styleable.CheckIconView_colorChecked, DEFAULT_COLOR);
        colorUnChecked = a.getColor(R.styleable.CheckIconView_colorUnChecked,DEFAULT_COLOR);
        isChecked = a.getBoolean(R.styleable.CheckIconView_checked, false);
        a.recycle();
        setChecked(isChecked);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    private boolean isChecked;

    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }


    public void setChecked(boolean checked,boolean notify) {
        this.isChecked = checked;
        if(notify&&listener!=null)listener.onCheckedChanged(this,checked);
        setTextColor(isChecked ? colorChecked : colorUnChecked);

        if (isChecked) {
            if (null != checkedTxt) setText(checkedTxt);
        } else if (null != unCheckedTxt) setText(unCheckedTxt);

    }

    public void setOnCheckChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    OnCheckedChangeListener listener;

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }


    public   interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The   view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(View buttonView, boolean isChecked);
    }
}
