package com.wallstreetcn.global.string;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.wallstreetcn.global.R;

public class DingFontSpan extends MetricAffectingSpan {

    private Typeface mTypeface;

    public DingFontSpan(Context context) {
        this(context,com.wallstreetcn.global. R.font.din_pro_medium);
    }

    public DingFontSpan(Context context, int id) {
        mTypeface = ResourcesCompat.getFont(context, id);
    }


    @Override
    public void updateMeasureState(@NonNull TextPaint tp) {
        tp.setTypeface(mTypeface);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);
    }

}