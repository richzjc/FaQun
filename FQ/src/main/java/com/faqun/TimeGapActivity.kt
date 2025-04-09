package com.faqun

import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import com.faqun.databinding.ActivitySecondBinding
import com.faqun.databinding.ActivityTimeGapBinding
import com.richzjc.observer.ObserverManger
import com.wallstreetcn.baseui.base.BaseActivity
import com.wallstreetcn.baseui.base.BasePresenter
import com.wallstreetcn.helper.utils.SharedPrefsUtil
import com.wallstreetcn.helper.utils.observer.ObserverIds
import com.wallstreetcn.webview.faqunText

class TimeGapActivity : BaseActivity<Any, BasePresenter<Any>>() {
    val timeList = listOf("1,4", "2,6", "3,8", "4,10", "5,12")
    var selectView: LinearLayout? = null
    var llList : List<LinearLayout>? = null

    override fun doInitSubViews(view: View?) {
        setStatusBarTranslucentCompat()
        super.doInitSubViews(view)
        llList = listOf(
            binding.llFirst,
            binding.llSecond,
            binding.llThird,
            binding.llForth,
            binding.llFifth
        )
        val timeGap = SharedPrefsUtil.getString("timeGap", "1,4")
        val index = timeList.indexOf(timeGap)
        selectView = llList?.get(index)
        (selectView?.getChildAt(0) as? CheckBox)?.isChecked = true

        llList?.forEach { inner->
            inner.setOnClickListener {
                (selectView?.getChildAt(0) as? CheckBox)?.isChecked = false
                selectView = inner
                (selectView?.getChildAt(0) as? CheckBox)?.isChecked = true
            }
        }

        binding.confirm.setOnClickListener {
            val index = llList?.indexOf(selectView) ?: 0
            val timeGap = timeList.get(index)
            SharedPrefsUtil.saveString("timeGap", timeGap)
            ObserverManger.getInstance().notifyObserver(ObserverIds.UPDATE_TIME_GAP, timeGap)
            finish()
        }
    }

    private lateinit var binding: ActivityTimeGapBinding

    override fun doGetContentView(): View {
        binding = ActivityTimeGapBinding.inflate(LayoutInflater.from(this))
        return binding.root
    }
}