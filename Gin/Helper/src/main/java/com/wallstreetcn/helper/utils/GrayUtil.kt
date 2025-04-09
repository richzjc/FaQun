package com.wallstreetcn.helper.utils

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.View


fun toGray(view: View) {
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f) // 置灰
    val paint = Paint()
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    view.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
}

fun cancelToGray(view: View) {
    val paint = Paint()
    paint.colorFilter = null
    view.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
}