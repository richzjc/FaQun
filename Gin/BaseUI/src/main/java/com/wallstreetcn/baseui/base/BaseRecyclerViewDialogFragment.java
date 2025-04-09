package com.wallstreetcn.baseui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.richzjc.anotation_api.manager.ParameterManager;
import com.richzjc.dialoglib.base.BaseDialogFragment;
import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.adapter.BaseRecycleAdapter;
import com.wallstreetcn.baseui.customView.CustomRecycleView;
import com.wallstreetcn.baseui.customView.PullToRefreshCustomRecyclerView;
import com.wallstreetcn.baseui.databinding.BaseFragmentRecycleBinding;
import com.wallstreetcn.baseui.internal.ViewQuery;
import com.wallstreetcn.baseui.manager.ViewManager;
import com.wallstreetcn.baseui.widget.endless.ILoadMorePageListener;
import com.wallstreetcn.baseui.widget.pulltorefresh.IRefreshListener;
import com.wallstreetcn.baseui.widget.pulltorefresh.PullToRefreshAdapterView;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.data.TraceUtils;
import com.wallstreetcn.helper.utils.protocol.ErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyang on 16/7/8.
 */
public abstract class BaseRecyclerViewDialogFragment<D, V, T extends BasePresenter<V>> extends BaseDialogFragment
        implements ILoadMorePageListener, IRefreshListener, BaseRecyclerViewCallBack<D> {

    public static Class videoControllerProxyCls = null;

    protected PullToRefreshAdapterView ptrRecyclerView;
    protected CustomRecycleView recycleView;
    protected BaseRecycleAdapter adapter;
    private RecyclerView.OnScrollListener onScrollListener;
    public IVideoController iVideoController;


    protected List<IViewLifeCircleInterface> mILifeCircles = new ArrayList<>();
    protected ViewQuery mViewQuery = new ViewQuery();
    protected T mPresenter;
    protected ViewManager viewManager;
    private boolean isLoadData = false;
    private String TAG = "BaseFragment";

    protected boolean isVisibleToUser = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mILifeCircles != null && !mILifeCircles.isEmpty()) {
            for (IViewLifeCircleInterface callback : mILifeCircles) {
                callback.onCreate();
            }
        }
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        try {
            //主要是为了避免在状态保存后 在执行下面的 这个方法会抛异常
            super.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParameterManager.getInstance().loadParameter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lazyLoadAction();
        doAfter();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewManager = new ViewManager(getActivity(), doGetContentView(container, inflater), container);
        doInitPresenter();
        attachToPresenter();
        doBefore(savedInstanceState);
        View containerView = getRealContentView();
        mViewQuery.setView(containerView);
        doInitSubViews(containerView);
        return containerView;
    }

    protected View getRealContentView() {
        return viewManager.showContentView();
    }

    private void doInitPresenter() {
        if (mPresenter == null) {
            mPresenter = doGetPresenter();
        }
    }

    protected T doGetPresenter() {
        return null;
    }


    @Override
    public ViewGroup doViewGroupRoot() {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mILifeCircles != null && !mILifeCircles.isEmpty()) {
            for (IViewLifeCircleInterface callback : mILifeCircles) {
                callback.onStart();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mILifeCircles != null && !mILifeCircles.isEmpty()) {
            for (IViewLifeCircleInterface callback : mILifeCircles) {
                callback.onStop();
            }
        }
    }

    public void setILifeCircle(IViewLifeCircleInterface mILifeCircle) {
        this.mILifeCircles.add(mILifeCircle);
    }

    public final void setOldUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    private void lazyLoadAction() {
        if (!isLoadData) {
            if (isAdded() && isVisibleToUser) {
                doInitPresenter();
                doInitData();
                isLoadData = true;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        TLog.i("onHiddenChanged", getClass().getSimpleName() + String.valueOf(hidden));
        if (mILifeCircles != null && !mILifeCircles.isEmpty()) {
            for (IViewLifeCircleInterface callback : mILifeCircles) {
                callback.onHiddenChanged(hidden);
            }
        }
        notifyStateChange();
    }

    private boolean islec_VisibleToUser = false;

    public void notifyStateChange() {
        boolean visible = le_visibleToUser();
        if (visible == islec_VisibleToUser) return;
        islec_VisibleToUser = visible;
        visibleChange(islec_VisibleToUser);
    }

    public boolean le_visibleToUser() {
        boolean parentVisible = true;
        if (getParentFragment() instanceof BaseFragment) {
            parentVisible = ((BaseFragment) getParentFragment()).le_visibleToUser();
        }
        return parentVisible && isResumed() && getUserVisibleHint() && !isHidden();
    }

    protected void attachToPresenter() {
        if (null != mPresenter) {
            if (!mPresenter.isViewRefAttached()) {
                mPresenter.attachViewRef((V) this);
            }
        }
    }

    protected void detachToPresenter() {
        if (null != mPresenter) {
            if (mPresenter.isViewRefAttached()) {
                mPresenter.detachViewRef();
            }
        }
    }

    public void onEmptyClick() {

    }

    public boolean onBackPressed() {
        return false;
    }

    public void showDialog() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            ((BaseActivity) activity).showDialog();
        }
    }

    public void dismissDialog() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            ((BaseActivity) activity).dismissDialog();
        }
    }


    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
        if (recycleView != null) {
            recycleView.addOnScrollListener(onScrollListener);
        }
    }

    public BaseRecycleAdapter getAdapter() {
        return adapter;
    }


    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getStyle() {
        return com.wallstreetcn.baseui.R.style.DefaultDialog;
    }

    private BaseFragmentRecycleBinding binding;

    @Override
    public View doGetContentView(ViewGroup container, LayoutInflater inflater) {
        binding = BaseFragmentRecycleBinding.inflate(inflater, container, false);
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

        ViewGroup.LayoutParams params = recycleView.getLayoutParams();
        if(params != null){
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            recycleView.setLayoutParams(params);
        }

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
        detachToPresenter();
        isLoadData = false;

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
        TraceUtils.onPageStart(getClass().getCanonicalName());
        if (mILifeCircles != null && !mILifeCircles.isEmpty()) {
            for (IViewLifeCircleInterface callback : mILifeCircles) {
                callback.onResume();
            }
        }
        notifyStateChange();

        boolean visible = isResumed() && !isHidden() && getUserVisibleHint();
        if (iVideoController != null)
            iVideoController.autoPlay(visible);
    }

    @Override
    public void onPause() {
        super.onPause();
        TraceUtils.onPageEnd(getClass().getCanonicalName());
        if (mILifeCircles != null && !mILifeCircles.isEmpty()) {
            for (IViewLifeCircleInterface callback : mILifeCircles) {
                callback.onPause();
            }
        }
        notifyStateChange();

        if (iVideoController != null) {
            iVideoController.stopVideo();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setOldUserVisibleHint(isVisibleToUser);
        if (mILifeCircles != null && !mILifeCircles.isEmpty()) {
            for (IViewLifeCircleInterface callback : mILifeCircles) {
                callback.setUserVisibleHint(isVisibleToUser);
            }
        }
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            lazyLoadAction();
        } else if (mPresenter != null) {
            dismiss();
        }
        notifyStateChange();

        boolean visible = isResumed() && !isHidden() && getUserVisibleHint();
        if (iVideoController != null) iVideoController.autoPlay(visible);
    }

    protected void visibleChange(boolean visible) {
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
