package com.wallstreetcn.global.utils;

import android.os.Parcelable;

import com.wallstreetcn.baseui.adapter.BaseRecycleAdapter;

/**
 * Created by  Leif Zhang on 2018/7/3.
 * Email leifzhanggithub@gmail.com
 */
public interface IRecyclerAdapter {
    BaseRecycleAdapter getAdapter(Parcelable entity);
}
