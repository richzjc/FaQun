package com.wallstreetcn.baseui.adapter;

import android.text.TextUtils;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

/**
 * Created by zhangyang on 2017/5/26.
 */

public class DiffCallBack extends DiffUtil.Callback {
    private List<?> mOldData, mNewData;

    public DiffCallBack(List<?> mOldData, List<?> mNewData) {
        this.mOldData = mOldData;
        this.mNewData = mNewData;
    }

    @Override
    public int getOldListSize() {
        return mOldData != null ? mOldData.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewData != null ? mNewData.size() : 0;
    }


    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Object object1 = mOldData.get(oldItemPosition);
        Object object2 = mNewData.get(newItemPosition);
        if(object1 == null || object2 == null)
            return false;
        if (object1 instanceof IDifference && object2 instanceof IDifference) {
            return TextUtils.equals(((IDifference) object1).getUniqueId(),
                    ((IDifference) object2).getUniqueId());
        } else {
            return object1.equals(object2);
        }
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Object object1 = mOldData.get(oldItemPosition);
        Object object2 = mNewData.get(newItemPosition);
        if (object1 instanceof IEqualsAdapter && object2 instanceof IEqualsAdapter) {
            return object1.equals(object2);
        } else {
            return true;
        }
    }
}