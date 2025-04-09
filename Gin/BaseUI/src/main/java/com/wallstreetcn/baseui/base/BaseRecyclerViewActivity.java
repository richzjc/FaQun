package com.wallstreetcn.baseui.base;

import com.wallstreetcn.baseui.databinding.BaseActivityRecycleBinding;
import com.wallstreetcn.helper.utils.TLog;

import android.view.View;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.adapter.BaseRecycleAdapter;
import com.wallstreetcn.baseui.customView.CustomRecycleView;
import com.wallstreetcn.baseui.customView.PullToRefreshCustomRecyclerView;
import com.wallstreetcn.baseui.widget.TitleBar;
import com.wallstreetcn.baseui.widget.endless.ILoadMorePageListener;
import com.wallstreetcn.baseui.widget.pulltorefresh.IRefreshListener;
import com.wallstreetcn.helper.utils.protocol.ErrorCode;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by Leif Zhang on 16/7/22.
 * Email leifzhanggithub@gmail.com
 */
public abstract class BaseRecyclerViewActivity<D, V, T extends BasePresenter<V>>
        extends BaseActivity<V, T> implements ILoadMorePageListener, IRefreshListener,
        BaseRecyclerViewCallBack<D> {

    protected PullToRefreshCustomRecyclerView ptrRecyclerView;
    protected CustomRecycleView recycleView;
    protected TitleBar titleBar;
    protected View dividerView;
    protected BaseRecycleAdapter adapter;


    @Override
    public View doGetContentView() {
        BaseActivityRecycleBinding binding = BaseActivityRecycleBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Nullable
    public abstract BaseRecycleAdapter doInitAdapter();

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        ptrRecyclerView = mViewQuery.findViewById(R.id.recycleView);
        titleBar = mViewQuery.findViewById(R.id.titlebar);
        dividerView = mViewQuery.findViewById(R.id.listDividerLine);
        recycleView = ptrRecyclerView.getCustomRecycleView();
        recycleView.setLoadMorePageListener(this);
        ptrRecyclerView.setRefreshListener(this);
    }

    @Override
    public void doInitData() {
        super.doInitData();
        if (adapter == null)
            adapter = doInitAdapter();
        recycleView.setAdapter(adapter);
    }


    @Override
    public void setData(List<D> results, boolean isCache) {
        viewManager.showContentView();
        if (!isCache) {
            ptrRecyclerView.onRefreshComplete();
        }
        if (adapter == null)
            adapter = doInitAdapter();
        adapter.setData(results);
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
    public void notifyDataRangeChange() {
        adapter.notifyItemChanged();
    }

    @Override
    public void isListFinish(boolean result) {
        recycleView.hideFooter(result);
    }
}
