package com.wallstreetcn.global.listener

import com.wallstreetcn.helper.utils.TLog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import com.pawegio.kandroid.visible
import com.richzjc.library.TabLayout
import com.wallstreetcn.helper.utils.anim.AnimateUtil

class RvScrollListener(val positions: List<Int>?, val tablayout: TabLayout, val tabListener: TabSelectListener, val offset: Int) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val manager = recyclerView?.layoutManager as? LinearLayoutManager
        val firstItem = manager?.findFirstVisibleItemPosition() ?: return
        val lastItem = manager?.findLastVisibleItemPosition()
        checkVisible(recyclerView, positions)

        if (!tablayout.visible) return
        TLog.i("tab", "start")
        if (positions != null)
            for (index in positions.indices) {
                if (positions.size == 1 || index == positions.size - 1) {
                    selectTab(index = index)
                    return
                } else {
                    lastItem ?: return
                    val startPosition = positions[index]
                    val endPosition = positions[index + 1] - 1
                    val next = positions[index + 1]
                    val isInScreen = next in firstItem..lastItem
                    val flag3 = isInScreen && ((manager?.findViewByPosition(next)?.top
                            ?: -1) <= offset)
                    when {
                        flag3 -> {
                            selectTab(index + 1)
                            return
                        }
                        firstItem in (startPosition + 1)..endPosition -> {
                            selectTab(index)
                            return
                        }
                        startPosition in firstItem..lastItem -> {
                            selectTab(index)
                            return
                        }
                    }
                }
            }
    }


    private fun selectTab(index: Int) {
        tablayout.removeOnTabSelectedListener(tabListener!!)
        val tab = tablayout?.getTabAt(index)
        if (tab != null)
            tablayout.selectTab(tab)
        tablayout.addOnTabSelectedListener(tabListener)
    }

    private fun checkVisible(recyclerView: RecyclerView, positions: List<Int>?) {
        val manager = recyclerView?.layoutManager as? LinearLayoutManager
        val firstItem = manager?.findFirstVisibleItemPosition() ?: return
        val lastItem = manager?.findLastVisibleItemPosition() ?: return
        val saveFirstIndex = if (positions?.isEmpty() != false) -1 else positions[0]
        val flag1 = saveFirstIndex in 0..firstItem
        val flag2 = saveFirstIndex in firstItem..lastItem
        val flag3 = flag2 && ((manager?.findViewByPosition(saveFirstIndex)?.top ?: -1) <= offset)
        if ((flag1 || flag3) && !tablayout.visible) {
            tablayout.show()
            AnimateUtil.showAnimUpToBottom(tablayout)
        } else {
            val temp1 = firstItem < saveFirstIndex
            val temp2 = saveFirstIndex > lastItem
            val temp3 = temp2 || ((manager?.findViewByPosition(saveFirstIndex)?.top ?: -1) > offset)
            if ((temp1 && temp3) && tablayout.visible) {
                tablayout.hide(true)
                AnimateUtil.dismissAnimBottomToUp(tablayout)
            }
        }
    }
}