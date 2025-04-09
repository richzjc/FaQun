package com.wallstreetcn.helper.utils.text;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by zhangyang on 16/5/16.
 */
public class MaxLengthTextFilter implements InputFilter {
    private int mMax;
    private final int mMaxLength;

    public MaxLengthTextFilter(int max) {
        mMaxLength = max;
        mMax = max / 2;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                               int dstart, int dend) {
        mMax = StringUtils.getLengthSubCount(dest + source.toString(), mMaxLength);
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }

    /**
     * @return the maximum length enforced by this input filter
     */
    public int getMax() {
        return mMax;
    }

}
