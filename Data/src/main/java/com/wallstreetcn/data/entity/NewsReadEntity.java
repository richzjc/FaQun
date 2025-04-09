package com.wallstreetcn.data.entity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.wallstreetcn.baseui.adapter.IDifference;
import com.wallstreetcn.helper.utils.AndroidExtsKt;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.date.DateFormatUtil;
import com.wallstreetcn.helper.utils.system.ScreenUtils;
import com.wallstreetcn.helper.utils.text.TextUtil;
import com.wallstreetcn.helper.utils.text.span.CenterImageSpan;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.DrawableRes;

/**
 * Created by Leif Zhang on 2017/2/8.
 * Email leifzhanggithub@gmail.com
 */

public class NewsReadEntity implements Parcelable, IDifference {
    private String id;
    private String title;
    private String authorName;
    private long displayTime;
    private String imageUri;
    private String uri;
    private long historyTime;
    private boolean vip;
    private String vip_type;

    public long getHistoryTime() {
        return historyTime;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public void setHistoryTime(long historyTime) {
        this.historyTime = historyTime / 1000;
    }

    public long getHeaderId() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyyMMdd", Locale.CHINA);
            String result = DateFormatUtil.getTime(historyTime, simpleDateFormat);
            return Long.valueOf(result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }

    public String getVip_type() {
        return vip_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(boolean vip) {
        this.vip = vip;
        uri = TextUtil.format("wscn://wallstreetcn.com/%sarticles/%s", vip ? "vip/" : "", id);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public CharSequence getDisplayTime(CharSequence charSequence) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (isVip() || !TextUtils.isEmpty(vip_type)) {
            spannableStringBuilder.append(charSequence);
            spannableStringBuilder.append("   ");
        }

        if (!TextUtils.isEmpty(authorName) && !TextUtils.isEmpty(id) && !id.contains("live") && !id.contains("chart"))
            spannableStringBuilder.append(authorName).append("   ");

        spannableStringBuilder.append(DateFormatUtil.dateFormat(displayTime));
        return SpannableString.valueOf(spannableStringBuilder);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(long displayTime) {
        this.displayTime = displayTime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public NewsReadEntity() {
    }

    @Override
    public String getUniqueId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.authorName);
        dest.writeLong(this.displayTime);
        dest.writeString(this.imageUri);
        dest.writeString(this.uri);
        dest.writeLong(this.historyTime);
        dest.writeByte(this.vip ? (byte) 1 : (byte) 0);
        dest.writeString(this.vip_type);
    }

    protected NewsReadEntity(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.authorName = in.readString();
        this.displayTime = in.readLong();
        this.imageUri = in.readString();
        this.uri = in.readString();
        this.historyTime = in.readLong();
        this.vip = in.readByte() != 0;
        this.vip_type = in.readString();
    }

    public static final Creator<NewsReadEntity> CREATOR = new Creator<NewsReadEntity>() {
        @Override
        public NewsReadEntity createFromParcel(Parcel source) {
            return new NewsReadEntity(source);
        }

        @Override
        public NewsReadEntity[] newArray(int size) {
            return new NewsReadEntity[size];
        }
    };
}
