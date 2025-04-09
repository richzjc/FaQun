package com.wallstreetcn.baseui.base;

import android.view.View;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.adapter.BaseRecycleAdapter;
import com.wallstreetcn.baseui.customView.CustomSwipeRecyclerView;
import com.wallstreetcn.baseui.customView.PullToRefreshSwipeRecyclerView;
import com.wallstreetcn.baseui.databinding.BaseFragmentRecyclerSwipeBinding;
import com.wallstreetcn.baseui.widget.endless.ILoadMorePageListener;
import com.wallstreetcn.baseui.widget.pulltorefresh.IRefreshListener;
import com.wallstreetcn.helper.utils.protocol.ErrorCode;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Leif Zhang on 2017/2/21.
 * Email leifzhanggithub@gmail.com
 */

public abstract class BaseSwipeRecyclerViewFragment<D, V, T extends BasePresenter<V>> extends BaseFragment<V, T>
        implements ILoadMorePageListener, IRefreshListener, BaseRecyclerViewCallBack<D> {

    protected PullToRefreshSwipeRecyclerView ptrRecyclerView;
    protected CustomSwipeRecyclerView        recycleView;
    protected BaseRecycleAdapter             adapter;

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        ptrRecyclerView = mViewQuery.findViewById(R.id.recycleView);
        recycleView = ptrRecyclerView.getCustomRecycleView();
        recycleView.setLoadMorePageListener(this);
        ptrRecyclerView.setRefreshListener(this);
        if (adapter == null) {
            adapter = doInitAdapter();
        }
        if (onScrollListener != null && isVisibleToUser) {
            recycleView.addOnScrollListener(onScrollListener);
        }
    }

    private RecyclerView.OnScrollListener onScrollListener;

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public BaseFragmentRecyclerSwipeBinding binding;

    @Override
    public View doGetContentView() {
        binding = BaseFragmentRecyclerSwipeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void destroyViewBinding() {
        binding = null;
    }

    @Nullable
    public abstract BaseRecycleAdapter doInitAdapter();

    @Override
    public void setData(List<D> results, boolean isCache) {
        viewManager.showContentView();
        if (!isCache) {
            ptrRecyclerView.refreshComplete();
        }
        if (adapter == null) {
            adapter = doInitAdapter();
            recycleView.setAdapter(adapter);
        }
        adapter.setData(results);
    }


    @Override
    public void doInitData() {
        super.doInitData();
        initAdapter();
        if (onScrollListener != null && recycleView != null) {
            recycleView.addOnScrollListener(onScrollListener);
        }
    }

    private void initAdapter() {
        if (recycleView.getAdapter() == null) {
            if (adapter == null) {
                adapter = doInitAdapter();
            }
            recycleView.setAdapter(adapter);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onScrollListener != null && recycleView != null) {
            recycleView.removeOnScrollListener(onScrollListener);
        }
    }

    public void autoRefresh() {
        try {
            recycleView.scrollToPosition(0);
            ptrRecyclerView.autoRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyDataRangeChange() {
        if (isAdded() && adapter != null) {
            adapter.notifyItemChanged();
        }
    }

    @Override
    public void isListFinish(boolean result) {
        if (isAdded()) {
            recycleView.hideFooter(result);
        }
    }

    @Override
    public void onResponseError(int code) {
        ptrRecyclerView.onRefreshComplete();
        recycleView.onLoadingError();
        isListFinish(true);
        if (adapter != null && adapter.getListItemCount() > 0)
            return;
        if (code == ErrorCode.EMPTYURL)
            viewManager.showNetworkErrorView();
        else
            viewManager.showLoadErrorView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (adapter != null) {
            adapter.clearData();
            adapter = null;
        }
    }

}