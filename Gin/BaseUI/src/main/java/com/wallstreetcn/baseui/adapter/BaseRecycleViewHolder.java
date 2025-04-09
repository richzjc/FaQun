package com.wallstreetcn.baseui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallstreetcn.baseui.internal.ViewQuery;
import com.wallstreetcn.baseui.model.SkeletonEntity;
import com.wallstreetcn.baseui.widget.RecyclerViewLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by micker on 16/6/17.
 */
public abstract class BaseRecycleViewHolder<T> extends RecyclerView.ViewHolder implements LifecycleOwner {

    protected T content;
    protected ViewQuery mViewQuery;
    protected Context mContext;
    protected View actualItemView;
    protected View skeletonItemView;
    protected RecyclerViewLayout itemContainer;
    private LifecycleRegistry mLifecycleRegistry;

    protected BaseRecycleViewHolder(View itemView) {
        super(itemView);
        setViewClickListener();
    }

    public BaseRecycleViewHolder(Context context) {
        super(new RecyclerViewLayout(context));
        mContext = context;
        itemContainer = (RecyclerViewLayout) itemView;
        View view = getHolderView(LayoutInflater.from(context), itemContainer);
        if (view != null && itemContainer.getChildCount() == 0) {
            actualItemView = view;
            itemContainer.addView(actualItemView);
        }

        if (actualItemView != null) {
            ViewGroup.LayoutParams params = actualItemView.getLayoutParams();
            ViewGroup.MarginLayoutParams itemParams = new ViewGroup.MarginLayoutParams(params.width,
                    params.height);
            itemContainer.setLayoutParams(itemParams);
            mViewQuery = new ViewQuery();
            mViewQuery.setView(itemContainer);
            setViewClickListener();
            doBindView(actualItemView);
        }
    }

    protected void setFullSpan() {
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public abstract View getHolderView(LayoutInflater inflater, RecyclerViewLayout itemContainer);


    public void setViewClickListener(){

    }

    public int getSkeletonLayoutId() {
        return 0;
    }

    public void loadSkeletonLayout(SkeletonEntity entity) {
        if (getSkeletonLayoutId() != 0) {
            actualItemView.setVisibility(View.GONE);
            skeletonItemView = LayoutInflater.from(mContext).inflate(getSkeletonLayoutId(), itemContainer, false);
            itemContainer.addView(skeletonItemView);
        } else {
            skeletonItemView = actualItemView;
            actualItemView.setVisibility(View.VISIBLE);
        }
        if (entity != null) {
            entity.setContext(mContext);
            bindSkeletonData(entity);
        }
    }

    protected void bindSkeletonData(SkeletonEntity entity) {

    }

    protected void doBindView(View itemView) {

    }


    public abstract void doBindData(T content);

    public void doBindData(T content, Object params) {

    }

    public T getContent() {
        return content;
    }

    public void onDetachView() {

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("V2     holderName = ")
                .append(getClass().getSimpleName())
                .append("; holderView = ")
                .append(itemView.getClass().getSimpleName()).append("; holderItemViewType = ").append(getItemViewType());
        return builder.toString();
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        if (mLifecycleRegistry == null)
            mLifecycleRegistry = new LifecycleRegistry(this);
        return mLifecycleRegistry;
    }
}
