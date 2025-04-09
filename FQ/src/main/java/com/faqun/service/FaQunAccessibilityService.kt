package com.faqun.service

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.faqun.service.util.responseToChat
import com.faqun.service.util.responseToContact
import com.faqun.service.util.responseToQunLiaoList
import com.faqun.service.util.responseToSendMsg
import com.wallstreetcn.rpc.coroutines.requestData
import kotlinx.coroutines.delay

var isStartFlag = false

class FaQunAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    fun startAccessibilityService() {
        isStartFlag = true
        requestData {
            val random = (1000 .. 2000).random()
            delay(random.toLong())
            if (isStartFlag) {
                responseToContact()
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        val rect = Rect()
        rootInActiveWindow.getBoundsInScreen(rect)
    }

    override fun onInterrupt() {

    }

    companion object {
        var instance: FaQunAccessibilityService? = null
        var logBuilder = StringBuilder()
    }
}