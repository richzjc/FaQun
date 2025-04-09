package com.wallstreetcn.baseui.model

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.wallstreetcn.helper.utils.TLog
import androidx.lifecycle.LifecycleOwner
import com.wallstreetcn.baseui.adapter.IDifference
import com.wallstreetcn.baseui.callback.ICustomModel
import com.wallstreetcn.rpc.AbstractApi
import com.wallstreetcn.rpc.coroutines.getDefferWithOwner
import com.wallstreetcn.rpc.coroutines.requestData

//如果接口是支持分页的需要用这个model
open class BaseCustomListModel<T : BaseListModel<out IDifference>> constructor(owner: LifecycleOwner?) : BaseCustomModel(owner) {

    //支持分页加载的cursor
    var cursor: String? = ""

    // 支持分页加载的条数
    var limit: Int = 20

    // 是否加载到了底部
    var isTouchEnd: Boolean = false

    override fun clear() {
        super.clear()
        cursor = ""
    }

    override fun getApiBundle(): Bundle {
        val bundle = super.getApiBundle()
        bundle.putString("cursor", cursor)
        bundle.putString("limit", "${limit}")
        return bundle
    }

    override fun realRequestData() {
        pullToRefresh = false
        apiClass?.also {
            requestData {
                try {
                    if (!isInterceptApi()) {
                        TLog.i("list", "cursor = ${cursor}")
                        val constructor = it.getConstructor(Bundle::class.java)
                        val api = constructor.newInstance(getApiBundle()) as? AbstractApi<Any>
                        val cacheData = getDefferWithOwner(owner, api).data
                        combineData(cacheData)
                    } else {
                        combineData(null)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (!isInterceptApi()) {
                        TLog.i("list", "cursor = ${cursor}")
                        val constructor = it.getConstructor()
                        val api = constructor.newInstance() as? AbstractApi<Any>
                        val cacheData = getDefferWithOwner(owner, api).data
                        combineData(cacheData)
                    } else {
                        combineData(null)
                    }
                }
            }
        }
    }

    override fun combineData(deffer: Any?) {
        var data = value
        if (data == null)
            data = ArrayList()

        if (deffer != null) {
            if (deffer !is BaseListModel<out IDifference>) {
                throw IllegalStateException("接口返回的实体类必须继承BaseListModel")
            }

            if (deffer is BaseListModel<out IDifference>
                    && deffer.results != null
                    && deffer.results.size > 0
                    && (deffer.results.get(0) !is ICustomModel)) {
                throw IllegalStateException("getResult里面返回的数据结构必须要实现ICustomModel接口")
            }

            onSuccess(deffer as? T, data)
        } else {
            data.clear()
        }

        value = data
    }

    open fun onSuccess(model: T?, data: ArrayList<in ICustomModel>) {
        checkIsEnd(model)
        if (TextUtils.isEmpty(cursor)) {
            data.clear()
            if (model?.results != null)
                data.addAll(model?.results as List<ICustomModel>)
        } else if (!TextUtils.equals(cursor, model?.next_cursor)) {
            data.addAll(model?.results as List<ICustomModel>)
        }

        if (model != null)
            cursor = model.next_cursor
    }

    open fun checkIsEnd(model: T?) {
        val list = model?.results
        isTouchEnd = if (list == null || list.isEmpty()) {
            true
        } else TextUtils.isEmpty(model.next_cursor)
    }


    constructor(parcel: Parcel) : this(null) {
        uniqueIdValue = parcel.readString() ?: ""
        cursor = parcel.readString()
        limit = parcel.readInt()
        pullToRefresh = parcel.readByte() != 0.toByte()
        isTouchEnd = parcel.readByte() != 0.toByte()
        bundle = parcel.readBundle(Bundle::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uniqueIdValue)
        parcel.writeString(cursor)
        parcel.writeInt(limit)
        parcel.writeByte(if (pullToRefresh) 1 else 0)
        parcel.writeByte(if (isTouchEnd) 1 else 0)
        parcel.writeBundle(bundle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseCustomListModel<BaseListModel<out IDifference>>> {
        override fun createFromParcel(parcel: Parcel): BaseCustomListModel<BaseListModel<out IDifference>> {
            return BaseCustomListModel(parcel)
        }

        override fun newArray(size: Int): Array<BaseCustomListModel<BaseListModel<out IDifference>>?> {
            return arrayOfNulls(size)
        }
    }
}