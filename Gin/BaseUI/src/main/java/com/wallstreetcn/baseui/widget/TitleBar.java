package com.wallstreetcn.baseui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.customView.CustomToolBar;
import com.wallstreetcn.baseui.customView.IconView;
import com.wallstreetcn.baseui.widget.expand.LinkMovementMethod;
import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

/**
 * Created by zhangjianchuan on 2016/9/14.
 */
public class TitleBar extends RelativeLayout {

    CustomToolBar toolBar;
    IconView iconBack;
    RelativeLayout toolbarLayout;
    IconView rightBtn1;
    IconView rightBtn2;
    public LinearLayout btnParent;
    Context context;
    private OnClickListener titleClickListener;


    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.base_action_bar_view, this, true);
        initWidget(context);
        initListener(context);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.actionbar);
        String title = array.getString(R.styleable.actionbar_titleText);
        if (title != null) {
            toolBar.setTitle(title);
        }
        if (getBackground() == null) {
            setBackgroundColor(ContextCompat.getColor(context,com.wallstreetcn.baseui.R.color.day_mode_background_color1_ffffff));
        }
        boolean isMarquee = array.getBoolean(R.styleable.actionbar_isMarquee, true);
        toolBar.setMarquee(isMarquee);
        int defaultColor = ContextCompat.getColor(getContext(),com.wallstreetcn.baseui.R.color.day_mode_text_color1_333333);
        int color = array.getColor(R.styleable.actionbar_titleTextColor,
                ContextCompat.getColor(context,com.wallstreetcn.baseui.R.color.day_mode_text_dark_light_color));
        toolBar.setTitleTextColor(color);
        int backColor = array.getColor(R.styleable.actionbar_iconBackColor, defaultColor);
        iconBack.setTextColor(backColor);
        boolean iconBackVisible = array.getBoolean(R.styleable.actionbar_iconBackVisible, true);
        if (iconBackVisible) {
            iconBack.setVisibility(View.VISIBLE);
        } else {
            iconBack.setVisibility(View.GONE);
        }
        String backText = array.getString(R.styleable.actionbar_iconBackText);
        if (!TextUtils.isEmpty(backText)) {
            iconBack.setText(backText);
        }
        String rightBtn1Text = array.getString(R.styleable.actionbar_rightBtn1Text);
        if (!TextUtils.isEmpty(rightBtn1Text)) {
            rightBtn1.setText(rightBtn1Text);
        }
        String rightBtn2Text = array.getString(R.styleable.actionbar_rightBtn2Text);
        if (!TextUtils.isEmpty(rightBtn2Text)) {
            rightBtn2.setText(rightBtn2Text);
        }
        int rightBtn1Color = array.getColor(R.styleable.actionbar_rightBtn1Color, defaultColor);
        rightBtn1.setTextColor(rightBtn1Color);

        int rightBtn2Color = array.getColor(R.styleable.actionbar_rightBtn2Color, defaultColor);
        rightBtn2.setTextColor(rightBtn2Color);

        boolean isAddStatusBarPadding = array.getBoolean(R.styleable.actionbar_statusBarPadding, false);
        if (isAddStatusBarPadding)
            setPadding(0, Util.getStatusBarHeight(context), 0, 0);
        array.recycle();

        setRightBtn1OnclickListener(null);
        setRightBtn2OnclickListener(null);
    }

    private void initWidget(Context context) {
        toolBar = findViewById(R.id.toolbar);
        iconBack = findViewById(R.id.icon_back);
        toolbarLayout = findViewById(R.id.toolbarLayout_new);
        rightBtn1 = findViewById(R.id.right_btn_1);
        rightBtn2 = findViewById(R.id.right_btn_2);
        btnParent = findViewById(R.id.btn_parent);
    }

    private void initListener(final Context context) {
        iconBack.setOnClickListener(view -> {
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        });
    }


    float downX;
    float downy;
    float maxScroll;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (titleClickListener != null) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                downX = ev.getX();
                downy = ev.getY();
                maxScroll = 0f;
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                double left = Math.min(rightBtn1.getLeft(), rightBtn2.getLeft());
                if (maxScroll <= ScreenUtils.dip2px(10f) && downX > iconBack.getRight() && downX < left){
                    titleClickListener.onClick(toolBar);
                }
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                maxScroll = Math.max(Math.abs(ev.getX() - downX), maxScroll);
                maxScroll = Math.max(Math.abs(ev.getY() - downy), maxScroll);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setIconBackVisible(int visible) {
        iconBack.setVisibility(visible);
    }

    public void setBackground(int color) {
        toolbarLayout.setBackgroundColor(color);
    }

    public void setTitle(CharSequence title) {
        toolBar.setTitle(title);
    }


    public void setIconBackOnclickListener(OnClickListener listener) {
        iconBack.setOnClickListener(listener);
    }

    public void setTitleOnclickListener(OnClickListener listener) {
        titleClickListener = listener;
    }

    public void setRightBtn1OnclickListener(OnClickListener listener) {
        rightBtn1.setOnClickListener(listener);
    }

    public void setRightBtn2OnclickListener(OnClickListener listener) {
        rightBtn2.setOnClickListener(listener);
    }

    public View getRight1View() {
        return rightBtn1;
    }

    public void setRightBtn2Color(int color) {
        rightBtn2.setTextColor(color);
    }

    public void setRightBtn2TextSize(int size) {
        rightBtn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setRightBtn2Visible(int visible) {
        rightBtn2.setVisibility(visible);
    }

    public void setRightBtn1Bg(int drawableId) {
        rightBtn1.setBackgroundResource(drawableId);
    }

    public void setRightBtn1Visible(int visible) {
        rightBtn1.setVisibility(visible);
    }

    public void setRightBtn2Text(int stringId) {
        rightBtn2.setText(stringId);
    }

    public void setRightBtn2Text(String text) {
        rightBtn2.setText(text);
    }

    public void setRightBtn1Text(String str) {
        rightBtn1.setText(str);
    }

    public void setRightBtn1TextColor(@ColorInt int color) {
        rightBtn1.setTextColor(color);
    }

    public CharSequence getTitle() {
        return toolBar.getTitle();
    }

    public CustomToolBar getToolBar() {
        return toolBar;
    }

    public void setBtn2TextSize(int size) {
        rightBtn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTitleTextSize(float size) {
        toolBar.setTitleTextSize(size);
    }

    public void setBtn1TextSize(int size) {
        rightBtn1.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTitleColor(int color) {
        toolBar.setTitleTextColor(color);
    }

    public void setCustomTypeFace(Typeface typeFace) {
        toolBar.setCustomTypeFace(typeFace);
    }

    public void setIconBackColor(int color) {
        iconBack.setTextColor(color);
    }

    public void setTitleFocus(boolean focus) {
        toolBar.setTitleFocus(focus);
    }

    public void setTitleAlpha(float alpha) {
        toolBar.setTitleTextAlpha(alpha);
    }

    public void setToolbarMargin(int marginLeft, int marginRight) {
        RelativeLayout.LayoutParams params = (LayoutParams) toolBar.getLayoutParams();
        params.leftMargin = marginLeft;
        params.rightMargin = marginRight;
        toolBar.setLayoutParams(params);
    }

    public void setNight() {
        setBackgroundResource(com.wallstreetcn.baseui.R.color.color_222222);
        int color = ContextCompat.getColor(getContext(), com.wallstreetcn.baseui.R.color.white);
        iconBack.setTextColor(color);
        rightBtn1.setTextColor(color);
        rightBtn2.setTextColor(color);
        toolBar.setTitleTextColor(color);
    }

    public void setStatusBarPaddig(boolean flag) {
        if (flag)
            setPadding(0, Util.getStatusBarHeight(context), 0, 0);
        else
            setPadding(0, 0, 0, 0);
    }
}
