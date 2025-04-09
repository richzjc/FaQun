package com.wallstreetcn.global.router

import android.content.Context
import com.wallstreetcn.baseui.adapter.BaseRecycleViewHolder

object ViewHolderFactory {
    private val holders = hashMapOf<String, Class<out BaseRecycleViewHolder<*>>>()

    @JvmStatic
    fun putHolders(key: String, holder: Class<out BaseRecycleViewHolder<*>>) {
        holders[key] = holder
    }

    @JvmStatic
    fun getHolders(key: String, context: Context): BaseRecycleViewHolder<*>? {
        return holders[key]?.getConstructor(Context::class.java)?.newInstance(context)
    }
}