package com.wallstreetcn.baseui.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wallstreetcn.baseui.customView.DefaultLoadErrorViewHolder;
import com.wallstreetcn.baseui.customView.DefaultLoadingViewHolder;
import com.wallstreetcn.baseui.customView.DefaultNetworkErrorViewHolder;
import com.wallstreetcn.helper.utils.protocol.ErrorCode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by wscn on 17/3/23.
 */

public class ViewManager implements View.OnClickListener {

    private View contentView;
    private View networkView;
    private View loadingView;
    private RelativeLayout parentView;

    private View.OnClickListener netErrorListener;
    private RelativeLayout.LayoutParams params;
    private Context context;
    private ViewGroup container;

    public ViewManager(Context context, View contentView) {
        this.context = context;
        this.contentView = contentView;
        parentView = new RelativeLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parentView.setLayoutParams(layoutParams);
    }

    public ViewManager(Context context, View contentView, ViewGroup container) {
        this.context = context;
        this.contentView = contentView;
        this.container = container;
        parentView = new RelativeLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parentView.setLayoutParams(layoutParams);
    }

    public View showNetworkErrorView() {
        return showNetworkErrorView(ErrorCode.EMPTYURL);
    }

    public View showNetworkErrorView(int code) {
        if (!deleteParentView(ViewTag.NETWORK_VIEW)) {
            DefaultNetworkErrorViewHolder holder = new DefaultNetworkErrorViewHolder(parentView);
            if (code == ErrorCode.ERROR_404) {
                networkView = holder.get404ErrorView();
            } else {
                networkView = holder.getNetWorkErrorView();
                networkView.setOnClickListener(this);
            }
            networkView.setTag(ViewTag.NETWORK_VIEW);
            parentView.addView(networkView, getParams());
        }
        return parentView;
    }

    private boolean deleteParentView(int viewTag) {
        int count = parentView.getChildCount();
        boolean value = false;
        if (count > 0) {
            View view;
            for (int i = 0; i < count; i++) {
                view = parentView.getChildAt(i);
                if (view != null) {
                    int tag = (Integer) view.getTag();
                    if (viewTag == tag) {
                        value = true;
                        view.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.GONE);
                    }
                }
            }
        }
        return value;
    }

    private RelativeLayout.LayoutParams getParams() {
        if (params == null)
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        return params;
    }

    public View showLoadErrorView() {
        if (!deleteParentView(ViewTag.LOAD_ERROR_VIEW)) {
            DefaultLoadErrorViewHolder holder = new DefaultLoadErrorViewHolder(parentView);
            View loadErrorView = holder.getView();
            loadErrorView.setTag(ViewTag.LOAD_ERROR_VIEW);
            parentView.addView(loadErrorView, getParams());
        }
        return parentView;
    }

    public View showContentView() {
        if (!deleteParentView(ViewTag.CONTENT_VIEW)) {
            contentView.setTag(ViewTag.CONTENT_VIEW);
            ViewGroup parent = (ViewGroup) contentView.getParent();
            if (parent != null) {
                parent.removeView(contentView);
            }
            parentView.addView(contentView, getParams());
        }

        return parentView;
    }

    public View getWithOutParentView() {
        if (contentView != null) {
            contentView.setTag(ViewTag.CONTENT_VIEW);
        }
        return contentView;
    }

    public void setNetErrorListener(View.OnClickListener listener) {
        this.netErrorListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == networkView && netErrorListener != null) {
            addLoadingView();
            netErrorListener.onClick(networkView);
        }
    }

    public void addLoadingView() {
        int count = parentView.getChildCount();
        if (count > 0) {
            View view;
            for (int i = 0; i < count; i++) {
                view = parentView.getChildAt(i);
                int tag = (Integer) view.getTag();
                if (ViewTag.LOADING_VIEW == tag) {
                    loadingView = view;
                    parentView.removeView(view);
                    break;
                }
            }
        }

        if (loadingView == null) {
            DefaultLoadingViewHolder holder = new DefaultLoadingViewHolder(parentView);
            loadingView = holder.getView();
            loadingView.setTag(ViewTag.LOADING_VIEW);
        }
        loadingView.setVisibility(View.VISIBLE);
        ViewGroup parent = (ViewGroup) loadingView.getParent();
        if (parent != null) {
            parent.removeView(loadingView);
        }
        parentView.addView(loadingView, getParams());
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            ViewTag.CONTENT_VIEW, ViewTag.LOADING_VIEW, ViewTag.NETWORK_VIEW, ViewTag.LOAD_ERROR_VIEW
    })
    public @interface ViewTag {
        int CONTENT_VIEW = 0;
        int LOADING_VIEW = 1;
        int NETWORK_VIEW = 2;
        int LOAD_ERROR_VIEW = 3;
    }
}
