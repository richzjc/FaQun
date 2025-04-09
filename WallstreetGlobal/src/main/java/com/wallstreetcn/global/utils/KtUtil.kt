package com.wallstreetcn.global.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Context.getColorbyId(colorId: Int) = ContextCompat.getColor(this, colorId)

fun Fragment.getColorbyId(colorId: Int): Int {
    this.context?.let {
        return ContextCompat.getColor(it, colorId)
    }
    return 0
}

fun View.getColorbyId(colorId: Int): Int {
    this?.context?.let {
        return ContextCompat.getColor(it, colorId)
    }
    return 0
}


