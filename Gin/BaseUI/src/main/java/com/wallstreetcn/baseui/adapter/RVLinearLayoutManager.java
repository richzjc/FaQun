package com.wallstreetcn.baseui.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wscn on 17/6/12.
 */

public class RVLinearLayoutManager extends LinearLayoutManager {
    private final String TAG = "RVLinearLayoutManager";
    public RVLinearLayoutManager(Context context) {
        super(context);
    }

    public RVLinearLayoutManager(Context context, int orientation) {
        this(context, orientation, false);
    }

    public RVLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public RVLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static RVLinearLayoutManager HorizontalLayoutManager(Context context) {
        return new RVLinearLayoutManager(context, HORIZONTAL);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getChildCount() {
        return super.getChildCount();
    }
}
