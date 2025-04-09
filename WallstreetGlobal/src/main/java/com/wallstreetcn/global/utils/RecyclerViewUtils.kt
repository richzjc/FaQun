package com.wallstreetcn.global.utils

import androidx.recyclerview.widget.RecyclerView
import com.pawegio.kandroid.setWidth
import com.wallstreetcn.helper.utils.system.ScreenUtils

object RecyclerViewUtils {
    /**
     * 动态设置recyclerview的宽度，使得item总体居中
     */
    fun resetItemMinWidth(recyclerView: RecyclerView, count: Int, minWidth: Float, margin: Float): Int {
        val recyclerViewMinWidth = ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(2 * margin)
        recyclerView.setWidth(recyclerViewMinWidth)
        return maxOf(recyclerViewMinWidth / minOf(5, count), ScreenUtils.dip2px(minWidth))
    }
}