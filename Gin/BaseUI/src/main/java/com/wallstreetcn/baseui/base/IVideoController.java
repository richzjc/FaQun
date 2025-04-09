package com.wallstreetcn.baseui.base;

import com.wallstreetcn.baseui.customView.CustomRecycleView;

public interface IVideoController {
    void bindRecycleView(CustomRecycleView recycleView);
    void autoPlay(boolean visible);
    void stopVideo();
}
