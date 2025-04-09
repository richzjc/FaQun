package com.wallstreetcn.refresh.refresh_view;

import android.content.Context;
import android.util.AttributeSet;

import com.wallstreetcn.baseui.widget.pulltorefresh.SmartRefreshHeaderView;

public class WscnRefreshView extends SmartRefreshHeaderView implements IBaseRefreshView {
    public WscnRefreshView(Context context) {
        super(context);
    }


    @Override
    public void setPercent(float percent, boolean invalidate) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}