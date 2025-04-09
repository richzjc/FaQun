package com.wallstreetcn.baseui.base;

/**
 * Created by micker on 16/6/16.
 */
public interface IViewLifeCircleInterface {

    void onStart();

    void onStop();

    void onCreate();

    void onRestart();

    void onResume();

    void onPause();

    void onDestroy();

    void setUserVisibleHint(boolean userVisibleHint);

    void onHiddenChanged(boolean isHidden);
}
