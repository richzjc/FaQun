package com.wallstreetcn.baseui.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.wallstreetcn.baseui.widget.pulltorefresh.PullToRefreshAdapterView;

import androidx.annotation.Nullable;

/**
 * Created by zhangyang on 16/6/24.
 */
public class PullToRefreshCustomRecyclerView extends PullToRefreshAdapterView {
    public PullToRefreshCustomRecyclerView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshCustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshCustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Nullable
    protected CustomRecycleView customRecycleView;

    protected void init() {
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


    public CustomRecycleView getCustomRecycleView() {
        if (customRecycleView == null)
            customRecycleView = new CustomRecycleView(getContext());
        return customRecycleView;
    }
}
