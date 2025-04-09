package com.wallstreetcn.global.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

class LinearGradientDrawable : Drawable() {

    private var startColor : Int = Color.WHITE
    private var endColor : Int = Color.TRANSPARENT
    private var cornerRadius : Float = 0F

    private var paint = Paint()

    override fun draw(canvas: Canvas) {
        val rect = bounds
        val lg = LinearGradient(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.top.toFloat(), startColor, endColor, Shader.TileMode.MIRROR)
        paint?.shader = lg
        canvas.drawRoundRect(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.bottom.toFloat(), cornerRadius, cornerRadius, paint)
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

    fun setColorAndRadius(startColor : Int, endColor : Int, cornerRadius : Float){
        this.startColor = startColor
        this.endColor = endColor
        if(cornerRadius <= 0)
            this.cornerRadius = 0F
        else
            this.cornerRadius =  cornerRadius
        invalidateSelf()
    }

    fun setColor(startColor : Int, endColor : Int){
        this.startColor = startColor
        this.endColor = endColor
        invalidateSelf()
    }

    fun setRadius(cornerRadius : Float){
        if(cornerRadius <= 0)
            this.cornerRadius = 0F
        else
            this.cornerRadius =  cornerRadius
        invalidateSelf()
    }
}