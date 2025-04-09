package com.faqun.service.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GestureResultCallback
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.graphics.Path
import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.faqun.service.isStartFlag
import com.pawegio.kandroid.runOnUiThread
import com.wallstreetcn.helper.utils.TLog
import com.wallstreetcn.helper.utils.snack.MToastHelper
import com.wallstreetcn.rpc.coroutines.requestData
import kotlinx.coroutines.delay

fun AccessibilityService.responseToContact() {
    // 获取当前窗口根节点
    rootInActiveWindow ?: return
    requestData {
        val random = (2000 .. 3000).random()
        delay(random.toLong())
        // 通过resource-id定位通讯录按钮
        var node: AccessibilityNodeInfo? = null
        val nodeList = rootInActiveWindow.findAccessibilityNodeInfosByText("通讯录")
        if (nodeList != null && nodeList.size > 0)
            node = nodeList.first()
        else {
            val list = ArrayList<AccessibilityNodeInfo>()
            recursionSendNode(rootInActiveWindow, "android.widget.TextView", "通讯录", list)
            if(list.size > 0)
                node = list.first()
        }

        if (node != null) {
            val rect = Rect()
            node!!.getBoundsInScreen(rect)
            val x = (rect.left + 1..<rect.right).random()
            val y = (rect.top + 1..<rect.bottom).random()
            simulateClick(x.toFloat(), y.toFloat())
        } else {
            isStartFlag = false
            runOnUiThread {
                MToastHelper.showToast("未取到通讯录按钮")
            }
        }
    }
}

private fun AccessibilityService.simulateClick(x: Float, y: Float) {
    val path = Path()
    path.moveTo(x, y) // 移动到点击的起点（通常是点击位置）
    path.lineTo(x, y) // 再次画线到点击位置，形成一个点（实际上不需要移动，但为了完整性）
    val startTime = (0 .. 100).random()
    val duration = (1.. 50).random()
    val stroke = StrokeDescription(path, startTime.toLong(), duration.toLong()) // 创建笔画描述，0为延迟时间，1为持续时间（毫秒）
    val gesture = GestureDescription.Builder().addStroke(stroke).build() // 创建手势描述并构建手势对象
    dispatchGesture(gesture, object : GestureResultCallback() {
        // 派发手势并监听结果回调
        override fun onCompleted(gesture: GestureDescription) {
            super.onCompleted(gesture) // 完成时的回调（可选）
            if (isStartFlag) {
                responseToQunLiao()
            }
        }

        override fun onCancelled(gesture: GestureDescription) {
            super.onCancelled(gesture) // 取消时的回调（可选）
        }
    }, null)
}