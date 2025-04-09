package com.wallstreetcn.helper.utils.text;

import android.os.Parcel;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leif Zhang on 16/9/23.
 * Email leifzhanggithub@gmail.com
 */
public class SpannedHelper {
    private final CharSequence mSource;
    private final CharSequence mReplacement;
    private final Matcher mMatcher;
    private int mAppendPosition;
    private final boolean mIsSpannable;

    public static SpannableStringBuilder replace(CharSequence source, String regex,
                                       CharSequence replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        return new SpannedHelper(source, matcher, replacement).doReplace();
    }

    public static int getRegexCount(CharSequence source, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static CharSequence trim(CharSequence mSource) {
        if(mSource == null)
            mSource = "";

        int i = mSource.length() - 1;
        while (i >= 0){
            if (mSource.charAt(i) != '\n' && mSource.charAt(i) != ' ') {
                break;
            }
            i--;
        }
        int start = 0;
        for (int j = 0; j < mSource.length(); j++) {
            start = j;
            if (mSource.charAt(j) != '\n' && mSource.charAt(j) != ' ') {
                break;
            }
        }
        if (start >= i + 1) {
            return "";
        }
        return mSource.subSequence(start, i + 1);
    }

    private SpannedHelper(CharSequence source, Matcher matcher,
                          CharSequence replacement) {
        mSource = source;
        mReplacement = replacement;
        mMatcher = matcher;
        mAppendPosition = 0;
        mIsSpannable = replacement instanceof Spannable;
    }

    private SpannableStringBuilder doReplace() {
        SpannableStringBuilder buffer = new SpannableStringBuilder();
        while (mMatcher.find()) {
            appendReplacement(buffer);
        }
        return appendTail(buffer);
    }

    private void appendReplacement(SpannableStringBuilder buffer) {
        buffer.append(mSource.subSequence(mAppendPosition, mMatcher.start()));
        CharSequence replacement = mIsSpannable
                ? copyCharSequenceWithSpans(mReplacement)
                : mReplacement;
        buffer.append(replacement);

        mAppendPosition = mMatcher.end();
    }

    public SpannableStringBuilder appendTail(SpannableStringBuilder buffer) {
        buffer.append(mSource.subSequence(mAppendPosition, mSource.length()));
        return buffer;
    }

    // This is a weird way of copying spans, but I don't know any better way.
    private CharSequence copyCharSequenceWithSpans(CharSequence string) {
        Parcel parcel = Parcel.obtain();
        try {
            TextUtils.writeToParcel(string, parcel, 0);
            parcel.setDataPosition(0);
            return TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        } finally {
            parcel.recycle();
        }
    }

}
