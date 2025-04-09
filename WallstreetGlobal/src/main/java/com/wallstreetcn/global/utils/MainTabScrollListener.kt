package com.wallstreetcn.global.utils

import androidx.recyclerview.widget.RecyclerView
import com.richzjc.observer.ObserverManger
import com.wallstreetcn.helper.utils.observer.ObserverIds
import com.wallstreetcn.helper.utils.system.ScreenUtils

class MainTabScrollListener : RecyclerView.OnScrollListener() {

    private var mOffset: Int = 0
    private var mDy = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        mDy = dy
        mOffset += dy
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE && mDy != 0 && recyclerView.isShown) {
            notifyChange()
        }
    }

    fun notifyChange() {
        if ((mDy > 0 && mOffset > ScreenUtils.getScreenHeight()) || (mDy < 0 && mOffset < ScreenUtils.getScreenHeight()))
            ObserverManger.getInstance().notifyObserver(ObserverIds.INDEX_SCROLL_TAB_CHANGE, mOffset)
    }

    fun onRefresh() {
        this.mOffset = 0
        mDy = 0
        ObserverManger.getInstance().notifyObserver(ObserverIds.INDEX_SCROLL_TAB_CHANGE, mOffset)
    }

    companion object {
        @JvmStatic
        fun reset() {
            ObserverManger.getInstance().notifyObserver(ObserverIds.INDEX_SCROLL_TAB_CHANGE, 0)
        }
    }
}