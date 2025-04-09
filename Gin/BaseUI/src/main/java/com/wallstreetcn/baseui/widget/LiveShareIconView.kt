package com.wallstreetcn.baseui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.core.content.res.ResourcesCompat
import com.wallstreetcn.baseui.R
import com.wallstreetcn.helper.utils.system.ScreenUtils

class LiveShareIconView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {


    private val setHeight = ScreenUtils.dip2px(22f)
    val gap by lazy {
        Math.tan(Math.PI * 6.0 / 180) * setHeight
    }

    private var customTypeFace: Typeface? = null


    var fontId : Int = com.wallstreetcn.baseui.R.font.iconfont
    set(value) {
        field = value
        initTypeFace(value)
    }

    init {
        val gap = Math.tan(Math.PI * 6.0 / 180) * setHeight
        setTextColor(Color.WHITE)
        val topPadding = 0
        val leftPadding = ScreenUtils.dip2px(10f)
        setPadding(leftPadding + gap.toInt(), topPadding, leftPadding, topPadding)
        gravity = Gravity.CENTER
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        maxLines = 1
    }


    fun initTypeFace(value : Int) {
        val gettypeFace = ResourcesCompat.getFont(context, value)
        setCustomTypeFace(gettypeFace)
    }


    fun setCustomTypeFace(customTypeFace: Typeface?) {
        this.customTypeFace = customTypeFace
        if (null != customTypeFace) setTypeface(customTypeFace)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, setHeight)
    }

    override fun onDraw(canvas: Canvas) {
        canvas?.save()
        canvas?.skew(-Math.tan(Math.PI * 6.0 / 180).toFloat(), 0f)
        super.onDraw(canvas)
        canvas?.restore()
    }
}