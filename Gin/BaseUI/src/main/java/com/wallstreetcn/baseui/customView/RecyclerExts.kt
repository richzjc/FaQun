package com.wallstreetcn.baseui.customView

import android.content.Context
import android.util.Log
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.ItemTouchHelper
import com.wallstreetcn.baseui.customView.gesture.GestureAdapter
import com.wallstreetcn.baseui.customView.gesture.GestureListener
import com.wallstreetcn.baseui.customView.gesture.GestureTouchHelperCallback
import com.wallstreetcn.baseui.customView.decoration.HorizontalDividerItemDecoration


fun androidx.recyclerview.widget.RecyclerView.fastSmoothScrollManager(): androidx.recyclerview.widget.RecyclerView {
    this.layoutManager = FastSmoothScrollManager(this.context)
    return this
}

fun androidx.recyclerview.widget.RecyclerView.returnTop(action: (() -> Unit)?) {
    this.smoothScrollToPosition(0)
    val manager = layoutManager
    if (manager is FastSmoothScrollManager) {
        val firstItem = manager.findFirstCompletelyVisibleItemPosition()

        val itemCount = adapter!!.itemCount
        Log.d("@@@@@", "itemCount: $itemCount , firstItem: $firstItem")
        smoothScrollToPosition(0)
        Log.d("@@@@@", "refresh status: ${itemCount - firstItem < 20}")
        //临时方案
        if (firstItem < 5) {
            action?.invoke()
        } else {
            val duration = if (firstItem < 30) 500L else 1300L
            postDelayed({
                action?.invoke()
            }, duration)
        }
    }
}


//val RecyclerView.paginer: PaginateHelper
//    get() =p
//

//object PageinerManager {
//
//    private val pagines: MutableMap<RecyclerView, PaginateHelper> = mutableMapOf()
//
//    fun add(pair: Pair<RecyclerView, PaginateHelper>?) {
//        pair?.let {
//            pagines.put(it.first, it.second)
//        }
//    }
//
//    fun add(target: RecyclerView, paginer: PaginateHelper) {
//        pagines[target] = paginer
//    }
//
//    fun remove(target: RecyclerView) {
//        pagines.remove(target)
//    }
//
//    fun get(target: RecyclerView): PaginateHelper? {
//        return pagines[target]
//    }
//
//}
//
//var RecyclerView.paginate: PaginateHelper
//    get() {
//        return this.paginate
//    }
//    set(value) {
//        this.paginate = value
//    }
//
//
//fun RecyclerView.bindPaginate(): RecyclerView {
//    return bindPaginate(DefaultLoadMoreView())
//}
//
//fun RecyclerView.bindPaginate(creator: LoadingListItemCreator): RecyclerView {
//    val paginer = PaginateHelper()
//    paginer.bindPaginate(this, creator)
//    PageinerManager.add(this, paginer)
//    return this
//}
//
//fun RecyclerView.unBindPaginate() {
//    this.let {
//        PageinerManager.remove(it)
//    }
//}
//
//
//fun RecyclerView.loaded(loadedAll: Boolean): RecyclerView {
//    PageinerManager.get(this)?.let {
//        it.finishLoad()
//        it.setLoadedAll(loadedAll)
//    }
//    return this
//}
//
//
//fun RecyclerView.onLoadMore(action: () -> Unit) = addLoadMoreListener(loadMore = action)
//fun RecyclerView.addLoadMoreListener(loadMore: (() -> Unit)?) {
//    val listener = object : OnLoadMoreListener {
//        override fun onLoadMore() {
//            PageinerManager.get(this@addLoadMoreListener)?.let {
//                if (it.isLoading) {
//                    return
//                }
//                it.loading()
//                loadMore?.invoke()
//            }
//
//
//        }
//
//    }
//    PageinerManager.get(this@addLoadMoreListener)?.setOnLoadMoreListener(listener)
//}

fun androidx.recyclerview.widget.RecyclerView.divider(size: Int, @ColorInt colorInt: Int, showLastDivider: Boolean = true) {
    divider(size, colorInt, 0, 0, showLastDivider)
}


fun androidx.recyclerview.widget.RecyclerView.divider(size: Int, @ColorInt colorInt: Int, marginLeft: Int, marginRight: Int, showLastDivider: Boolean = true) {
    addItemDecoration(
        HorizontalDividerItemDecoration.Builder(this.context)
            .size(size)
            .margin(marginLeft, marginRight)
            .color(colorInt)
            .showLastDivider(showLastDivider)
            .build()
    )
}


fun androidx.recyclerview.widget.RecyclerView.bindGesture() {

    if (this.adapter is GestureAdapter<*, *>) {

        val mTouchHelperCallback = GestureTouchHelperCallback(adapter as GestureAdapter<*, *>)
        mTouchHelperCallback.setSwipeEnabled(true)
        mTouchHelperCallback.isManualDragEnabled = true
        mTouchHelperCallback.setGestureFlagsForLayout(this.layoutManager)
        val touchHelper = ItemTouchHelper(mTouchHelperCallback)
        touchHelper.attachToRecyclerView(this)
        (adapter as GestureAdapter<*, *>).setGestureListener(GestureListener(touchHelper))
    }


}

class VerticalLayoutManager(context: Context?) : androidx.recyclerview.widget.LinearLayoutManager(context) {

    var isScrollEnabled = true

    init {
        orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()

    }

}


class FastSmoothScrollManager(context: Context?) : androidx.recyclerview.widget.LinearLayoutManager(context) {
    init {
        orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
    }

    override fun smoothScrollToPosition(recyclerView: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State, position: Int) {
        val linearSmoothScroller = object : androidx.recyclerview.widget.LinearSmoothScroller(recyclerView.context) {
            override fun calculateTimeForScrolling(dx: Int): Int {
                var dx = dx
                // 此函数计算滚动dx的距离需要多久，当要滚动的距离很大时，比如说52000，
                // 经测试，系统会多次调用此函数，每10000距离调一次，所以总的滚动时间
                // 是多次调用此函数返回的时间的和，所以修改每次调用该函数时返回的时间的
                // 大小就可以影响滚动需要的总时间，可以直接修改些函数的返回值，也可以修改
                // dx的值，这里暂定使用后者.
                // (See LinearSmoothScroller.TARGET_SEEK_SCROLL_DISTANCE_PX)
                if (dx > 3000) {
                    dx = 3000
                }
                return super.calculateTimeForScrolling(dx)
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }
}


