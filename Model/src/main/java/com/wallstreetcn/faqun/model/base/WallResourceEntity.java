package com.wallstreetcn.faqun.model.base;

import android.os.Parcel;
import android.os.Parcelable;

public class WallResourceEntity implements Parcelable {
    public String resource_title;
    public String resource_type;
    public String resource;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.resource_title);
        dest.writeString(this.resource_type);
        dest.writeString(this.resource);
    }

    public WallResourceEntity() {
    }

    protected WallResourceEntity(Parcel in) {
        this.resource_title = in.readString();
        this.resource_type = in.readString();
        this.resource = in.readString();
    }

    public static final Parcelable.Creator<WallResourceEntity> CREATOR = new Parcelable.Creator<WallResourceEntity>() {
        @Override
        public WallResourceEntity createFromParcel(Parcel source) {
            return new WallResourceEntity(source);
        }

        @Override
        public WallResourceEntity[] newArray(int size) {
            return new WallResourceEntity[size];
        }
    };
}
