package com.wallstreetcn.baseui.callback

import com.wallstreetcn.baseui.adapter.BaseRecycleViewHolder
import com.wallstreetcn.baseui.model.BaseCustomModel

interface ICustomModel {

    fun getHolderClass(): Class<out BaseRecycleViewHolder<out Any>>?

    fun getStickyHolderClass() : Class<out BaseRecycleViewHolder<out Any>>? = null

    fun getHeaderId() : Long

    fun bindCustomModel(model : BaseCustomModel?)
}