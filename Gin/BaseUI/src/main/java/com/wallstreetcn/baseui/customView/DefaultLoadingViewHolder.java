package com.wallstreetcn.baseui.customView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.callback.IViewHolder;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by Leif Zhang on 2016/12/9.
 * Email leifzhanggithub@gmail.com
 */

public class DefaultLoadingViewHolder implements IViewHolder {
    private View view;

    public DefaultLoadingViewHolder(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_fragment_dialog_loading, parent, false);
        view.setLayoutParams(new StaggeredGridLayoutManager.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public View getView() {
        return view;
    }
}
