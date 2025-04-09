package com.wallstreetcn.baseui.customView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.wallstreetcn.helper.utils.system.ScreenUtils;

/**
 * Created by micker on 16/6/22.
 */
public class EditTextWithDelete extends EditText {

    private Drawable mRightDrable;
    private boolean isHasFocus;
    private ConfigData listener;

    public EditTextWithDelete(Context context) {
        super(context);
        init();
    }

    public EditTextWithDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable[] drawables = getCompoundDrawables();
        //取得editText四周的right的图片
        mRightDrable = drawables[2];
        mRightDrable.setBounds(0, 0, ScreenUtils.dip2px(15), ScreenUtils.dip2px(15));
        this.setOnFocusChangeListener(new FocusChangeListenerImpl());
        //   this.addTextChangedListener(new TextWatcherIpl());
        setDrawableIcon(false);
    }

    //应藏或者显示clean图标
    public void setDrawableIcon(boolean isvisiable) {
        Drawable drawable;
        if (isvisiable) {
            drawable = mRightDrable;
        } else {
            drawable = null;
        }
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], drawable, getCompoundDrawables()[3]);
    }

    public void setListener(ConfigData listener) {
        this.listener = listener;
    }


    public interface ConfigData {
        public void showSearchFragment();
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

    protected class TextWathcerIpl implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            boolean isVisiable = getText().toString().length() >= 1;
            setDrawableIcon(isVisiable);
        }
    }
}
