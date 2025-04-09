package com.wallstreetcn.global.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

class DotDrawable : Drawable() {

    private var color: Int = Color.BLACK
    private var radius : Float = 0f

    private var paint = Paint()

    override fun draw(canvas: Canvas) {
        paint.color = color
        canvas.drawCircle(radius, radius, radius, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint!!.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint!!.colorFilter = colorFilter
        invalidateSelf()
    }

    fun setColor(color: Int, radius : Float) {
        this.color = color
        this.radius = radius
        invalidateSelf()
    }
}