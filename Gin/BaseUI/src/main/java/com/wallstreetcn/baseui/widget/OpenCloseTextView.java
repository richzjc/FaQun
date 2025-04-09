package com.wallstreetcn.baseui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;

import com.wallstreetcn.helper.utils.TLog;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.widget.expand.LinkMovementMethod;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.text.SpannedHelper;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class OpenCloseTextView extends CustomTextView {

    public static final int STATE_SHRINK = 0;
    public static final int STATE_EXPAND = 1;

    private static final String CLASS_NAME_VIEW = "android.view.View";
    private static final String CLASS_NAME_LISTENER_INFO = "android.view.View$ListenerInfo";
    private static final String ELLIPSIS_HINT = "...";
    private static final String GAP_TO_EXPAND_HINT = " ";
    private static final String GAP_TO_SHRINK_HINT = " ";
    private static final int MAX_LINES_ON_SHRINK = 2;
    private static final int TO_EXPAND_HINT_COLOR = 0xFF1482f0;
    private static final int TO_SHRINK_HINT_COLOR = 0xFF1482f0;
    private static final int TO_EXPAND_HINT_COLOR_BG_PRESSED = 0x55999999;
    private static final int TO_SHRINK_HINT_COLOR_BG_PRESSED = 0x55999999;
    private static final boolean TOGGLE_ENABLE = true;
    private static final boolean SHOW_TO_EXPAND_HINT = true;
    private static final boolean SHOW_TO_SHRINK_HINT = true;

    private String mEllipsisHint;
    private String mToExpandHint;
    private String mToShrinkHint;
    private String mGapToExpandHint = GAP_TO_EXPAND_HINT;
    private String mGapToShrinkHint = GAP_TO_SHRINK_HINT;
    private boolean mToggleEnable = TOGGLE_ENABLE;
    private boolean mShowToExpandHint = SHOW_TO_EXPAND_HINT;
    private boolean mShowToShrinkHint = SHOW_TO_SHRINK_HINT;
    protected int mMaxLinesOnShrink = MAX_LINES_ON_SHRINK;
    protected int mToExpandHintColor = TO_EXPAND_HINT_COLOR;
    protected int mToShrinkHintColor = TO_SHRINK_HINT_COLOR;
    private int mToExpandHintColorBgPressed = TO_EXPAND_HINT_COLOR_BG_PRESSED;
    private int mToShrinkHintColorBgPressed = TO_SHRINK_HINT_COLOR_BG_PRESSED;
    private int mCurrState = STATE_SHRINK;
    private boolean mHintToggleEnable = true;

    private ClickableSpan mTouchableSpan;
    private BufferType mBufferType = BufferType.NORMAL;
    private TextPaint mTextPaint;
    private Layout mLayout;
    private int mTextLineCount = -1;
    private int mLayoutWidth = 0;
    private int mFutureTextViewWidth = 0;

    private CharSequence mOrigText;
    private ExpandableClickListener mExpandableClickListener;
    private OnExpandListener mOnExpandListener;
    private LineCountCallback lineCountCallback;

    public OpenCloseTextView(Context context) {
        super(context);
        init();
    }

    public OpenCloseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public OpenCloseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    public OpenCloseTextView(Context context, int mMaxLinesOnShrink, boolean toggleEnable) {
        super(context);
        this.mMaxLinesOnShrink = mMaxLinesOnShrink;
        this.mToggleEnable = toggleEnable;
        init();
    }

    public void setMaxLinesOnShrink(int maxLinesOnShrink) {
        this.mMaxLinesOnShrink = maxLinesOnShrink;
    }

    public void setTouchSpan(ClickableSpan clickableSpan) {
        this.mTouchableSpan = clickableSpan;
    }

    public OpenCloseTextView setmToExpandHint(String mToExpandHint) {
        this.mToExpandHint = mToExpandHint;
        return this;
    }

    public OpenCloseTextView setmToShrinkHint(String mToShrinkHint) {
        this.mToShrinkHint = mToShrinkHint;
        return this;
    }

    public void setLineCountCallback(LineCountCallback callback) {
        this.lineCountCallback = callback;
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        if (a == null) {
            return;
        }
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ExpandableTextView_etv_MaxLinesOnShrink) {
                mMaxLinesOnShrink = a.getInteger(attr, MAX_LINES_ON_SHRINK);
            } else if (attr == R.styleable.ExpandableTextView_etv_EllipsisHint) {
                mEllipsisHint = a.getString(attr);
            } else if (attr == R.styleable.ExpandableTextView_etv_ToExpandHint) {
                mToExpandHint = a.getString(attr);
            } else if (attr == R.styleable.ExpandableTextView_etv_ToShrinkHint) {
                mToShrinkHint = a.getString(attr);
            } else if (attr == R.styleable.ExpandableTextView_etv_EnableToggle) {
                mToggleEnable = a.getBoolean(attr, TOGGLE_ENABLE);
            } else if (attr == R.styleable.ExpandableTextView_etv_ToExpandHintShow) {
                mShowToExpandHint = a.getBoolean(attr, SHOW_TO_EXPAND_HINT);
            } else if (attr == R.styleable.ExpandableTextView_etv_ToShrinkHintShow) {
                mShowToShrinkHint = a.getBoolean(attr, SHOW_TO_SHRINK_HINT);
            } else if (attr == R.styleable.ExpandableTextView_etv_ToExpandHintColor) {
                mToExpandHintColor = a.getInteger(attr, TO_EXPAND_HINT_COLOR);
            } else if (attr == R.styleable.ExpandableTextView_etv_ToShrinkHintColor) {
                mToShrinkHintColor = a.getInteger(attr, TO_SHRINK_HINT_COLOR);
            } else if (attr == R.styleable.ExpandableTextView_etv_ToExpandHintColorBgPressed) {
                mToExpandHintColorBgPressed = a.getInteger(attr, TO_EXPAND_HINT_COLOR_BG_PRESSED);
            } else if (attr == R.styleable.ExpandableTextView_etv_ToShrinkHintColorBgPressed) {
                mToShrinkHintColorBgPressed = a.getInteger(attr, TO_SHRINK_HINT_COLOR_BG_PRESSED);
            } else if (attr == R.styleable.ExpandableTextView_etv_InitState) {
                mCurrState = a.getInteger(attr, STATE_SHRINK);
            } else if (attr == R.styleable.ExpandableTextView_etv_GapToExpandHint) {
                mGapToExpandHint = a.getString(attr);
            } else if (attr == R.styleable.ExpandableTextView_etv_GapToShrinkHint) {
                mGapToShrinkHint = a.getString(attr);
            } else if (attr == R.styleable.ExpandableTextView_etv_enableHintToggle) {
                mHintToggleEnable = a.getBoolean(attr, true);
            }
        }
        a.recycle();
    }

    private void init() {
        mTouchableSpan = new TouchableSpan(this);
        if (mHintToggleEnable)
            setMovementMethod(new LinkTouchMovementMethod());
        if (TextUtils.isEmpty(mEllipsisHint)) {
            mEllipsisHint = ELLIPSIS_HINT;
        }
        if (TextUtils.isEmpty(mToExpandHint)) {
            mToExpandHint = ResourceUtils.getResStringFromId(R.string.to_expand_hint);
        }
        if (TextUtils.isEmpty(mToShrinkHint)) {
            mToShrinkHint = ResourceUtils.getResStringFromId(R.string.to_shrink_hint);
        }
        if (mToggleEnable) {
            mExpandableClickListener = new ExpandableClickListener();
            setOnClickListener(mExpandableClickListener);
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
                setTextInternal(getNewTextByConfig(), mBufferType);
            }
        });
    }

    public void updateForRecyclerView(CharSequence text, int futureTextViewWidth, int expandState) {
        mFutureTextViewWidth = futureTextViewWidth;
        mCurrState = expandState;
        setText(text);
    }

    public void setmFutureTextViewWidth(int futureTextViewWidth) {
        if (futureTextViewWidth >= 0)
            this.mFutureTextViewWidth = futureTextViewWidth;
    }

    public boolean isSmallerThanMaxLines() {
        return mTextLineCount <= mMaxLinesOnShrink;
    }

    public int getExpandState() {
        return mCurrState;
    }

    private CharSequence getNewTextByConfig() {
        if (TextUtils.isEmpty(mOrigText)) {
            notifyLine(0);
            return mOrigText;
        }

        mLayout = getLayout();
        if (mLayout != null) {
            mLayoutWidth = mLayout.getWidth();
        }

        if (mLayoutWidth <= 0) {
            if (getWidth() == 0) {
                if (mFutureTextViewWidth == 0) {
                    notifyLine(-1);
                    return mOrigText;
                } else {
                    mLayoutWidth = mFutureTextViewWidth - getPaddingLeft() - getPaddingRight();
                }
            } else {
                mLayoutWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            }
        }

        mTextPaint = getPaint();
        mTextLineCount = -1;
        switch (mCurrState) {
            case STATE_SHRINK: {
                mLayout = new DynamicLayout(mOrigText, mTextPaint, mLayoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                mTextLineCount = mLayout.getLineCount();
                notifyLine(mTextLineCount);
                if (mTextLineCount <= mMaxLinesOnShrink) {
                    return mOrigText;
                } else {
                    return responseLineLargerShrink(mLayout);
                }
            }
            case STATE_EXPAND: {
                return responseToExpand();
            }
        }
        return mOrigText;
    }

    private CharSequence responseToExpand() {
        if (!mShowToShrinkHint) {
            return mOrigText;
        }
        mLayout = new DynamicLayout(mOrigText, mTextPaint, mLayoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        mTextLineCount = mLayout.getLineCount();
        notifyLine(mTextLineCount);
        if (mTextLineCount <= mMaxLinesOnShrink) {
            return mOrigText;
        }

        SpannableStringBuilder ssbExpand = new SpannableStringBuilder(mOrigText)
                .append(mGapToShrinkHint).append(mToShrinkHint);
        ssbExpand.setSpan(mTouchableSpan, ssbExpand.length() - getLengthOfString(mToShrinkHint), ssbExpand.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssbExpand;
    }

    @NotNull
    private CharSequence responseLineLargerShrink(Layout mLayout) {
        int indexEnd = getValidLayout().getLineEnd(mMaxLinesOnShrink - 1);
        int indexStart = getValidLayout().getLineStart(mMaxLinesOnShrink - 1);
        CharSequence charSequence = mOrigText.subSequence(indexStart, indexEnd);
        int length = SpannedHelper.trim(charSequence).length();
        CharSequence tailCharSequence = mOrigText.subSequence(indexEnd, mOrigText.length());
        if (length > 0) {
            if (SpannedHelper.trim(tailCharSequence).length() <= 0) {
                return SpannedHelper.trim(mOrigText.subSequence(0, indexEnd));
            } else {
                return responseLargeZero(indexStart, indexEnd);
            }
        } else {
            if (SpannedHelper.trim(tailCharSequence).length() <= 0) {
                return SpannedHelper.trim(mOrigText.subSequence(0, indexStart));
            } else {
                return responseSmallZero(indexStart, mLayout);
            }
        }
    }

    private CharSequence responseSmallZero(int indexStart, Layout layout) {
        SpannableStringBuilder builder = new SpannableStringBuilder(SpannedHelper.trim(mOrigText.subSequence(0, indexStart)));
        int totalCount = layout.getLineCount();
        int tempStart = 0;
        int tempEnd = 0;
        CharSequence tempCharsequence = null;
        for (int i = mMaxLinesOnShrink; i < totalCount; i++) {
            tempStart = layout.getLineStart(i);
            tempEnd = layout.getLineEnd(i);
            tempCharsequence = SpannedHelper.trim(mOrigText.subSequence(tempStart, tempEnd));
            if (tempCharsequence.length() != 0)
                break;
        }

        if (tempCharsequence == null || tempCharsequence.length() == 0) {
            return builder;
        } else {
            builder.append("\n");
            CharSequence chs = mOrigText.subSequence(tempEnd, mOrigText.length());
            if (SpannedHelper.trim(chs).length() <= 0) {
                builder.append(tempCharsequence);
                return builder;
            } else {
                int subIndexEnd = calEndIndex(tempStart, tempEnd);
                SpannableStringBuilder ssbShrink = new SpannableStringBuilder(SpannedHelper.trim(mOrigText.subSequence(tempStart, subIndexEnd))).append(mEllipsisHint);
                if (mShowToExpandHint) {
                    ssbShrink.append(getContentOfString(mGapToExpandHint) + getContentOfString(mToExpandHint));
                    ssbShrink.setSpan(mTouchableSpan, ssbShrink.length() - getLengthOfString(mToExpandHint), ssbShrink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                builder.append(ssbShrink);
                return builder;
            }
        }
    }

    private int calEndIndex(int indexStart, int indexEnd) {
        TLog.e("text", mOrigText.subSequence(indexStart, indexEnd).toString());

        float widthTailReplaced = mTextPaint.measureText(getContentOfString(mEllipsisHint + " ")
                + (mShowToExpandHint ? (getContentOfString(mToExpandHint) + getContentOfString(mGapToExpandHint)) : ""));
        int remainWidth = getValidLayout().getWidth() - (int) (mTextPaint.measureText(mOrigText.subSequence(indexStart, indexEnd).toString()) + 0.5);
        if (remainWidth > widthTailReplaced)
            return indexEnd;
        else {
            int indexEndTrimmed = indexEnd;
            indexEndTrimmed--;
            while (indexEndTrimmed > indexStart) {
                remainWidth = getValidLayout().getWidth() -
                        (int) (mTextPaint.measureText(mOrigText.subSequence(indexStart, indexEndTrimmed).toString()) + 0.5);
                if (remainWidth > widthTailReplaced)
                    break;
                else
                    indexEndTrimmed--;
            }
            return indexEndTrimmed;
        }
    }

    private CharSequence responseLargeZero(int indexStart, int indexEnd) {
        int subIndexEnd = calEndIndex(indexStart, indexEnd);
        SpannableStringBuilder ssbShrink = new SpannableStringBuilder(SpannedHelper.trim(mOrigText.subSequence(0, subIndexEnd)))
                .append(mEllipsisHint);
        if (mShowToExpandHint) {
            ssbShrink.append(getContentOfString(mGapToExpandHint) + getContentOfString(mToExpandHint));
            ssbShrink.setSpan(mTouchableSpan, ssbShrink.length() - getLengthOfString(mToExpandHint), ssbShrink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssbShrink;
    }

    private void notifyLine(int mTextLineCount) {
        if (lineCountCallback != null && mTextLineCount > 0)
            lineCountCallback.getLineCount(mTextLineCount);
    }

    public void setExpandListener(OnExpandListener listener) {
        mOnExpandListener = listener;
    }

    private Layout getValidLayout() {
        return mLayout != null ? mLayout : getLayout();
    }

    public void toggle() {
        TLog.e("otv", "toggle:  " + mCurrState);
        switch (mCurrState) {
            case STATE_SHRINK:
                mCurrState = STATE_EXPAND;
                if (mOnExpandListener != null) {
                    mOnExpandListener.onExpand(this);
                }
                break;
            case STATE_EXPAND:
                mCurrState = STATE_SHRINK;
                if (mOnExpandListener != null) {
                    mOnExpandListener.onShrink(this);
                }
                break;
        }
        setTextInternal(getNewTextByConfig(), mBufferType);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mOrigText = text;
        mBufferType = type;
        setTextInternal(getNewTextByConfig(), type);
    }

    private void setTextInternal(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    private int getLengthOfString(String string) {
        if (string == null)
            return 0;
        return string.length();
    }

    private String getContentOfString(String string) {
        if (string == null)
            return "";
        return string;
    }

    public interface OnExpandListener {
        void onExpand(OpenCloseTextView view);

        void onShrink(OpenCloseTextView view);
    }

    public class ExpandableClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            toggle();
        }
    }

    public OnClickListener getOnClickListener(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getOnClickListenerV14(view);
        } else {
            return getOnClickListenerV(view);
        }
    }

    private OnClickListener getOnClickListenerV(View view) {
        OnClickListener retrievedListener = null;
        try {
            Field field = Class.forName(CLASS_NAME_VIEW).getDeclaredField("mOnClickListener");
            field.setAccessible(true);
            retrievedListener = (OnClickListener) field.get(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retrievedListener;
    }

    private OnClickListener getOnClickListenerV14(View view) {
        OnClickListener retrievedListener = null;
        try {
            Field listenerField = Class.forName(CLASS_NAME_VIEW).getDeclaredField("mListenerInfo");
            Object listenerInfo = null;

            if (listenerField != null) {
                listenerField.setAccessible(true);
                listenerInfo = listenerField.get(view);
            }

            Field clickListenerField = Class.forName(CLASS_NAME_LISTENER_INFO).getDeclaredField("mOnClickListener");

            if (clickListenerField != null && listenerInfo != null) {
                clickListenerField.setAccessible(true);
                retrievedListener = (OnClickListener) clickListenerField.get(listenerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retrievedListener;
    }

    public static class TouchableSpan extends ClickableSpan {
        private boolean mIsPressed;
        private OpenCloseTextView closeTextView;

        public TouchableSpan(OpenCloseTextView closeTextView) {
            this.closeTextView = closeTextView;
        }


        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }


        @Override
        public void onClick(View widget) {
            if (!closeTextView.hasOnClickListeners() || (!(closeTextView.getOnClickListener(closeTextView) instanceof ExpandableClickListener))) {
                closeTextView.toggle();
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            switch (closeTextView.getExpandState()) {
                case STATE_SHRINK:
                    ds.setColor(closeTextView.mToExpandHintColor);
                    ds.bgColor = mIsPressed ? closeTextView.mToExpandHintColorBgPressed : 0;
                    break;
                case STATE_EXPAND:
                    ds.setColor(closeTextView.mToShrinkHintColor);
                    ds.bgColor = mIsPressed ? closeTextView.mToShrinkHintColorBgPressed : 0;
                    break;
            }
            ds.setUnderlineText(false);
        }
    }

    public static class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return super.onTouchEvent(textView, spannable, event);
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }
    }

    public interface LineCountCallback {
        void getLineCount(int lineCount);
    }
}