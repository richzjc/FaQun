package com.wallstreetcn.baseui.customView;

import android.content.Context;
import android.util.AttributeSet;

import com.tubb.smrv.SwipeMenuRecyclerView;
import com.wallstreetcn.baseui.adapter.BaseRecycleAdapter;
import com.wallstreetcn.baseui.adapter.RVLinearLayoutManager;
import com.wallstreetcn.baseui.callback.IViewHolder;
import com.wallstreetcn.baseui.callback.IViewLoadAdapter;
import com.wallstreetcn.baseui.widget.endless.EndlessRecyclerAdapter;
import com.wallstreetcn.baseui.widget.endless.EndlessRecyclerOnScrollListener;
import com.wallstreetcn.baseui.widget.endless.HeaderSpanSizeLookup;
import com.wallstreetcn.baseui.widget.endless.ILoadMorePageListener;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by Leif Zhang on 2017/2/21.
 * Email leifzhanggithub@gmail.com
 */

public class CustomSwipeRecyclerView  extends SwipeMenuRecyclerView {

    private ILoadMorePageListener loadMorePageListener;
    private EndlessRecyclerOnScrollListener listener;
    private DefaultEmptyViewHolder viewHolder;
    private IViewHolder loadingViewHolder;
    private IViewHolder footerViewHolder;
    private boolean isEndless = true;
    private EndlessRecyclerAdapter mAdapter;


    public CustomSwipeRecyclerView(Context context) {
        super(context);
        initView();
    }

    public CustomSwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    public CustomSwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setLayoutManager(new RVLinearLayoutManager(getContext()));
        setItemAnimator(new DefaultItemAnimator());
        viewHolder = new DefaultEmptyViewHolder(this);
        loadingViewHolder = new DefaultLoadingViewHolder(this);
        footerViewHolder = new DefaultFooterViewHolder(this);
        setListener();
    }

    private void setListener() {
        ((DefaultFooterViewHolder) footerViewHolder).setOnclickListener(v -> {
            if (loadMorePageListener != null && isEndless && listener != null) {
                ((DefaultFooterViewHolder) footerViewHolder).showLoading();
                loadMorePageListener.onLoadMore(listener.getCurrentPage());
            }
        });

        listener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                if (loadMorePageListener != null && isEndless) {
                    ((DefaultFooterViewHolder) footerViewHolder).showLoading();
                    loadMorePageListener.onLoadMore(currentPage);
                }
            }
        };
        addOnScrollListener(listener);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof BaseRecycleAdapter) {
            BaseRecycleAdapter mRecycleAdapter = (BaseRecycleAdapter) adapter;
            mRecycleAdapter.setEmptyView(viewHolder.getView());
            mRecycleAdapter.setLoadingView(loadingViewHolder.getView());
            mAdapter = new EndlessRecyclerAdapter(mRecycleAdapter);
            addFooter();
            if (lookup != null) {
                lookup.setAdapter(mAdapter);
            }
            swapAdapter(mAdapter, false);
        } else {
            super.setAdapter(adapter);
        }
    }

    private void addFooter() {
        if (isEndless) {
            mAdapter.addFooterView(footerViewHolder.getView());
        }
        if (footerViewHolder instanceof IViewLoadAdapter) {
            ((IViewLoadAdapter) footerViewHolder).isRecyclerEnd(isEndless);
        }
    }

    public void setIsEndless(boolean isEndless) {
        this.isEndless = isEndless;
        ((IViewLoadAdapter) footerViewHolder).isRecyclerEnd(isEndless);
    }


    public void hideFooter(boolean isHide) {
        isEndless = !isHide;
        ((IViewLoadAdapter) footerViewHolder).isRecyclerEnd(!isHide);
        if (!isHide && mAdapter != null && mAdapter.getFooterCount() == 0) {
            mAdapter.addFooterView(footerViewHolder.getView());
        }
    }

    public void setFooterViewHolder(IViewHolder footerViewHolder) {
        this.footerViewHolder = footerViewHolder;
    }

    public void setLoadMorePageListener(ILoadMorePageListener loadMorePageListener) {
        this.loadMorePageListener = loadMorePageListener;
    }

    public void onRefreshComplete() {
        listener.clearPage();
    }

    public void onLoadingError() {
        listener.loadingError();
    }

    private HeaderSpanSizeLookup lookup;

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof GridLayoutManager) {
            if (((GridLayoutManager) layout).getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                lookup = new HeaderSpanSizeLookup(((GridLayoutManager) layout).getSpanCount());
                ((GridLayoutManager) layout).setSpanSizeLookup(lookup);
            }
        }
        super.setLayoutManager(layout);
    }

    public int getCurItemPosition() {
        LayoutManager layoutManager = getLayoutManager();
        int firstCompletelyItemPosition = 0;
        if (layoutManager instanceof GridLayoutManager) {
            firstCompletelyItemPosition = ((GridLayoutManager) layoutManager)
                    .findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            firstCompletelyItemPosition = ((LinearLayoutManager) layoutManager)
                    .findFirstCompletelyVisibleItemPosition();
        }
        return firstCompletelyItemPosition;
    }

    public void setEmptyTv(String text) {
        viewHolder.setEmptyText(text);
    }

    public void setEmptyBtn(int visible, String btnText, OnClickListener listener) {
        viewHolder.setTvBtn(visible, btnText, listener);
    }

    public void setEmptyImageRes(int resId) {
        viewHolder.setEmptyImageRes(resId);
    }

    public final void onRefresh() {
        listener.onRefreshLoading();
    }
}
