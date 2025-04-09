package com.wallstreetcn.baseui.customView;

import static com.wallstreetcn.helper.utils.observer.ObserverIds.NIGHT_TYPE_CHANGED;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.richzjc.observer.Observer;
import com.richzjc.observer.ObserverManger;
import com.wallstreetcn.baseui.adapter.BaseRecycleAdapter;
import com.wallstreetcn.baseui.adapter.RVLinearLayoutManager;
import com.wallstreetcn.baseui.callback.IViewHolder;
import com.wallstreetcn.baseui.callback.IViewLoadAdapter;
import com.wallstreetcn.baseui.widget.endless.EndlessRecyclerAdapter;
import com.wallstreetcn.baseui.widget.endless.EndlessRecyclerOnScrollListener;
import com.wallstreetcn.baseui.widget.endless.HeaderSpanSizeLookup;
import com.wallstreetcn.baseui.widget.endless.ILoadMorePageListener;
import com.wallstreetcn.helper.utils.TLog;

import java.lang.reflect.Field;

/**
 * Created by micker on 16/6/16.
 */
public class CustomRecycleView extends RecyclerView implements Observer {

    private ILoadMorePageListener loadMorePageListener;
    private EndlessRecyclerOnScrollListener listener;
    private DefaultEmptyViewHolder viewHolder;
    private IViewHolder loadingViewHolder;
    private IViewHolder footerViewHolder;
    private boolean isEndless = true;
    private EndlessRecyclerAdapter mAdapter;


    public CustomRecycleView(Context context) {
        super(context);
        initView();
    }

    public CustomRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        ObserverManger.getInstance().registerObserver(this, NIGHT_TYPE_CHANGED);
        if (getLayoutManager() == null) {
            setLayoutManager(new RVLinearLayoutManager(getContext()));
        }
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        setItemAnimator(itemAnimator);

        viewHolder = new DefaultEmptyViewHolder(this);
        loadingViewHolder = new DefaultLoadingViewHolder(this);
        footerViewHolder = new DefaultFooterViewHolder(this);
        setListener();
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
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

            @Override
            public Adapter getEndlessRecyclerAdapter(RecyclerView recyclerView) {
                return mAdapter == null ? super.getEndlessRecyclerAdapter(recyclerView) : mAdapter;
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
            setAdapter(mAdapter);
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

    public void setFooterViewVisible(int visible) {
        if (footerViewHolder == null || footerViewHolder.getView() == null)
            return;

        footerViewHolder.getView().setVisibility(visible);
    }

    public void removeFooterView() {
        isEndless = false;
        ((IViewLoadAdapter) footerViewHolder).isRecyclerEnd(isEndless);
        if (mAdapter != null && mAdapter.getFooterCount() > 0)
            mAdapter.removeFooterView(footerViewHolder.getView());
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

    @Override
    public ViewHolder getChildViewHolder(View child) {
        return super.getChildViewHolder(child);
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

    @Override
    public void clearOnScrollListeners() {
        super.clearOnScrollListeners();
        addOnScrollListener(listener);
    }

    public void setWrapEmptyView() {
        viewHolder.setWrapContent();
    }

    @Override
    public void update(int i, Object... objects) {
        if (i == NIGHT_TYPE_CHANGED)
            setAdapter(null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ObserverManger.getInstance().removeObserver(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            super.onLayout(changed, l, t, r, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
