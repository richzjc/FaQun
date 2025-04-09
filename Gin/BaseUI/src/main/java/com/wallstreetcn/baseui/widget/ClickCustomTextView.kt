package com.wallstreetcn.baseui.widget

import android.content.Context
import android.text.Spannable
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class ClickCustomTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)
            return super.dispatchTouchEvent(event)
        else {
            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= totalPaddingLeft
            y -= totalPaddingTop

            x += scrollX
            y += scrollY

            val layout = getLayout()
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val mSpannable = if (text is Spannable) text as Spannable else null
            val link = mSpannable?.getSpans(off, off, ClickableSpan::class.java)
            if (link != null && link.isNotEmpty())
                return super.dispatchTouchEvent(event)
            else
                return false
        }
    }
}