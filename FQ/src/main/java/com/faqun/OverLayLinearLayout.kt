package com.faqun

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import com.faqun.service.isStartFlag
import com.wallstreetcn.helper.utils.TLog

class OverLayLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){
            TLog.e("overlay", "x = ${event?.rawX}; y = ${event?.getRawY()}")
//            if(isStartFlag){
//                findViewById<TextView>(R.id.locate).text = "x = ${event?.rawX}; y = ${event?.getRawY()}"
//            }
        }
        return super.onTouchEvent(event)
    }
}