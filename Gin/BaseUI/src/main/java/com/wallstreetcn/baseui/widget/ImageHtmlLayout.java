package com.wallstreetcn.baseui.widget;

import static com.wallstreetcn.helper.utils.ParagraphUtilKt.reduceSpacingParagraphsSpan;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.widget.expand.LinkMovementMethod;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.image.ImageUtlFormatHelper;
import com.wallstreetcn.helper.utils.rx.RxUtils;
import com.wallstreetcn.helper.utils.system.ScreenUtils;
import com.wallstreetcn.helper.utils.text.HtmlUtils;
import com.wallstreetcn.helper.utils.text.SpannedHelper;
import com.wallstreetcn.helper.utils.text.StringUtils;
import com.wallstreetcn.imageloader.ImageLoadManager;
import com.wallstreetcn.imageloader.WscnImageView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhangyang on 2017/6/30.
 */

public class ImageHtmlLayout extends LinearLayout {

    private static final int MAX_LINES_ON_SHRINK = 2;
    private float mTextSize;
    @ColorInt
    private int mTextColor;
    private float mLineSpacing;
    public boolean mToggleEnable;
    private int maxLinesOnShrink;
    private boolean enableShrink = true;
    private boolean mPSpace = false;
    private CharSequence lastCharsequence;
    private Disposable disposable;

    private ArrayList<String> imageList;

