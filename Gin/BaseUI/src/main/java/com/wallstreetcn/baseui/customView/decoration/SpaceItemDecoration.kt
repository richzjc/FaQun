package com.wallstreetcn.baseui.customView.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val column = getSpanCount(parent)
        val position = parent.getChildAdapterPosition(view)
        val itemsize = space / column
        // 列索引
        outRect.left = (position % column) * itemsize
        outRect.right = (column - position % column - 1) * itemsize
        // 行间距
        if (position >= column) {
            outRect.top = space
        }
    }

    private fun getSpanCount(view: RecyclerView): Int {
        return if (view.layoutManager is GridLayoutManager) {
            (view.layoutManager as GridLayoutManager).spanCount
        } else {
            1
        }
    }
}