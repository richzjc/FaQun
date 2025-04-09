package com.wallstreetcn.baseui.base;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by zhangjianchuan on 2016/6/27.
 */
public class BasePresenter<T extends Object> {
    protected Reference<T> mViewRef;

    public void attachViewRef(T view) {
        if (mViewRef == null) {
            mViewRef = new WeakReference<>(view);
        }
    }

    public T getViewRef() {
        if (null != mViewRef) {
            return mViewRef.get();
        }
        return null;
    }

    public boolean isViewRefAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachViewRef() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    protected Lifecycle getLifecycle() {
        if(mViewRef.get() != null && mViewRef.get() instanceof  LifecycleOwner)
            return ((LifecycleOwner) mViewRef.get()).getLifecycle();
        return null;
    }
}
