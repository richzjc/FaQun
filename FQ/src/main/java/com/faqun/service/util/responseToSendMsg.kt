package com.faqun.service.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK
import android.accessibilityservice.AccessibilityService.GestureResultCallback
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.faqun.service.FaQunAccessibilityService
import com.faqun.service.isStartFlag
import com.pawegio.kandroid.runOnUiThread
import com.wallstreetcn.helper.utils.snack.MToastHelper
import com.wallstreetcn.rpc.coroutines.requestData
import com.wallstreetcn.webview.faqunText
import kotlinx.coroutines.delay


suspend fun AccessibilityService.responseToSendMsg() {
    rootInActiveWindow ?: return
    if (!isStartFlag) return

    val random = (1000 .. 2000).random()
    delay(random.toLong())
    var node: AccessibilityNodeInfo? = null
    val nodeList = rootInActiveWindow.findAccessibilityNodeInfosByText("发送")
    if (nodeList != null && nodeList.size > 0)
        node = nodeList.last()
    else {
        val list = ArrayList<AccessibilityNodeInfo>()
        recursionSendNode(rootInActiveWindow, "android.widget.Button", "发送", list)
        if(list.size > 0)
            node = list.last()
    }

    if (node != null) {
        // 执行点击操作
        val rect = Rect()
        node.getBoundsInScreen(rect)
        val x = (rect.left + 1..<rect.right).random()
        val y = (rect.top + 1..<rect.bottom).random()
        simulateClick(x.toFloat(), y.toFloat())

    } else {
        isStartFlag = false
        runOnUiThread {
            MToastHelper.showToast("未取到发送按钮")
        }
    }
}


fun AccessibilityService.recursionSendNode(
    node: AccessibilityNodeInfo,
    className: String,
    text: String,
    list: ArrayList<AccessibilityNodeInfo>
) {
    val nodeCLassName = node.className?.toString()
    val nodeText = node.text?.toString()
    val rect = Rect()
    node.getBoundsInScreen(rect)
    if (rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0)
        Log.e(
            "logId",
            "left = ${rect.left}; top = ${rect.top}; right = ${rect.right}; bottom = ${rect.bottom}, className = ${nodeCLassName}; text = ${nodeText}; "
        )

    if (TextUtils.equals(className, nodeCLassName)) {
        val rect = Rect()
        node.getBoundsInScreen(rect)
        if (rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0 && TextUtils.equals(
                text?.trim(),
                nodeText?.trim()
            )
        ) {
            list?.add(node)
        }
    }



    if (node.childCount > 0) {
        (0 until node.childCount)?.forEach {
            recursionSendNode(node.getChild(it), className, text, list)
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
            responseToBack()
        }

        override fun onCancelled(gesture: GestureDescription) {
            super.onCancelled(gesture) // 取消时的回调（可选）
        }
    }, null)
}

private fun AccessibilityService.responseToBack() {
    requestData {
        val random = (1500 .. 2000).random()
        delay(random.toLong())
        performGlobalAction(GLOBAL_ACTION_BACK)
        continueSend()
    }
}
