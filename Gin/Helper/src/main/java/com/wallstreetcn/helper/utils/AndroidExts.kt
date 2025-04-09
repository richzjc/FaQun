package com.wallstreetcn.helper.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.os.Build
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.DimenRes
import java.util.*
import kotlin.math.ceil

/**
 * Created by yuweichen on 2018/3/12.
 */

@Deprecated("")
fun getUniqueDeviceID(): String {
    var serial: String?

    val szDevIDShort = "35" +
            Build.BOARD.length % 10 + Build.BRAND.length % 10 +

            Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +

            Build.DISPLAY.length % 10 + Build.HOST.length % 10 +

            Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +

            Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +

            Build.TAGS.length % 10 + Build.TYPE.length % 10 +

            Build.USER.length % 10 //13 位

    return try {
        serial = Build::class.java.getField("SERIAL").get(null).toString()
        "android-" + UUID(szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    } catch (exception: Exception) {
        //serial需要一个初始化
        serial = "serial" // 随便一个初始化
        "android-" + UUID(szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }

}

fun Context.screenW(): Int {
    return resources.displayMetrics.widthPixels
}

inline fun View.screenW(): Int = context.screenW()

inline fun Activity.screenW(): Int = (this as Context).screenW()

inline fun androidx.fragment.app.Fragment.screenW(): Int = (activity as Context).screenW()

inline fun android.app.Fragment.screenW(): Int = (activity as Context).screenW()

fun Context.screenH(): Int {
    return resources.displayMetrics.heightPixels
}

inline fun View.screenH(): Int = context.screenH()

inline fun Activity.screenH(): Int = (this as Context).screenH()

inline fun androidx.fragment.app.Fragment.screenH(): Int = (activity as Context).screenH()

inline fun android.app.Fragment.screenH(): Int = (activity as Context).screenH()


fun Context.statusBarHeight(): Int {
    var statusBarHeight = 0
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resId > 0) {
        statusBarHeight = resources.getDimensionPixelSize(resId)
    }
    return statusBarHeight
}

inline fun View.statusBarHeight(): Int = context.statusBarHeight()

inline fun Activity.statusBarHeight(): Int = (this as Context).statusBarHeight()

inline fun androidx.fragment.app.Fragment.statusBarHeight(): Int = (activity as Context).statusBarHeight()

inline fun android.app.Fragment.statusBarHeight(): Int = (activity as Context).statusBarHeight()

fun Context.navigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height",
            "dimen", "android")
    //获取NavigationBar的高度
    return resources.getDimensionPixelSize(resourceId)
}

inline fun View.navigationBarHeight(): Int = context.navigationBarHeight()

inline fun Activity.navigationBarHeight(): Int = (this as Context).navigationBarHeight()

inline fun androidx.fragment.app.Fragment.navigationBarHeight(): Int = (activity as Context).navigationBarHeight()

inline fun android.app.Fragment.navigationBarHeight(): Int = (activity as Context).navigationBarHeight()

fun Context.areNotifyEnable(): Boolean {
    return AndroidUtil.areNotifyEnable(this)
}

inline fun Activity.areNotifyEnable(): Boolean = (this as Context).areNotifyEnable()

inline fun androidx.fragment.app.Fragment.areNotifyEnable(): Boolean = (activity as Context).areNotifyEnable()

inline fun android.app.Fragment.areNotifyEnable(): Boolean = (activity as Context).areNotifyEnable()

fun Context.checkNotify(currentFinish: Boolean = false): Boolean {
    val areNotifyEnable = areNotifyEnable()
    if (!areNotifyEnable()) {
        AndroidUtil.showNotifySettingDialog(this, currentFinish)
    }
    return areNotifyEnable
}

inline fun Activity.checkNotify(currentFinish: Boolean = false): Boolean = (this as Context).checkNotify(currentFinish)

inline fun androidx.fragment.app.Fragment.checkNotify(currentFinish: Boolean = false): Boolean = (activity as Context).checkNotify(currentFinish)

inline fun android.app.Fragment.checkNotify(currentFinish: Boolean = false): Boolean = (activity as Context).checkNotify(currentFinish)

fun Context.actionBarHeight(): Int {
    val tv = TypedValue()
    theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
    return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
}

inline fun View.actionBarHeight(): Int = context.actionBarHeight()

inline fun Activity.actionBarHeight(): Int = (this as Context).actionBarHeight()

inline fun androidx.fragment.app.Fragment.actionBarHeight(): Int = (activity as Context).actionBarHeight()

