package com.wallstreetcn.baseui.base;

import android.view.View;

import com.wallstreetcn.baseui.widget.pulltorefresh.IRefreshListener;

/**
 * Created by Leif Zhang on 2016/10/14.
 * Email leifzhanggithub@gmail.com
 */
public class BaseTabFragment<V, T extends BasePresenter<V>> extends BaseFragment<V, T>
        implements IRefreshListener {


    @Override
    public void onRefresh() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void destroyViewBinding() {

    }

    @Override
    public View doGetContentView() {
        return null;
    }
}
