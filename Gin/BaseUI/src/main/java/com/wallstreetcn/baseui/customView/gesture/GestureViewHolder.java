package com.wallstreetcn.baseui.customView.gesture;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Base view holder class for gesture compatible items.
 * @author thesurix
 */
public abstract class GestureViewHolder extends RecyclerView.ViewHolder {

    public GestureViewHolder(final View itemView) {
        super(itemView);
    }

    /**
     * Returns view that can spawn drag gesture. If there is no view simply return null.
     * @return view that can spawn drag gesture
     */
    @Nullable
    public View getDraggableView() {
        return null;
    }

    /**
     * Method that shows view for manual drag gestures.
     * Called only when getDraggableView() returns valid view.
     */
    public void showDraggableView() {
        getDraggableView().setVisibility(View.VISIBLE);
    }

    /**
     * Method that hides view for manual drag gestures.
     * Called only when getDraggableView() returns valid view.
     */
    public void hideDraggableView() {
        getDraggableView().setVisibility(View.GONE);
    }

    /**
     * Indicates that view is selected.
     */
    public void onItemSelect() {}

    /**
     * Indicates that view has no selection.
     */
    public void onItemClear() {}

    /**
     * Returns information if we can drag this view.
     * @return true if draggable, false otherwise
     */
    public abstract boolean canDrag();

    /**
     * Returns information if we can swipe this view.
     * @return true if swipeable, false otherwise
     */
    public abstract boolean canSwipe();
}
