package com.wallstreetcn.rpc.coroutines

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kronos.volley.VolleyError
import com.wallstreetcn.helper.utils.TLog
import com.wallstreetcn.helper.utils.data.CrashReport
import com.wallstreetcn.rpc.AbstractApi
import com.wallstreetcn.rpc.exception.ErrorCode
import com.wallstreetcn.rpc.exception.ErrorUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val SUCCESS: Int = 200

/*fun requestData(sus: suspend () -> Unit): Job {
    return MainScope().launch { sus() }
}*/

fun requestData(throwableCallback: ThrowableCallback? = null, sus: suspend () -> Unit): Job {
    return MainScope().launch {
        try {
            sus()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            throwableCallback?.throwableCallback(throwable)
        }
    }
}

suspend fun <T> getDeffer(api: AbstractApi<T>?): RequestResult<T> {
    return try {
        if (api?.responseListener != null)
            throw IllegalStateException("kotlin 解决的就是 回调问题， 因此不用传responseListener")
        var tempData: T? = null
        val data = suspendCoroutine<T> { continuation ->
            api?.realRequest?.setRequestListener {
                try {
                    if (it.isCache) {
                        TLog.i("process", "cache")
                        tempData = it.getData()
                    } else {
                        TLog.i("process", "network")
                        continuation?.resume(it.getData())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            api?.realRequest?.setErrorListener {
                try {
                    if (tempData != null) {
                        TLog.i("process", "error cache")
                        continuation?.resume(tempData!!)
                    } else {
                        onVolleyError(api, it)
                        continuation.resumeWithException(it)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            api?.start()
        }
        RequestResult(data, SUCCESS)
    } catch (e: Exception) {
        print(e.message)
        RequestResult(null, onVolleyError(api, e as? VolleyError))
    }
}


private fun <T> onVolleyError(api: AbstractApi<T>?, error: VolleyError?): Int {
    error ?: return ErrorCode.EMPTYURL
    try {
        val errorCode = if (error.networkResponse == null) ErrorCode.EMPTYURL else error.networkResponse.statusCode
        val errorMessage = if (error.networkResponse == null) "" else error.networkResponse.errorResponseString
        ErrorUtils.responseFailed(errorMessage, api?.isNeedToast ?: false, errorCode, api)
        return errorCode
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return ErrorCode.EMPTYURL
}

data class RequestResult<T>(val data: T?, val code: Int)


suspend fun <T> LifecycleOwner.getLifecycleDeffer(api: AbstractApi<T>?): RequestResult<T> {
    return try {
        if (api?.responseListener != null)
            throw IllegalStateException("kotlin 解决的就是 回调问题， 因此不用传responseListener")
        var tempData: T? = null
        val data = suspendCoroutine<T> { continuation ->
            val requestData = MutableLiveData<Any>()
            requestData.observe(this, Observer {
                TLog.i("coroutines", "observer")
                if (it is VolleyError) {
                    continuation.resumeWithException(it)
                } else {
                    continuation.resume(it as T)
                }
            })

            api?.realRequest?.setRequestListener {
                try {
                    if (it.isCache) {
                        TLog.i("process", "cache")
                        tempData = it.getData()
                    } else {
                        TLog.i("process", "network")
                        requestData.value = it.getData()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            api?.realRequest?.setErrorListener {
                try {
                    if (tempData != null) {
                        TLog.i("process", "error cache")
                        requestData.value = tempData
                    } else {
                        requestData.value = it
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            api?.start()
        }
        RequestResult(data, SUCCESS)
    } catch (e: Exception) {
        print(e.message)
        RequestResult(null, onVolleyError(api, e as? VolleyError))
    }
}

suspend fun <T> getDefferWithOwner(owner: LifecycleOwner?, api: AbstractApi<T>?): RequestResult<T> {
    return try {
        if (api?.responseListener != null)
            throw IllegalStateException("kotlin 解决的就是 回调问题， 因此不用传responseListener")
        var tempData: T? = null
        val data = suspendCoroutine<T> { continuation ->
            val requestData = MutableLiveData<Any>()
            if (owner == null) {
                requestData.observeForever {
                    if (it is VolleyError) {
                        continuation.resumeWithException(it)
                    } else {
                        continuation.resume(it as T)
                    }
                }
            } else {
                requestData.observe(owner, Observer {
                    if (it is VolleyError) {
                        continuation.resumeWithException(it)
                    } else {
                        continuation.resume(it as T)
                    }
                })
            }

            api?.realRequest?.setRequestListener {
                try {
                    if (it.isCache) {
                        TLog.i("process", "cache")
                        tempData = it.getData()
                    } else {
                        TLog.i("process", "network")
                        requestData.value = it.getData()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            api?.realRequest?.setErrorListener {
                try {
                    if (tempData != null) {
                        TLog.i("process", "error cache")
                        requestData.value = tempData
                    } else {
                        requestData.value = it
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            api?.start()
        }
        RequestResult(data, SUCCESS)
    } catch (e: Exception) {
        print(e.message)
        RequestResult(null, onVolleyError(api, e as? VolleyError))
    }

}

interface ThrowableCallback {
    fun throwableCallback(throwable: Throwable?)
}