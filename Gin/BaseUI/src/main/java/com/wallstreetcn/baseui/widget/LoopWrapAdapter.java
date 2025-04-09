package com.wallstreetcn.baseui.widget;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by  Leif Zhang on 2018/5/2.
 * Email leifzhanggithub@gmail.com
 */

public class LoopWrapAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private RecyclerView.Adapter<VH> mAdapter;

    public LoopWrapAdapter(RecyclerView.Adapter<VH> adapter) {
        this.mAdapter = adapter;
        this.setHasStableIds(this.mAdapter.hasStableIds());
        adapter.registerAdapterDataObserver(mDataObserver);
    }


    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return this.mAdapter.onCreateViewHolder(parent, viewType);
    }

    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        this.mAdapter.registerAdapterDataObserver(observer);
    }

    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        this.mAdapter.unregisterAdapterDataObserver(observer);
    }

    public void onViewRecycled(@NonNull VH holder) {
        super.onViewRecycled(holder);
        this.mAdapter.onViewRecycled(holder);
    }

    public boolean onFailedToRecycleView(@NonNull VH holder) {
        return this.mAdapter.onFailedToRecycleView(holder);
    }

    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
        this.mAdapter.onViewAttachedToWindow(holder);
    }

    public void onViewDetachedFromWindow(@NonNull VH holder) {
        super.onViewDetachedFromWindow(holder);
        this.mAdapter.onViewDetachedFromWindow(holder);
    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    public void onBindViewHolder(@NonNull VH holder, int position) {
        this.mAdapter.onBindViewHolder(holder, getActualPosition(position));
    }

    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public int getItemViewType(int position) {
        return this.mAdapter.getItemViewType(getActualPosition(position));
    }

    public long getItemId(int position) {
        return this.mAdapter.getItemId(getActualPosition(position)) + position;
    }


    public int getActualItemCount() {
        return this.mAdapter.getItemCount();
    }

    public int getActualPosition(int position) {
        int actualPosition = position;
        if (position >= this.getActualItemCount() && getActualItemCount() != 0) {
            actualPosition = position % this.getActualItemCount();
        }
        return actualPosition;
    }

    public int getMiddlePosition() {
        int middleItem = getItemCount() / 2;
        if (getActualItemCount() == 0) {
            return middleItem;
        }
        int actualCurrentPosition = middleItem % getActualItemCount();
        return middleItem - actualCurrentPosition;
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
}
