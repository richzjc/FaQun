package com.wallstreetcn.global.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollToPos(position: Int, offset: Int) {
    val manager = layoutManager as? LinearLayoutManager
    manager?.scrollToPositionWithOffset(position, offset)
}