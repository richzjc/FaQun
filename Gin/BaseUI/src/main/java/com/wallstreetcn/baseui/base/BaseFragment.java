package com.wallstreetcn.baseui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.richzjc.anotation_api.manager.ParameterManager;
import com.wallstreetcn.baseui.internal.ViewQuery;
import com.wallstreetcn.baseui.manager.ViewManager;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.data.TraceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micker on 16/6/16.
 */
public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment implements
        ICreateViewInterface, View.OnClickListener {

    protected ViewQuery mViewQuery = new ViewQuery();
    protected T mPresenter;
    protected ViewManager viewManager;
    private boolean isLoadData = false;
    private String TAG = "BaseFragment";

    protected boolean isVisibleToUser = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TLog.e("createFragment", getClass().getSimpleName());
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
        registerLifeCycle();
        doAfter();
    }

    private void registerLifeCycle() {
        getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onCreate(owner);
            }

            @Override
            public void onStart(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onStart(owner);
            }

            @Override
            public void onResume(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onResume(owner);
                setUserVisible(true);
            }

            @Override
            public void onPause(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onPause(owner);
                setUserVisible(false);
            }

            @Override
            public void onStop(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onStop(owner);
            }

            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onDestroy(owner);
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewManager = new ViewManager(getActivity(), doGetContentView(), container);
        doInitPresenter();
        attachToPresenter();
        doBefore(savedInstanceState);
        View containerView = getRealContentView();
        __internal(containerView);
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

    private void __internal(View view) {
        mViewQuery.setView(view).setClickListener(this);
    }

    @Override
    public void doBefore(Bundle savedInstanceState) {

    }

    @Override
    public ViewGroup doViewGroupRoot() {
        return null;
    }


    @Override
    public void doInitSubViews(View view) {

    }

    @Override
    public void doInitData() {

    }

    @Override
    public void doAfter() {

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onResume() {
        super.onResume();
        TraceUtils.onPageStart(getClass().getCanonicalName());
        notifyStateChange();
    }

    @Override
    public void onPause() {
        super.onPause();
        TraceUtils.onPageEnd(getClass().getCanonicalName());
        notifyStateChange();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        detachToPresenter();
        isLoadData = false;
    }


    private void setUserVisible(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            lazyLoadAction();
        } else if (mPresenter != null) {
            dismiss();
        }
        notifyStateChange();
    }

    private void lazyLoadAction() {
        if (!isLoadData) {
            if (isAdded() && isVisibleToUser) {
                TLog.e("lazyLoad", getClass().getName());
                doInitPresenter();
                doInitData();
                isLoadData = true;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        notifyStateChange();
    }

    private boolean islec_VisibleToUser = false;

    public void notifyStateChange() {
        boolean visible = le_visibleToUser();
        if (visible == islec_VisibleToUser) return;
        islec_VisibleToUser = visible;
        visibleChange(islec_VisibleToUser);
    }

    protected void visibleChange(boolean visible) {

    }

    public boolean le_visibleToUser() {
        boolean parentVisible = true;
        if (getParentFragment() instanceof BaseFragment) {
            parentVisible = ((BaseFragment) getParentFragment()).le_visibleToUser();
        }
        return parentVisible && isResumed() && getUserVisibleHint() && !isHidden();
    }

    public void dismiss() {

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

    public abstract void destroyViewBinding();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyViewBinding();
    }
}
