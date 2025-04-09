package com.wallstreetcn.faqun.model.base;

import android.os.Parcelable;

/**
 * Created by zhangyang on 16/3/2.
 */
public interface BaseResourceEntity extends Parcelable {
    String getUrl();

    String getId();

    String getKey();
}
