package com.wallstreetcn.baseui.adapter;

import com.wallstreetcn.helper.utils.TLog;

import androidx.recyclerview.widget.DiffUtil;

public class AdapterDiffUtil {
    public static boolean calculateDiff(DiffUtil.Callback cb) {
        boolean isNeedNotifyDataSetChange = false;
        final int oldSize = cb.getOldListSize();
        final int newSize = cb.getNewListSize();
        if (newSize > 0) {
            int changedCount = 0;
            for (int i = 0; i < newSize; i++) {
                if (i < oldSize) {
                    if (!cb.areItemsTheSame(i, i)) {
                        changedCount++;
                    } else if (!cb.areContentsTheSame(i, i)) {
                        changedCount++;
                    }
                } else {
                    changedCount = changedCount + (newSize - i);
                    break;
                }
            }

            float rate = (changedCount * 1.0f) / newSize;
            isNeedNotifyDataSetChange = (rate >= 0.7);
            TLog.i("AdapterDataDelegate  newSize = " + newSize + "; changeCount = " + changedCount + "; rate = " + rate);
        }else{
            isNeedNotifyDataSetChange = true;
        }
        return isNeedNotifyDataSetChange;
    }
}
