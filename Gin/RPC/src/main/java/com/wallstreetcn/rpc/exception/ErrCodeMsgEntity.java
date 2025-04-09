package com.wallstreetcn.rpc.exception;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallstreetcn.rpc.AbstractApi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wscn on 16/12/16.
 */

public class ErrCodeMsgEntity implements Parcelable {

    private int code;
    private String message;
    private Map<String, String> params;
    public AbstractApi api;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.message);
    }

    public ErrCodeMsgEntity() {
    }

    protected ErrCodeMsgEntity(Parcel in) {
        this.code = in.readInt();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<ErrCodeMsgEntity> CREATOR = new Parcelable.Creator<ErrCodeMsgEntity>() {
        @Override
        public ErrCodeMsgEntity createFromParcel(Parcel source) {
            return new ErrCodeMsgEntity(source);
        }

        @Override
        public ErrCodeMsgEntity[] newArray(int size) {
            return new ErrCodeMsgEntity[size];
        }
    };
}
