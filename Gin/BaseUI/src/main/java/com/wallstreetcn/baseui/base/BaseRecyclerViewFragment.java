package com.wallstreetcn.baseui.base;

import android.view.View;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.adapter.BaseRecycleAdapter;
import com.wallstreetcn.baseui.customView.CustomRecycleView;
import com.wallstreetcn.baseui.customView.PullToRefreshCustomRecyclerView;
import com.wallstreetcn.baseui.databinding.BaseFragmentRecycleBinding;
import com.wallstreetcn.baseui.widget.endless.ILoadMorePageListener;
import com.wallstreetcn.baseui.widget.pulltorefresh.IRefreshListener;
import com.wallstreetcn.baseui.widget.pulltorefresh.PullToRefreshAdapterView;
import com.wallstreetcn.helper.utils.protocol.ErrorCode;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zhangyang on 16/7/8.
 */
public abstract class BaseRecyclerViewFragment<D, V, T extends BasePresenter<V>> extends BaseFragment<V, T>
        implements ILoadMorePageListener, IRefreshListener, BaseRecyclerViewCallBack<D> {

    public static Class videoControllerProxyCls = null;

    protected PullToRefreshAdapterView ptrRecyclerView;
    protected CustomRecycleView recycleView;
    protected BaseRecycleAdapter adapter;
    private RecyclerView.OnScrollListener onScrollListener;
    public IVideoController iVideoController;


    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
        if (recycleView != null) {
            recycleView.addOnScrollListener(onScrollListener);
        }
    }

    public BaseRecycleAdapter getAdapter(){
        return adapter;

    }

    private BaseFragmentRecycleBinding binding;

    @Override
    public View doGetContentView() {
        binding = BaseFragmentRecycleBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void destroyViewBinding() {
        binding = null;
    }

    public CustomRecycleView getCustomRecycleView() {
        if (ptrRecyclerView instanceof PullToRefreshCustomRecyclerView)
            return ((PullToRefreshCustomRecyclerView) ptrRecyclerView).getCustomRecycleView();
        return null;
    }

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        if (videoControllerProxyCls != null) {
            try {
                iVideoController = (IVideoController) videoControllerProxyCls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ptrRecyclerView = mViewQuery.findViewById(R.id.recycleView);
        recycleView = getCustomRecycleView();
        recycleView.setLoadMorePageListener(this);
        ptrRecyclerView.setRefreshListener(this);

        if (iVideoController != null) {
            iVideoController.bindRecycleView(recycleView);
        }

        if (adapter == null) {
            adapter = doInitAdapter();
        }
        if (onScrollListener != null && recycleView != null) {
            recycleView.clearOnScrollListeners();
            recycleView.addOnScrollListener(onScrollListener);
        }
    }

    @Nullable
    public abstract BaseRecycleAdapter doInitAdapter();

    @Override
    public void doInitData() {
        super.doInitData();
        initAdapter();
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
    public void onDetach() {
        super.onDetach();
        if (adapter != null) {
            adapter.clearData();
            adapter = null;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void setData(List<D> results, boolean isCache) {
        viewManager.showContentView();
        if (!isCache) {
            ptrRecyclerView.onRefreshComplete();
        }
        if (adapter == null) {
            adapter = doInitAdapter();
            recycleView.setAdapter(adapter);
        }
        adapter.setData(results);
    }

    @Override
    public void onResponseError(int code) {
        ptrRecyclerView.onRefreshComplete();
        recycleView.onLoadingError();
        isListFinish(true);
        if (adapter != null && adapter.getListItemCount() > 0)
            return;
        if (code == ErrorCode.EMPTYURL) {
            viewManager.showNetworkErrorView();
        } else {
            viewManager.showLoadErrorView();
        }
    }

    @Override
    public void onRefresh() {
        if (iVideoController != null)
            iVideoController.stopVideo();
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

    public void autoRefresh() {
        try {
            recycleView.scrollToPosition(0);
            ptrRecyclerView.autoRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean visible = isResumed() && !isHidden() && getUserVisibleHint();
        if (iVideoController != null)
            iVideoController.autoPlay(visible);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (iVideoController != null) {
            iVideoController.stopVideo();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        boolean visible = isResumed() && !isHidden() && getUserVisibleHint();
        if (iVideoController != null) iVideoController.autoPlay(visible);
    }

    @Override
    protected void visibleChange(boolean visible) {
        super.visibleChange(visible);
        if (iVideoController != null) iVideoController.autoPlay(visible);
    }

    public boolean isCanRefresh() {
        if (ptrRecyclerView == null)
            return false;
        return ptrRecyclerView.isCanRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (iVideoController != null)
            iVideoController.stopVideo();
    }

}
