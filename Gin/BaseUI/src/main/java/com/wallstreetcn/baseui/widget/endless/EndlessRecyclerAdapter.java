package com.wallstreetcn.baseui.widget.endless;

import com.wallstreetcn.helper.utils.TLog;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.wallstreetcn.baseui.adapter.BaseRecycleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-26
 * Time: 18:23
 */
public class EndlessRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FOOTERS_START = Integer.MIN_VALUE + 10;
    private static final int ITEMS_START = Integer.MIN_VALUE + 20;
    private static final int ADAPTER_MAX_TYPES = 100;
    private StaticViewHolder staticViewHolder;

    private BaseRecycleAdapter mWrappedAdapter;
    private List<View> mFooterViews;
    private Map<Class, Integer> mItemTypesOffset;

    public EndlessRecyclerAdapter(BaseRecycleAdapter adapter) {
        mFooterViews = new ArrayList<>();
        mItemTypesOffset = new HashMap<>();
        setWrappedAdapter(adapter);
    }

    public BaseRecycleAdapter getmWrappedAdapter() {
        return mWrappedAdapter;
    }

    public void setAdapter(BaseRecycleAdapter adapter) {
        if (mWrappedAdapter != null && mWrappedAdapter.getItemCount() > 0) {
            notifyItemRangeRemoved(0, mWrappedAdapter.getItemCount());
        }
        setWrappedAdapter(adapter);
        notifyItemRangeInserted(0, mWrappedAdapter.getItemCount());
    }

    public boolean isRealLoadMore() {
        if (mWrappedAdapter != null) {
            return !mWrappedAdapter.dataIsNullOrEmpty();
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int itemCount = mWrappedAdapter.getItemCount();
        if (position < itemCount) {
            return getAdapterTypeOffset() + mWrappedAdapter.getItemViewType(position);
        } else {
            return FOOTERS_START + position - itemCount;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType < FOOTERS_START + getFooterCount()) {
            if(staticViewHolder == null){
                staticViewHolder = new StaticViewHolder(mFooterViews.get(viewType - FOOTERS_START));
            }
            if(staticViewHolder.itemView.getParent() != null){
                ViewParent parent = staticViewHolder.itemView.getParent();
                if(parent instanceof ViewGroup)
                    ((ViewGroup) parent).removeView(staticViewHolder.itemView);
            }
            return staticViewHolder;
        } else {
            return mWrappedAdapter.onCreateViewHolder(viewGroup, viewType - getAdapterTypeOffset());
        }
    }

    public int getHeadCount() {
        return mWrappedAdapter.getHeadViewSize();
    }

    public boolean isWrapEmpty() {
        return mWrappedAdapter.isEmpty();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        mWrappedAdapter.onBindViewHolder(viewHolder, position);
        if (viewHolder instanceof StaticViewHolder) {
            ((StaticViewHolder) viewHolder).setFullSpan();
        }
    }


    public void addFooterView(View view) {
        mFooterViews.add(view);
        notifyItemInserted(mWrappedAdapter.getItemCount());
    }

    public void removeFooterView(View view) {
        mFooterViews.remove(view);
        notifyItemRemoved(mWrappedAdapter.getItemCount() + mFooterViews.size());
        notifyItemChanged(mWrappedAdapter.getItemCount());
    }

    @Override
    public int getItemCount() {
        if (mWrappedAdapter.isEmpty()) {
            return getWrappedItemCount();
        }
        return getFooterCount() + getWrappedItemCount();
    }

    public int getWrappedItemCount() {
        return mWrappedAdapter.getItemCount();
    }


    public int getFooterCount() {
        return mFooterViews.size();
    }


    private void setWrappedAdapter(BaseRecycleAdapter adapter) {
        if (mWrappedAdapter != null) mWrappedAdapter.unregisterAdapterDataObserver(mDataObserver);
        mWrappedAdapter = adapter;
        Class adapterClass = mWrappedAdapter.getClass();
        if (!mItemTypesOffset.containsKey(adapterClass)) putAdapterTypeOffset(adapterClass);
        mWrappedAdapter.registerAdapterDataObserver(mDataObserver);
    }


    private void putAdapterTypeOffset(Class adapterClass) {
        mItemTypesOffset.put(adapterClass, ITEMS_START + mItemTypesOffset.size() * ADAPTER_MAX_TYPES);
    }


    private int getAdapterTypeOffset() {
        return mItemTypesOffset.get(mWrappedAdapter.getClass());
    }


    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart, itemCount);
        }


        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart, itemCount);
        }


        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart, itemCount);
        }


        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemRangeChanged(fromPosition, toPosition + itemCount);
        }
    };

    private static class StaticViewHolder extends RecyclerView.ViewHolder {
        StaticViewHolder(View itemView) {
            super(itemView);
        }

        protected void setFullSpan() {
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    public boolean isEmptyView() {
        return (mWrappedAdapter.getListItemCount() > 0);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (mWrappedAdapter != null) {
            mWrappedAdapter.onViewAttachedToWindow(holder);
        }
    }
}
