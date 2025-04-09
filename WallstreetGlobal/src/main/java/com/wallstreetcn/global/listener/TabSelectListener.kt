package com.wallstreetcn.global.listener

import androidx.recyclerview.widget.RecyclerView
import com.pawegio.kandroid.visible
import com.richzjc.library.TabLayout
import com.wallstreetcn.helper.utils.rx.RxUtils
import java.util.concurrent.TimeUnit

class TabSelectListener(val positions: List<Int>?, val tabLayout: TabLayout, val recyclerView: RecyclerView?, val offset: Int) : TabLayout.OnTabSelectedListener {

    private val rvScrollListener = RvScrollListener(positions, tabLayout, this, offset)

    init {
        recyclerView?.addOnScrollListener(rvScrollListener)
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        if (tabLayout.visible) {
            recyclerView?.removeOnScrollListener(rvScrollListener)
            val position = p0?.position ?: 0
            val locatePosition = positions?.get(position) ?: 0
            recyclerView?.scrollToPos(locatePosition, offset)
            RxUtils.delayDo(200, TimeUnit.MILLISECONDS)
                    .doFinally { recyclerView?.addOnScrollListener(rvScrollListener) }
                    .subscribe()

        }
    }
}