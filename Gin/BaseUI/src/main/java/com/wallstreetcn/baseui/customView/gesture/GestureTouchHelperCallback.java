package com.wallstreetcn.baseui.customView.gesture;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static com.wallstreetcn.baseui.customView.gesture.LayoutFlags.GRID;
import static com.wallstreetcn.baseui.customView.gesture.LayoutFlags.LINEAR;
import static com.wallstreetcn.baseui.customView.gesture.LayoutFlags.STAGGERED;


/**
 * Touch helper callback that handles different RecycleView gestures.
 *
 * @author thesurix
 */
public class GestureTouchHelperCallback extends ItemTouchHelper.Callback {

    /**
     * GestureAdapter instance for touch callbacks
     */
    private final GestureAdapter mGestureAdapter;

    /**
     * Flag that enables or disables swipe gesture
     */
    private boolean mIsSwipeEnabled;
    /**
     * Flag that enables or disables manual drag gesture
     */
    private boolean mIsManualDragEnabled;
    /**
     * Flag that enables long press drag gesture
     */
    private boolean mIsLongPressDragEnabled;

    /**
     * Flags for drag gesture
     */
    private int mDragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    /**
     * Flags for swipe gesture
     */
    private int mSwipeFlags = ItemTouchHelper.RIGHT;

    /**
     * Constructs callback object based on passed adapter.
     *
     * @param adapter adapter
     */
    public GestureTouchHelperCallback(final GestureAdapter adapter) {
        mGestureAdapter = adapter;
    }

    @Override
    public int getMovementFlags(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        final GestureViewHolder holder = (GestureViewHolder) viewHolder;
        return makeMovementFlags(holder.canDrag() ? mDragFlags : 0, holder.canSwipe() ? mSwipeFlags : 0);
    }

    @Override
    public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder source, final RecyclerView.ViewHolder target) {
        return mGestureAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
        mGestureAdapter.onItemDismissed(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(final RecyclerView.ViewHolder viewHolder, final int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof GestureViewHolder) {
                final GestureViewHolder itemViewHolder = (GestureViewHolder) viewHolder;
                itemViewHolder.onItemSelect();
            }
        }
    }

    @Override
    public void clearView(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mGestureAdapter.onItemMoved();
        if (viewHolder instanceof GestureViewHolder) {
            final GestureViewHolder itemViewHolder = (GestureViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return mIsLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mIsSwipeEnabled;
    }

    /**
     * Returns true if manual drag is enabled, false if manual drag is disabled.
     *
     * @return manual drag state
     */
    public boolean isManualDragEnabled() {
        return mIsManualDragEnabled;
    }

    /**
     * Sets manual drag gesture enabled or disabled.
     * Manual drag is disabled by default.
     *
     * @param enabled true to enable, false to disable
     */
    public void setManualDragEnabled(final boolean enabled) {
        mIsManualDragEnabled = enabled;
        //update adapter to enable/disable manual drag spawns
        mGestureAdapter.allowManualDrag(mIsManualDragEnabled);
    }

    /**
     * Sets swipe gesture enabled or disabled.
     * Swipe is disabled by default.
     *
     * @param enabled true to enable, false to disable
     */
    public void setSwipeEnabled(final boolean enabled) {
        mIsSwipeEnabled = enabled;
    }

    /**
     * Sets long press drag gesture enabled or disabled.
     * Long press drag is disabled by default.
     *
     * @param enabled true to enable, false to disable
     */
    public void setLongPressDragEnabled(final boolean enabled) {
        mIsLongPressDragEnabled = enabled;
    }

    /**
     * Sets predefined flags for RecyclerView layout.
     *
     * @param layout type of the RecyclerView layout
     */
    public void setGestureFlagsForLayout(final RecyclerView.LayoutManager layout) {
        if (layout instanceof GridLayoutManager) {
            mDragFlags = GRID.getDragFlags(layout);
            mSwipeFlags = GRID.getSwipeFlags(layout);
        } else if (layout instanceof LinearLayoutManager) {
            mDragFlags = LINEAR.getDragFlags(layout);
            mSwipeFlags = LINEAR.getSwipeFlags(layout);
        } else if (layout instanceof StaggeredGridLayoutManager) {
            mDragFlags = STAGGERED.getDragFlags(layout);
            mSwipeFlags = STAGGERED.getSwipeFlags(layout);
        } else {
            throw new IllegalArgumentException("Unsupported layout type.");
        }
    }

    /**
     * Sets flags for drag gesture.
     * See {@link ItemTouchHelper} flags.
     *
     * @param flags flags for drag gesture
     */
    public void setDragFlags(final int flags) {
        mDragFlags = flags;
    }

    /**
     * Sets flags for swipe gesture.
     * See {@link ItemTouchHelper} flags.
     *
     * @param flags flags for swipe gesture
     */
    public void setSwipeFlags(final int flags) {
        mSwipeFlags = flags;
    }
}
