package com.wallstreetcn.baseui.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.wallstreetcn.baseui.widget.pulltorefresh.PullToRefreshAdapterView;

import androidx.annotation.Nullable;

/**
 * Created by Leif Zhang on 2017/2/21.
 * Email leifzhanggithub@gmail.com
 */

public class PullToRefreshSwipeRecyclerView extends PullToRefreshAdapterView {
    public PullToRefreshSwipeRecyclerView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshSwipeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshSwipeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Nullable
    private CustomSwipeRecyclerView customRecycleView;

    private void init() {
        customRecycleView = getCustomRecycleView();
        addView(customRecycleView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onRefreshComplete() {
        super.onRefreshComplete();
        getCustomRecycleView().onRefreshComplete();
    }


    public CustomSwipeRecyclerView getCustomRecycleView() {
        if (customRecycleView == null)
            customRecycleView = new CustomSwipeRecyclerView(getContext());
        return customRecycleView;
    }
}
