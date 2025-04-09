package com.wallstreetcn.baseui.adapter.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.setHeight
import com.pawegio.kandroid.show
import com.wallstreetcn.baseui.adapter.BaseRecycleViewHolder
import com.wallstreetcn.baseui.databinding.BaseRecyclerItemDefaultLoadingBinding
import com.wallstreetcn.baseui.model.BaseCustomModel
import com.wallstreetcn.baseui.widget.RecyclerViewLayout

class DefaultRecycleHolder(context: Context?) : BaseRecycleViewHolder<Any>(context) {

    private var binding: BaseRecyclerItemDefaultLoadingBinding? = null

    override fun getHolderView(
        inflater: LayoutInflater?,
        itemContainer: RecyclerViewLayout?
    ): View {
        if (binding == null) binding = BaseRecyclerItemDefaultLoadingBinding.inflate(
            inflater!!,
            itemContainer,
            false
        )
        return binding!!.getRoot()
    }

    override fun doBindData(content: Any?) {
        super.content = content
        when (content) {
            is BaseCustomModel -> {
                if (content.value == null) {
                    itemView?.show()
                    if (content.defaultHeight >= 0)
                        itemView?.setHeight(content.defaultHeight)
                    else if (content.defaultHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                        itemView?.setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                    }else
                        itemView?.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)

                } else {
                    itemView?.hide(true)
                }
            }
            else -> {
                itemView?.hide(true)
            }
        }
    }
}