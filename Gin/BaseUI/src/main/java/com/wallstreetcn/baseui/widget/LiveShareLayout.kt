package com.wallstreetcn.baseui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.LinearLayout

class LiveShareLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    var view1: LiveShareIconView? = null
    var rectColor: Int = Color.BLACK

    var text : String? = ""
    set(value) {
        field = value
        view1?.setText(value)
    }

    init {
        setWillNotDraw(false)
        orientation = HORIZONTAL
        view1 = LiveShareIconView(getContext())
        addView(view1)
    }

    private val paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint
    }

    fun bindText(leftFontId : Int, leftText : String){
        view1?.fontId = leftFontId
        view1?.text = leftText
    }


    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        val gap = Math.tan(Math.PI * 12.0 / 180) * measuredHeight
        paint.color = rectColor
        val path = Path()

        var startX = gap.toFloat()
        var startY = 0f
        path.moveTo(startX, startY)

        startX = measuredWidth.toFloat()
        startY = 0f

        path.lineTo(startX, startY)

        startX = measuredWidth.toFloat() - gap.toFloat()
        startY = measuredHeight.toFloat()

        path.lineTo(startX, startY)

        startX = 0f
        startY = measuredHeight.toFloat()

        path.lineTo(startX, startY)

        canvas?.drawPath(path, paint)


    }

}