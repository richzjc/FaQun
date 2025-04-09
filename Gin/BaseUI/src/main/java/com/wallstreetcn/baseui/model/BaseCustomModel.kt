package com.wallstreetcn.baseui.model

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.wallstreetcn.baseui.adapter.IDifference
import com.wallstreetcn.baseui.adapter.IEqualsAdapter
import com.wallstreetcn.baseui.callback.ICustomModel
import com.wallstreetcn.rpc.AbstractApi
import com.wallstreetcn.rpc.coroutines.getDefferWithOwner
import com.wallstreetcn.rpc.coroutines.requestData

open class BaseCustomModel(val owner: LifecycleOwner?) : MutableLiveData<ArrayList<in ICustomModel?>>(), Parcelable, IDifference, IEqualsAdapter {

    //用来标记是否是下拉刷新
    var pullToRefresh = false

    //描述LoadingItem的高度
    var defaultHeight: Int = ViewGroup.LayoutParams.MATCH_PARENT

    var uniqueIdValue: String? = ""

    var bundle: Bundle? = null

    open fun getApiBundle(): Bundle {
        return if (bundle == null) {
            bundle = Bundle()
            bundle!!
        } else
            bundle!!
    }

    //该model绑定的接口
    @JvmField
    var apiClass: Class<out AbstractApi<out Any>>? = null

    constructor(parcel: Parcel) : this(null) {
        uniqueIdValue = parcel.readString()
        pullToRefresh = parcel.readByte() != 0.toByte()
        bundle = parcel.readBundle(Bundle::class.java.classLoader)
    }

    open fun realRequestData() {
        pullToRefresh = false
        apiClass?.also {
            requestData {
                try {
                    if (!isInterceptApi()) {
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

    open fun combineData(deffer: Any?) {
        val list = ArrayList<ICustomModel?>()
        if (deffer != null) {
            if (deffer !is ICustomModel) {
                throw IllegalStateException("接口返回的数据必须要实现ICustomModel接口")
            }
            list?.add(deffer)
        }
        value = list
    }

    open fun isInterceptApi(): Boolean = false

    open fun clear() {
        //对于下拉刷新需要什么些什么处理， 比如清除掉Cursor等等
    }


    override fun getUniqueId(): String {
        return uniqueIdValue ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uniqueIdValue)
        parcel.writeByte(if (pullToRefresh) 1 else 0)
        parcel.writeBundle(bundle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseCustomModel> {
        override fun createFromParcel(parcel: Parcel): BaseCustomModel {
            return BaseCustomModel(parcel)
        }

        override fun newArray(size: Int): Array<BaseCustomModel?> {
            return arrayOfNulls(size)
        }
    }
}