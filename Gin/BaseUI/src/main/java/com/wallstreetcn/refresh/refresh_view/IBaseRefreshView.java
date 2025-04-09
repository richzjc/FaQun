package com.wallstreetcn.refresh.refresh_view;

import android.graphics.drawable.Animatable;

public interface IBaseRefreshView extends Animatable {

    void setPercent(float percent, boolean invalidate);

    void offsetTopAndBottom(int offset);
}
