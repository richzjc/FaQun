package com.wallstreetcn.baseui.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.wallstreetcn.helper.utils.system.ScreenUtils;

public class EditTextWithDel extends AutoCompleteTextView {

    private Drawable mRightDrawable;
    private boolean isHasFocus;


    public EditTextWithDel(Context context) {
        super(context);
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        Drawable[] drawables = getCompoundDrawables();
        //取得editText四周的right的图片
        mRightDrawable = drawables[2];
        mRightDrawable.setBounds(0, 0, ScreenUtils.dip2px(15), ScreenUtils.dip2px(15));
        this.setOnFocusChangeListener(new FocusChangeListenerImpl());
        setDrawableIcon(false);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()) && getX() < (getWidth() - getPaddingRight()));
                    if (isClean)
                        setText("");
                }
                return false;
            }
        });
    }

    protected class FocusChangeListenerImpl implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean b) {
            isHasFocus = b;
            if (isHasFocus) {
                boolean isVisible = getText().toString().length() >= 1;
                setDrawableIcon(isVisible);
            } else {
                setDrawableIcon(false);
            }

        }
    }

    public static class TextWatcherIpl implements TextWatcher {
        private EditTextWithDel editTextWithDel;

        public TextWatcherIpl(EditTextWithDel editTextWithDel) {
            this.editTextWithDel = editTextWithDel;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            boolean isVisible = editTextWithDel.getText().toString().length() >= 1;
            editTextWithDel.setDrawableIcon(isVisible);
        }
    }


    public void setDrawableIcon(boolean isVisible) {
        Drawable drawable;
        if (isVisible) {
            drawable = mRightDrawable;
        } else {
            drawable = null;
        }
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], drawable, getCompoundDrawables()[3]);

    }
}
