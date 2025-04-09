package com.richzjc.dialoglib.callback;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by micker on 16/6/16.
 */
public interface ICreateViewInterface {

    /***
     * 创建事前处理
     * @param savedInstanceState
     */
    public void doBefore(Bundle savedInstanceState);

    /***
     * 视图组
     * @return
     */
    public ViewGroup doViewGroupRoot();


    /***
     * 初始化子视图，在此做控件、事件的绑定操作
     * @param view
     */
    public void doInitSubViews(View view);

    /***
     * 对视图中的控件进行数据初始化
     */
    public void doInitData();

    /***
     * 做事件的最终处理
     */
    public void doAfter();

}
