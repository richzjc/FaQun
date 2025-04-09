package com.wallstreetcn.baseui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import com.wallstreetcn.helper.utils.system.ScreenUtils

class LiveShareTotalLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    var view1: LiveShareLayout? = null
    var view2: LiveShareLayout? = null
    var rectColor: Int = Color.BLACK

    private val setHeight = ScreenUtils.dip2px(22f)
    val gap by lazy {
        Math.tan(Math.PI * 12.0 / 180) * setHeight
    }

    init {
        setWillNotDraw(false)
        orientation = HORIZONTAL
        view1 = LiveShareLayout(getContext())
        view2 = LiveShareLayout(getContext())
        addView(view1)
        addView(view2)
    }

    fun bindText(leftFontId: Int, rightFontId: Int, leftText: String, rightText: String) {
        view1?.rectColor = Color.parseColor("#1478f0")
        view2?.rectColor = Color.parseColor("#59000000")
        view1?.bindText(leftFontId, leftText)
        view2?.bindText(rightFontId, rightText)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (view1 != null && view2 != null) {
            val view1Width = view1!!.measuredWidth
            val view1Height = view1!!.measuredHeight

            view1?.layout(0, 0, view1Width, view1Height)

            view2?.layout(
                (view1Width - gap).toInt(),
                0,
                (view1Width - gap).toInt() + view2!!.measuredWidth,
                view2!!.measuredHeight
            )
        }
    }


}