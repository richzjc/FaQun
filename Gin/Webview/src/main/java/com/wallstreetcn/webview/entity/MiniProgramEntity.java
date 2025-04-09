package com.wallstreetcn.webview.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MiniProgramEntity implements Parcelable {
    public String js;
    public String regex;

    public MiniProgramEntity(){

    }

    protected MiniProgramEntity(Parcel in) {
        js = in.readString();
        regex = in.readString();
    }

    public static final Creator<MiniProgramEntity> CREATOR = new Creator<MiniProgramEntity>() {
        @Override
        public MiniProgramEntity createFromParcel(Parcel in) {
            return new MiniProgramEntity(in);
        }

        @Override
        public MiniProgramEntity[] newArray(int size) {
            return new MiniProgramEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(js);
        dest.writeString(regex);
    }
}
