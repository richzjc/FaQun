package com.wallstreetcn.baseui.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.adapter.IDifference;
import com.wallstreetcn.helper.utils.UtilsContextManager;
import com.wallstreetcn.helper.utils.text.span.BackgroundColorSpanWithPaddingAndLineSpacing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Leif Zhang on 2018/3/8.
 * Email leifzhanggithub@gmail.com
 */

public class SkeletonEntity implements Parcelable, IDifference {
    private String id;
    private final String LINE = "                                                                                                      ";

    public void setId(String id) {
        this.id = id;
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
        if (this.context == null) {
            this.context = UtilsContextManager.getInstance().getApplication();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public SkeletonEntity(int id) {
        this.id = String.valueOf(id);
    }

    public SpannableStringBuilder getSpannableString(int line) {
        return getSpannableString(line, 0, ContextCompat.getColor(context,
               com.wallstreetcn.baseui.R.color.day_mode_divide_line_color_e6e6e6));
    }

    public SpannableStringBuilder getSpannableString(int line, float percentLine) {
        return getSpannableString(line, percentLine, ContextCompat.getColor(context,
               com.wallstreetcn.baseui.R.color.day_mode_divide_line_color_e6e6e6));
    }

    public SpannableStringBuilder getSpannableString(int line, float percentLine, int color) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        for (int i = 0; i < line; i++) {
            spannableString.append(LINE).append("\n");
        }
        if (line > 0 && percentLine == 0) {
            spannableString = (SpannableStringBuilder) spannableString.subSequence(0, spannableString.length() - 1);
        }
        int end = (int) (LINE.length() * percentLine);
        end = end > LINE.length() ? LINE.length() : end;
        if (end > 0) {
            spannableString.append(LINE.substring(0, end));
        }
        spannableString.setSpan(new BackgroundColorSpanWithPaddingAndLineSpacing(color, 5, 0),
                0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    protected SkeletonEntity(Parcel in) {

    }

    public static final Parcelable.Creator<SkeletonEntity> CREATOR = new Parcelable.Creator<SkeletonEntity>() {
        @Override
        public SkeletonEntity createFromParcel(Parcel source) {
            return new SkeletonEntity(source);
        }

        @Override
        public SkeletonEntity[] newArray(int size) {
            return new SkeletonEntity[size];
        }
    };


    public static List<SkeletonEntity> skeletonList(int count) {
        List<SkeletonEntity> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new SkeletonEntity(i));
        }
        return list;
    }

    @Override
    public String getUniqueId() {
        return getClass().getName() + id;
    }
}
