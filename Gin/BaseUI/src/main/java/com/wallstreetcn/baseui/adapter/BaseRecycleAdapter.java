package com.wallstreetcn.baseui.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import com.wallstreetcn.helper.utils.TLog;

import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micker on 16/6/17.
 */
public abstract class BaseRecycleAdapter<D, T extends RecyclerView.ViewHolder> extends BaseAnimationAdapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_HEADER_START = 1000;
    protected static final int TYPE_FOOTER = 2000;
    protected static final int TYPE_EMPTY_START = 3000;
    protected static final int TYPE_LOADING = 4000;

    private List<View> mHeaderViews = new ArrayList<>();
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;
    private View mLoadingView;
    private AdapterDataDelegate<D> delegate;
    protected String editTextColor;
    private boolean needSkeleton;

    public BaseRecycleAdapter() {
        super();
        delegate = new AdapterDataDelegate<>(this);
        setHasStableIds(true);
    }

    public void setNeedSkeleton(boolean needSkeleton) {
        this.needSkeleton = needSkeleton;
        delegate.setNeedSkeleton(needSkeleton);
    }

    public void setSkeletonCount(int count) {
        delegate.setSkeletonCount(count);
    }

    @Override
    protected Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0).setDuration(200)
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof HeaderHolder) {
            return;
        } else if (holder instanceof FooterHolder) {

            return;
        } else if (holder instanceof EmptyHolder) {
            return;
        }
        if (holder instanceof BaseRecycleViewHolder) {
            try {
                if (isLoadSketon()) {
                    ((BaseRecycleViewHolder) holder).loadSkeletonLayout(delegate.getSkeletonEntity(position));
                } else {
                    binderItemHolder((T) holder, getListItemPosition(position));
                }
                ((LifecycleRegistry) ((BaseRecycleViewHolder) holder).getLifecycle()).handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean isLoadSketon() {
        return delegate.isEmpty();
    }

    public boolean dataIsNullOrEmpty() {
        return delegate.isEmpty() || delegate.isListEmpty();
    }

    public void clearData() {
        mHeaderViews.clear();
        delegate.clear();
        mFooterView = null;
        notifyDataSetChanged();
    }

    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        notifyDataSetChanged();
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        notifyDataSetChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER_START) {
            View v = mHeaderView;
            return new HeaderHolder(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = mFooterView;
            return new FooterHolder(v);
        } else if (viewType == TYPE_EMPTY_START) {
            View v = mEmptyView;
            return new EmptyHolder(v);
        } else if (viewType == TYPE_LOADING) {
            View v = mLoadingView;
            return new EmptyHolder(v);
        }
        T holder = createListItemView(parent, viewType);
        if (holder instanceof BaseRecycleViewHolder)
            ((LifecycleRegistry) ((BaseRecycleViewHolder) holder).getLifecycle()).handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        return holder;
    }

    @Override
    public int getItemCount() {
        int size = getListItemCount();
        // int headerFooterCount = getHeadViewSize() + getFooterViewSize();
        if (size == 0 && delegate.isEmpty() && !needSkeleton && null != mLoadingView) {
            return 1;
        }
        if (size == 0 && delegate.isListEmpty() && null != mEmptyView) {
            return 1 + getHeadViewSize() + getFooterViewSize();
        } else {
            return getHeadViewSize() + size + getFooterViewSize();
        }
    }

    @Override
    public final int getItemViewType(int position) {
        int size = getListItemCount();
        if (position < getHeadViewSize()) {
            mHeaderView = mHeaderViews.get(position);
            return TYPE_HEADER_START;
        } else if (delegate.isEmpty() && null != mLoadingView && !needSkeleton) {
            TLog.i("TYPE_LOADING", "TYPE_LOADING");
            return TYPE_LOADING;
        } else if (size == 0 && delegate.isListEmpty() && null != mEmptyView) {
            return TYPE_EMPTY_START;
        } else if (position >= getHeadViewSize() + getListItemCount()) {
            return TYPE_FOOTER;
        }
        try {
            return getListType(getListItemPosition(position));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getListType(int listItemPosition) {
        return 0;
    }


    public void setData(List<D> data) {
        delegate.setData(data);
    }

    public void setDataNew(List<D> data) {
        delegate.setDataNew(data);
    }

    public void onlySetData(List<D> data) {
        delegate.onlySetData(data);
    }

    public void notifyItemChanged() {
        delegate.notifyItemChanged();
    }

    public void onItemMove(int lastPos, int nowPos) {
        delegate.onItemMove(lastPos, nowPos);
    }


    public D getItemAtPosition(int pos) {
        if (pos < getHeadViewSize()) {
            return null;
        }
        if (pos >= (getHeadViewSize() + getListItemCount())) {
            return null;
        }
        if (delegate.isEmpty()) {
            return null;
        }

        return delegate.getData(getListItemPosition(pos));
    }

    @Override
    public long getItemId(int position) {
        return Integer.MAX_VALUE + position;
    }

    public int getHeadViewSize() {
        return mHeaderViews.size();
    }

    public int getFooterViewSize() {
        return mFooterView == null ? 0 : 1;
    }

    public int getListItemCount() {
        return delegate.getListSize();
    }

    public int getListItemPosition(int pos) {
        return pos - getHeadViewSize();
    }

    public abstract void binderItemHolder(T holder, int position);

    public abstract T createListItemView(ViewGroup parent, int viewType);


    //add a header to the adapter
    public void addHeader(View header) {
        TLog.i("addHeader", String.valueOf(mHeaderViews.contains(header)));
        if (!mHeaderViews.contains(header)) {
            mHeaderViews.add(header);
            notifyItemInserted(mHeaderViews.size() - 1);
        }
    }

    //remove a header from the adapter
    public void removeHeader(View header) {
        if (mHeaderViews.contains(header)) {
            notifyItemRemoved(mHeaderViews.indexOf(header));
            mHeaderViews.remove(header);
        }
    }

    public void removeAllHeader() {
        //  notifyItemRangeRemoved(0, mHeaderViews.size());
        mHeaderViews.clear();
        notifyDataSetChanged();
    }

    //add a footer to the adapter
    public void addFooter(View footer) {
        mFooterView = footer;
        notifyItemInserted(getItemCount() + 1);
        // notifyDataSetChanged();
    }

    //remove a footer from the adapter
    public void removeFooter() {
        mFooterView = null;
        notifyDataSetChanged();
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {

        HeaderHolder(View itemView) {
            super(itemView);
            setFullSpan();
        }

        void setFullSpan() {
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    private static class FooterHolder extends RecyclerView.ViewHolder {
        FooterHolder(View itemView) {
            super(itemView);
            setFullSpan();
        }

        void setFullSpan() {
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    protected static class EmptyHolder extends RecyclerView.ViewHolder {
        EmptyHolder(View itemView) {
            super(itemView);
            setFullSpan();
        }

        void setFullSpan() {
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    public void onAdapterItemClick(View view, RecyclerView.ViewHolder holder) {
        try {
            if (adapterItemClickListener != null) {
                adapterItemClickListener.onViewClick(view, getItemAtPosition(holder.getAdapterPosition()),
                        holder.getAdapterPosition() - getHeadViewSize());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof BaseRecycleViewHolder) {
            ((BaseRecycleViewHolder) holder).onDetachView();
            ((LifecycleRegistry) ((BaseRecycleViewHolder) holder).getLifecycle()).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        }
    }

    protected AdapterItemClickListener<D> adapterItemClickListener;

    public void setAdapterItemClickListener(AdapterItemClickListener<D> adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    public interface AdapterItemClickListener<D> {
        void onViewClick(View view, D entity, int position);
    }

    public List<D> get() {
        return delegate.getData();
    }

    public void setEditTextColor(String editTextColor) {
        this.editTextColor = editTextColor;
    }

    public boolean isEmpty() {
        return delegate.isEmpty() || delegate.isListEmpty();
    }


    public D get(int pos) {
        return delegate.getData(pos);
    }


    public void removeEntity(D item) {
        delegate.removeItem(item);
    }

    public void removeEntity(int position) {
        List<D> mDataCursor = delegate.getData();
        if (mDataCursor != null && position >= 0 && position < mDataCursor.size()) {
            mDataCursor.remove(position);
            notifyItemChanged();
        }
    }

    public void diffDetectMoves(boolean diffDetectMoves) {
        delegate.diffDetectMoves(diffDetectMoves);
    }
}