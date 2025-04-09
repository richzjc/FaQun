package com.wallstreetcn.refresh.refresh_view;

import android.content.Context;
import android.widget.ImageView;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.refresh.PullToRefreshView;

public class SunRefreshView extends ImageView implements IBaseRefreshView {
    private SunRefreshDrawable sunRefreshDrawable;

    public SunRefreshView(PullToRefreshView view) {
        super(view.getContext());
        sunRefreshDrawable = new SunRefreshDrawable(view);
        sunRefreshDrawable.setBounds(0, 0, sunRefreshDrawable.getIntrinsicWidth(), sunRefreshDrawable.getIntrinsicHeight());
        setBackground(sunRefreshDrawable);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        sunRefreshDrawable.setPercent(percent, invalidate);
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        super.offsetTopAndBottom(offset);
        sunRefreshDrawable.offsetTopAndBottom(offset);
    }

    @Override
    public void start() {
        sunRefreshDrawable.start();
    }

    @Override
    public void stop() {
        sunRefreshDrawable.stop();
    }

    @Override
    public boolean isRunning() {
        return sunRefreshDrawable.isRunning();
    }
}
