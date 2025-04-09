package com.wallstreetcn.baseui.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

/**
 * Created by ichongliang on 05/03/2018.
 */

public class PullToRefreshAdapterView extends SmartRefreshLayout implements OnRefreshListener {

    private IRefreshListener refreshListener;
    private RefreshHeader header;
    private boolean isCanRefresh = true;

    public PullToRefreshAdapterView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        init();
    }


    private void init() {
        header = new SmartRefreshHeaderView(getContext());
        setRefreshHeader(header, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setOnRefreshListener(this);
        setEnableLoadMore(false);
    }

    public void setCanRefresh(boolean canRefresh) {
        setEnableRefresh(canRefresh);
        isCanRefresh = canRefresh;
    }

    public void setRefreshListener(IRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void onRefreshComplete() {
        refreshComplete();
    }

    public boolean isCanRefresh() {
        return isCanRefresh;
    }

    public void refreshComplete() {
        finishRefresh();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    @Override
    public boolean autoRefresh() {
        boolean refresh = super.autoRefresh();

        if (!refresh && mRefreshListener != null) {
            mRefreshListener.onRefresh(this);
        }
        return refresh;
    }
}