inline fun android.app.Fragment.actionBarHeight(): Int = (activity as Context).actionBarHeight()

fun Context.colorInt(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

@SuppressLint("MissingPermission")
fun Activity.vibrate(milliseconds: Long) {
    var mill = milliseconds

//    if (Build.MANUFACTURER.toLowerCase() == "huawei") {
//        mill *= 8
//    }

    val vibrator = this
            .getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(VibrationEffect.createOneShot(mill, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(mill)
    }

}


inline fun View.colorInt(@ColorRes resId: Int): Int = context.colorInt(resId)

inline fun Activity.colorInt(@ColorRes resId: Int): Int = (this as Context).colorInt(resId)

inline fun androidx.fragment.app.Fragment.colorInt(@ColorRes resId: Int) = (activity as Context).colorInt(resId)

inline fun android.app.Fragment.colorInt(@ColorRes resId: Int): Int = (activity as Context).colorInt(resId)

fun Context.dip(dpValue: Float) = dip2px(dpValue)

fun Context.dip2px(dpValue: Float): Int {
    return ceil(dpValue * resources.displayMetrics.density + 0.5).toInt()
}

inline fun androidx.fragment.app.Fragment.dip2px(dpValue: Float): Int = (activity as Context).dip2px(dpValue)
inline fun android.app.Fragment.dip2px(dpValue: Float): Int = (activity as Context).dip2px(dpValue)

//the same for the views
inline fun View.dip(value: Int): Int = context.dip2px(value.toFloat())

inline fun View.dip(value: Float): Int = context.dip2px(value)


inline fun View.dip2px(dpValue: Float): Int = context.dip2px(dpValue)
inline fun Activity.dip2px(dpValue: Float): Int = (this as Context).dip2px(dpValue)


fun Context.sp(spValue: Float) = sp2px(spValue)

fun Context.sp2px(spValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}


inline fun View.sp(value: Int): Int = context.sp2px(value.toFloat())
inline fun View.sp(value: Float): Int = context.sp2px(value)
inline fun View.px2sp(px: Int): Float = context.px2sp(px.toFloat()).toFloat()


inline fun View.dimen(@DimenRes resource: Int): Int = context.resources.getDimension(resource).toInt()




//the same for the views
inline fun Activity.dip(value: Int): Int = (this as Context).dip(value.toFloat())
inline fun Activity.dip(value: Float): Int = (this as Context).dip(value)
inline fun Activity.sp(value: Int): Int = (this as Context).sp(value.toFloat())
inline fun Activity.sp(value: Float): Int = (this as Context).sp(value)
inline fun Activity.dimen(@DimenRes resource: Int): Int = (this as Context).resources.getDimension(resource).toInt()

//the same for Fragments
inline fun android.app.Fragment.dip(value: Int): Int = activity.dip(value)
inline fun android.app.Fragment.dip(value: Float): Int =activity.dip(value)
inline fun android.app.Fragment.sp(value: Int): Int = activity.sp(value)
inline fun android.app.Fragment.sp(value: Float): Int = activity.sp(value)
inline fun android.app.Fragment.dimen(@DimenRes resource: Int): Int = activity.resources.getDimension(resource).toInt()



inline fun <T> T.postRun(crossinline block: T.() -> Unit) {
    android.os.Handler(Looper.getMainLooper()).post {
        block()
    }
}

inline fun <T> T.postDelayed(delay: Long, crossinline block: T.() -> Unit) {
    android.os.Handler(Looper.getMainLooper()).postDelayed({
        block()
    }, delay)
}

inline fun <T> runWithMiniSdk(minVersion: Int, func: () -> T) {
    if (android.os.Build.VERSION.SDK_INT >= minVersion) {
        func()
    }
}

inline fun <T> runWithDebug(debug: Boolean, func: () -> T) {
    if (debug) {
        func()
    }
}

fun isOnMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

//View的Global Layout方法
inline fun View.onWaitGlobalLayout(crossinline f: () -> Unit) =
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                f()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })


/**
 *  检查a
 */
fun View.checkActivityDestroy(): Boolean {

    return try {
        when (context) {
            is androidx.fragment.app.FragmentActivity -> {
                (context as androidx.fragment.app.FragmentActivity).isDestroyed || (context as androidx.fragment.app.FragmentActivity).isFinishing
            }
            is Activity -> {
                (context as Activity).isDestroyed || (context as Activity).isFinishing
            }
            else -> {
                false
            }
        }
    } catch (e: Exception) {
        true
    }


}


