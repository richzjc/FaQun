package com.wallstreetcn.baseui.callback;

import com.richzjc.dialog_view.IAnimView;

import java.util.List;

public interface IRegistGetAnimView {

    void regist(IAnimView iAnimView);
    List<IAnimView> getAnimViews();
}
