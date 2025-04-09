package com.wallstreetcn.baseui.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageHtmlEntity implements Parcelable {
    public String text;
    public String imageUrl;
    public int imageWidth;
    public int imageHeight;
    public boolean isImage;

    public ImageHtmlEntity(){

    }

    protected ImageHtmlEntity(Parcel in) {
        text = in.readString();
        imageUrl = in.readString();
        imageWidth = in.readInt();
        imageHeight = in.readInt();
        isImage = in.readByte() != 0;
    }

    public static final Creator<ImageHtmlEntity> CREATOR = new Creator<ImageHtmlEntity>() {
        @Override
        public ImageHtmlEntity createFromParcel(Parcel in) {
            return new ImageHtmlEntity(in);
        }

        @Override
        public ImageHtmlEntity[] newArray(int size) {
            return new ImageHtmlEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(imageUrl);
        dest.writeInt(imageWidth);
        dest.writeInt(imageHeight);
        dest.writeByte((byte) (isImage ? 1 : 0));
    }
}
