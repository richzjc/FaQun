package com.wallstreetcn.data.search;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 模糊搜索提示
 * 当前实现仅基于行情接口数据
 */
public class SearchPromptEntity implements Parcelable {

    public SearchPromptEntity() {
    }

    public List<Prompt> items;
    public int total_hits;
    public String filterString;

    protected SearchPromptEntity(Parcel in) {
        items = in.createTypedArrayList(Prompt.CREATOR);
        total_hits = in.readInt();
        filterString = in.readString();
    }

    public static final Creator<SearchPromptEntity> CREATOR = new Creator<SearchPromptEntity>() {
        @Override
        public SearchPromptEntity createFromParcel(Parcel in) {
            return new SearchPromptEntity(in);
        }

        @Override
        public SearchPromptEntity[] newArray(int size) {
            return new SearchPromptEntity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeInt(total_hits);
        dest.writeString(filterString);
    }


    public static class Prompt implements Parcelable {

        public String wscn_code;
        public String symbol;
        public String prod_name;
        public String asset_type;
        public String market_type;

        protected Prompt(Parcel in) {
            wscn_code = in.readString();
            symbol = in.readString();
            prod_name = in.readString();
            asset_type = in.readString();
            market_type = in.readString();
        }

        public static final Creator<Prompt> CREATOR = new Creator<Prompt>() {
            @Override
            public Prompt createFromParcel(Parcel in) {
                return new Prompt(in);
            }

            @Override
            public Prompt[] newArray(int size) {
                return new Prompt[size];
            }
        };

        public Prompt() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(wscn_code);
            dest.writeString(symbol);
            dest.writeString(prod_name);
            dest.writeString(asset_type);
            dest.writeString(market_type);
        }
    }


}