    public ImageHtmlLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ImageHtmlLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageHtmlLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setMaxLinesOnShrink(int maxLinesOnShrink) {
        this.maxLinesOnShrink = maxLinesOnShrink;
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.imageHtmlLayout);
        if (array != null) {
            mTextSize = array.getDimensionPixelSize(R.styleable.imageHtmlLayout_htmlTextSize, ScreenUtils.dip2px(16));
            mLineSpacing = array.getDimension(R.styleable.imageHtmlLayout_htmlLineSpace, ScreenUtils.dip2px(5));
            mTextColor = array.getColor(R.styleable.imageHtmlLayout_htmlTextColor, ContextCompat.getColor(context,com.wallstreetcn.baseui.R.color.day_mode_text_color1_333333));
            mToggleEnable = array.getBoolean(R.styleable.imageHtmlLayout_htmlEnableToggle, false);
            maxLinesOnShrink = array.getInteger(R.styleable.imageHtmlLayout_htmlMaxLineOnShrink, MAX_LINES_ON_SHRINK);
            enableShrink = array.getBoolean(R.styleable.imageHtmlLayout_htmlEnableShrink, true);
            mPSpace = array.getBoolean(R.styleable.imageHtmlLayout_htmlPspace, false);
        } else {
            mTextSize = ScreenUtils.dip2px(16);
            mLineSpacing = ScreenUtils.dip2px(5);
            mTextColor = ContextCompat.getColor(context,com.wallstreetcn.baseui.R.color.day_mode_text_color1_333333);
            mToggleEnable = false;
            maxLinesOnShrink = MAX_LINES_ON_SHRINK;
            enableShrink = true;
        }
    }

    public SpannableStringBuilder parserHtml(String html) {
        html = html.replaceAll("\n", "");
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();

        removeAllViews();
        html = html.trim();
        SpannableStringBuilder chs = new SpannableStringBuilder();
        if (mToggleEnable) {
            responseToggleEnableIsTrue(chs, html);
        } else {
            responseToggleEnableIsFalse(this, chs, html);
            addTextView(this, lastCharsequence);
        }
        return chs;
    }

    private void responseToggleEnableIsTrue(SpannableStringBuilder chs, String html) {
        LinearLayout subParent = new LinearLayout(getContext());
        subParent.setOrientation(LinearLayout.VERTICAL);
        subParent.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(subParent, params);
        addSubView(chs, html, subParent, params);
    }

    private void addSubView(SpannableStringBuilder chs, String html, LinearLayout subParent, LayoutParams params) {
        responseToggleEnableIsFalse(subParent, chs, html);
        if (TextUtils.isEmpty(SpannedHelper.trim(chs)) && TextUtils.isEmpty(SpannedHelper.trim(lastCharsequence))) {
            subParent.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(chs)) {
            OpenCloseTextView openCloseTextView = new OpenCloseTextView(getContext(), maxLinesOnShrink, false) {
                @Override
                public void toggle() {
                    if (getExpandState() == STATE_SHRINK) {
                        this.setVisibility(View.GONE);
                        subParent.setVisibility(View.VISIBLE);
                    } else {
                        super.toggle();
                    }
                }
            };
            openCloseTextView.setId(android.R.id.text1);
            openCloseTextView.setMovementMethod(new OpenCloseTextView.LinkTouchMovementMethod());
            openCloseTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            openCloseTextView.setTextColor(mTextColor);
            openCloseTextView.setLineSpacing(mLineSpacing, 1);

            openCloseTextView.setOnClickListener(v -> {
                openCloseTextView.setVisibility(View.GONE);
                subParent.setVisibility(View.VISIBLE);
            });
            addView(openCloseTextView, params);
            if (openCloseTextView.getWidth() > 0) {
                setOpenCloseText(chs, subParent, openCloseTextView);
            } else {
                disposable = RxUtils.interval(50, TimeUnit.MILLISECONDS)
                        .takeUntil(aLong -> openCloseTextView.getWidth() > 0)
                        .take(1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> setOpenCloseText(chs, subParent, openCloseTextView))
                        .subscribe();
            }
        } else {
            addTextView(subParent, lastCharsequence);
            subParent.setVisibility(View.VISIBLE);
        }
    }

    private void setOpenCloseText(SpannableStringBuilder chs, LinearLayout subParent, OpenCloseTextView openCloseTextView) {
        openCloseTextView.setLineCountCallback(lineCount -> {
            openCloseTextView.setLineCountCallback(null);
            if (lineCount <= maxLinesOnShrink && lineCount > 0) {
                addLastTextView(subParent, openCloseTextView, false);
                openCloseTextView.setVisibility(View.GONE);
                subParent.setVisibility(View.VISIBLE);
            } else if (lineCount > maxLinesOnShrink) {
                openCloseTextView.setVisibility(View.VISIBLE);
                subParent.setVisibility(View.GONE);
                addLastTextView(subParent, openCloseTextView, enableShrink);
            }
        });

        if (getMeasuredWidth() > 0)
            openCloseTextView.setmFutureTextViewWidth(getMeasuredWidth());

        openCloseTextView.setText(reduceSpacingParagraphsSpan(chs), TextView.BufferType.NORMAL);
    }

    private void addLastTextView(LinearLayout subParent, OpenCloseTextView oct, boolean needShrinkHint) {
        if (needShrinkHint) {
            lastCharsequence = StringUtils.trim(lastCharsequence);
            String shrink = ResourceUtils.getResStringFromId(R.string.to_shrink_hint);
            SpannableStringBuilder ssbExpand = new SpannableStringBuilder(lastCharsequence);
            if (lastCharsequence.length() != 0)
                ssbExpand.append(" ");
            ssbExpand.append(shrink);
            ssbExpand.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    oct.setVisibility(View.VISIBLE);
                    subParent.setVisibility(View.GONE);
                }
            }, ssbExpand.length() - shrink.length(), ssbExpand.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssbExpand.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),com.wallstreetcn.baseui.R.color.day_mode_theme_color_1478f0)), ssbExpand.length() - shrink.length(), ssbExpand.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            addTextView(subParent, ssbExpand);
        } else {
            addTextView(subParent, lastCharsequence);
        }
    }

    private void responseToggleEnableIsFalse(LinearLayout parent, SpannableStringBuilder chs, String html) {
        SpannableStringBuilder spannableString = HtmlUtils.getHtmlNoReplace(html, false);
        imageList = HtmlUtils.getImgSrcFromHtml(html);
        ArrayList<String> imageArray = HtmlUtils.getImgFromHtml(html);
        ImageSpan[] imageSpans = spannableString.getSpans(0, spannableString.length(), ImageSpan.class);
        imageSpans = getImageSpan(imageList, imageSpans);
        int textStart = 0;
        int count = 0;
        for (ImageSpan imageSpan : imageSpans) {
            try {
                String image = imageArray.get(count);
                int h = (int) Double.parseDouble(HtmlUtils.attrsFromHtml(image, "data-wscnh"));
                int w = (int) Double.parseDouble(HtmlUtils.attrsFromHtml(image, "data-wscnw"));
                int start = spannableString.getSpanStart(imageSpan);
                if (textStart < start) {
                    SpannableStringBuilder charSequence = (SpannableStringBuilder) spannableString.subSequence(textStart, start);
                    addTextView(parent, charSequence);
                    getCharSequence(chs, charSequence);
                }
                addImageView(parent, imageSpan.getSource(), w, h);
                textStart = spannableString.getSpanEnd(imageSpan);
                count++;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                count++;
            }
        }
        lastCharsequence = spannableString.subSequence(textStart, spannableString.length());
        getCharSequence(chs, lastCharsequence);
    }

    private void getCharSequence(SpannableStringBuilder chs, CharSequence charSequence) {
        try {
            if (!TextUtils.isEmpty(charSequence))
                chs.append(charSequence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ImageSpan[] getImageSpan(ArrayList<String> imageList, ImageSpan[] imageSpans) {
        if (imageList == null || imageSpans == null || (imageList.size() != imageSpans.length))
            return imageSpans;
        else {
            ImageSpan[] spans = new ImageSpan[imageSpans.length];
            for (int i = 0; i < imageSpans.length; i++) {
                String url = imageList.get(i);
                for (ImageSpan span : imageSpans) {
                    if (TextUtils.equals(span.getSource(), url)) {
                        spans[i] = span;
                        break;
                    }
                }
            }
            return spans;
        }
    }

    private void addTextView(LinearLayout parent, CharSequence charSequence) {
        CharSequence newCharSequence = SpannedHelper.trim(charSequence);
        if (TextUtils.isEmpty(newCharSequence)) {
            return;
        }

        TextView view = new TextView(getContext());
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        view.setTextColor(mTextColor);
        view.setLineSpacing(mLineSpacing, 1);
        view.setText(reduceSpacingParagraphsSpan(charSequence));
        parent.addView(view);
    }

    private void addImageView(LinearLayout parent, String source, int w, int h) {
        String imageUrl = w > ScreenUtils.getScreenWidth() ? ImageUtlFormatHelper.formatImageWithThumbnail(source, ScreenUtils.getScreenWidth(), 0) :
                ImageUtlFormatHelper.formatImageWithThumbnail(source, w, 0);
        WscnImageView imageView = new WscnImageView(getContext());
        imageView.setBackgroundColor(ContextCompat.getColor(getContext(),com.wallstreetcn.baseui.R.color.day_mode_divide_line_color_e6e6e6));
        float ratio = (w * 1f / h);
        imageView.setAspectRatio(ratio);
        int width = getWidth(w);
        int height = (int) (width / ratio);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(width, height);
        setImageViewMargin(layoutParams, parent);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setTag(imageUrl);
        ImageLoadManager.loadImage(imageUrl, imageView, 0);
        parent.addView(imageView, layoutParams);
        imageView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            String[] images = new String[imageList.size()];
            int position = 0;
            for (int i = 0; i < imageList.size(); i++) {
                images[i] = imageList.get(i);
                if (TextUtils.equals(source, imageList.get(i))) {
                    position = i;
                }
            }
            bundle.putStringArray("images", images);
            bundle.putInt("index", position);
//            RouterHelper.open("wscn://wallstreetcn.com/imagegallery", view.getContext(), bundle);
        });
    }

    private void setImageViewMargin(LinearLayout.LayoutParams layoutParams, LinearLayout parent) {
        if (parent.getChildCount() > 0 && (parent.getChildAt(parent.getChildCount() - 1) instanceof WscnImageView)) {
            View view = parent.getChildAt(parent.getChildCount() - 1);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.bottomMargin = 0;
            view.setLayoutParams(params);
            layoutParams.topMargin = 0;
            layoutParams.bottomMargin = ScreenUtils.dip2px(10);
        } else {
            layoutParams.topMargin = ScreenUtils.dip2px(10);
            layoutParams.bottomMargin = ScreenUtils.dip2px(10);
        }
    }

    private int getWidth(int w) {
        if (w > 640 || w <= 0) {
            int totalwidth = ScreenUtils.getScreenWidth();
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params != null && (params instanceof MarginLayoutParams)) {
                totalwidth = totalwidth - ((MarginLayoutParams) params).leftMargin - ((MarginLayoutParams) params).rightMargin;
            }
            return totalwidth;
        } else {
            int width = ScreenUtils.dip2px(w / 2);
            ViewGroup.LayoutParams params = getLayoutParams();
            int totalwidth = ScreenUtils.getScreenWidth();
            if (params != null && (params instanceof MarginLayoutParams)) {
                totalwidth = totalwidth - ((MarginLayoutParams) params).leftMargin - ((MarginLayoutParams) params).rightMargin;
            }

            totalwidth = totalwidth - getPaddingLeft() - getPaddingRight();

            if (width > totalwidth && totalwidth > 0)
                return totalwidth;
            else
                return width;
        }
    }
}
