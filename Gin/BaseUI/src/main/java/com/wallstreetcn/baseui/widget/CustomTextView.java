package com.wallstreetcn.baseui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.widget.EmojiTextView;

import com.richzjc.observer.Observer;
import com.richzjc.observer.ObserverManger;
import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.skin.SkinActivity;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.observer.ObserverIds;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

public class CustomTextView extends EmojiTextView implements Observer {
    private boolean isClickable = true;
    private boolean fontScaleResource = false;
    private float originSpTextSize;


    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(getContext1(context), attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.customTextView);
        fontScaleResource = array.getBoolean(R.styleable.customTextView_fontScaleResource, false);
        array.recycle();
        if (fontScaleResource && getContext() instanceof SkinActivity) {
            float textSize = getTextSize();
            originSpTextSize = ScreenUtils.px2sp(getContext(), textSize);

            setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.sp2px(originSpTextSize, ((SkinActivity) getContext()).getFontScaleResource()));
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (fontScaleResource) {
            ObserverManger.getInstance().registerObserver(this, ObserverIds.UPDATE_FONT_INDEX);
            if (originSpTextSize <= 0) {
                float textSize = getTextSize();
                originSpTextSize = ScreenUtils.px2sp(getContext(), textSize);
            }

            if (originSpTextSize > 0 && getContext() instanceof SkinActivity) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.sp2px(originSpTextSize, ((SkinActivity) getContext()).getFontScaleResource()));
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ObserverManger.getInstance().removeObserver(this);
    }

    @Override
    public void setTextSize(float size) {
        originSpTextSize = size;
        if (fontScaleResource && getContext() instanceof SkinActivity) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.sp2px(size, ((SkinActivity) getContext()).getFontScaleResource()));
        } else {
            super.setTextSize(size);
        }
    }

    public void setFontScaleResource(boolean fontScaleResource) {
        this.fontScaleResource = fontScaleResource;
        if (fontScaleResource)
            ObserverManger.getInstance().registerObserver(this, ObserverIds.UPDATE_FONT_INDEX);
        if (originSpTextSize <= 0) {
            float textSize = getTextSize();
            originSpTextSize = ScreenUtils.px2sp(getContext(), textSize);
        }

        if (originSpTextSize > 0 && fontScaleResource && getContext() instanceof SkinActivity) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.sp2px(originSpTextSize, ((SkinActivity) getContext()).getFontScaleResource()));
        }
    }

    @Override
    public void setTextSize(int unit, float size) {
        if (unit == TypedValue.COMPLEX_UNIT_SP)
            originSpTextSize = size;
        if (unit == TypedValue.COMPLEX_UNIT_SP && fontScaleResource && getContext() instanceof SkinActivity) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.sp2px(size, ((SkinActivity) getContext()).getFontScaleResource()));
        } else {
            super.setTextSize(unit, size);
        }
    }

    private static Context getContext1(Context context) {
        initEmoji(context);
        return context;
    }

    private static void initEmoji(Context context) {
        try {
            BundledEmojiCompatConfig config = new BundledEmojiCompatConfig(context);
            config.setReplaceAll(true)
                    .registerInitCallback(new EmojiCompat.InitCallback() {
                        @Override
                        public void onInitialized() {
                            TLog.i("", "EmojiCompat initialized");
                        }

                        @Override
                        public void onFailed(@Nullable Throwable throwable) {
                            TLog.e("", "EmojiCompat initialization failed", throwable);
                        }
                    });

            EmojiCompat.init(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TextView widget = this;
        Object text = widget.getText();
        if (text instanceof Spanned) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_DOWN) {

                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = ((Spanned) text).getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_DOWN) {
                        isClickable = false;
                    }
                }
            }

        }
        boolean flag = super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP)
            isClickable = true;
        return flag;
    }

    @Override
    public boolean performClick() {
        if (isClickable)
            return super.performClick();
        else return false;
    }

    @Override
    public void update(int i, Object... objects) {
        if (i == ObserverIds.UPDATE_FONT_INDEX) {
            if (originSpTextSize > 0 && fontScaleResource && getContext() instanceof SkinActivity) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.sp2px(originSpTextSize, ((SkinActivity) getContext()).getFontScaleResource()));
            }
        }
    }
}