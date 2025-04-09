package com.wallstreetcn.baseui.customView.gesture;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Enum with predefined gesture flags for various layout managers, see {@link RecyclerView.LayoutManager}
 * @author thesurix
 */
enum LayoutFlags {

    LINEAR {
        @Override
        int getDragFlags(final RecyclerView.LayoutManager layout) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

            final LinearLayoutManager linearLayout = (LinearLayoutManager) layout;
            if (linearLayout.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            return dragFlags;
        }

        @Override
        int getSwipeFlags(final RecyclerView.LayoutManager layout) {
            int swipeFlags = ItemTouchHelper.RIGHT;

            final LinearLayoutManager linearLayout = (LinearLayoutManager) layout;
            if (linearLayout.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                swipeFlags = ItemTouchHelper.UP;
            }
            return swipeFlags;
        }
    },
    GRID {
        @Override
        int getDragFlags(final RecyclerView.LayoutManager layout) {
            return ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }

        @Override
        int getSwipeFlags(final RecyclerView.LayoutManager layout) {
            int swipeFlags = ItemTouchHelper.RIGHT;

            final GridLayoutManager gridLayout = (GridLayoutManager) layout;
            if (gridLayout.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return swipeFlags;
        }
    },
    STAGGERED {
        @Override
        int getDragFlags(final RecyclerView.LayoutManager layout) {
            return ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }

        @Override
        int getSwipeFlags(final RecyclerView.LayoutManager layout) {
            int swipeFlags = ItemTouchHelper.RIGHT;

            final StaggeredGridLayoutManager staggeredGridLayout = (StaggeredGridLayoutManager) layout;
            if (staggeredGridLayout.getOrientation() == StaggeredGridLayoutManager.HORIZONTAL) {
                swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return swipeFlags;
        }
    };

    /**
     * Returns drag flags for the given layout manager.
     * @param layout layout manager instance
     * @return drag flags
     */
    abstract int getDragFlags(final RecyclerView.LayoutManager layout);

    /**
     * Returns swipe flags for the given layout manager.
     * @param layout layout manager instance
     * @return swipe flags
     */
    abstract int getSwipeFlags(final RecyclerView.LayoutManager layout);
}
