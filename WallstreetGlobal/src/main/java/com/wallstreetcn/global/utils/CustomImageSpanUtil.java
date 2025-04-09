package com.wallstreetcn.global.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;

public class CustomImageSpanUtil {

    public static SpannableStringBuilder contact(SpannableString... first) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (SpannableString spannableString : first) {
            builder.append(spannableString);
        }
        return builder;
    }
}
