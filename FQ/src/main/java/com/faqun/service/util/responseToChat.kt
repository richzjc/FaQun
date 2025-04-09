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
import android.view.accessibility.AccessibilityNodeInfo
import com.faqun.service.FaQunAccessibilityService
import com.faqun.service.isStartFlag
import com.pawegio.kandroid.runOnUiThread
import com.wallstreetcn.helper.utils.TLog
import com.wallstreetcn.helper.utils.snack.MToastHelper
import com.wallstreetcn.helper.utils.system.ScreenUtils
import com.wallstreetcn.helper.utils.text.StringUtils
import com.wallstreetcn.rpc.coroutines.requestData
import com.wallstreetcn.webview.faqunText
import kotlinx.coroutines.delay

private val AUDIO_CLICK_FLAG = 0
private val EDITTEXT_CLICK_FLAG = 1

var editNodeInfo: AccessibilityNodeInfo? = null
fun AccessibilityService.responseToChat() {
    requestData {
        val random = (2000..4000).random()
        delay(random.toLong())

//        val random = (2000..4000).random()
//        delay(random.toLong())
//        performGlobalAction(GLOBAL_ACTION_BACK)
//        continueSend()

        if (isStartFlag) {
            editNodeInfo = null
            editNodeInfo = recursionEditNode(rootInActiveWindow)
            if (editNodeInfo != null) {
                responseToClick(editNodeInfo!!, EDITTEXT_CLICK_FLAG)
            } else {
                val node = recursionNode1(rootInActiveWindow)
                if (node != null) {
                    responseToClick(node, AUDIO_CLICK_FLAG)
                } else {
                    isStartFlag = false
                    runOnUiThread {
                        MToastHelper.showToast("未取到输入框按钮")
                    }
                }
            }
        }
    }
}


private fun AccessibilityService.recursionNode1(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
    // 递归子节点
    val screenWidth = ScreenUtils.getScreenWidth()
    val screenHeight = ScreenUtils.getScreenHeight()
    for (i in 0 until rootNode.getChildCount()) {
        val node = rootNode.getChild(i)
        try {
            val className: String = node.className.toString() // 控件类名
            val rect = Rect()
            node.getBoundsInScreen(rect)
            if (rect.left < screenWidth / 5 && rect.right < screenWidth / 5 && rect.top > screenHeight * 0.5 && TextUtils.equals(
                    className,
                    "android.widget.ImageButton"
                )
            ) {
                return node
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val childNode = recursionNode1(node)
        if (childNode != null)
            return childNode
    }
    return null;
}

fun AccessibilityService.recursionEditNode(listNodes: AccessibilityNodeInfo): AccessibilityNodeInfo? {
    if (listNodes != null && listNodes.childCount > 0) {
        (0 until listNodes.childCount).forEach {
            val node = listNodes.getChild(it)
            val className: String = node.className.toString() // 控件类名
            if (TextUtils.equals(className, "android.widget.EditText"))
                return node
            else {
                val nodel = recursionEditNode(node)
                if (nodel != null)
                    return nodel
            }
        }
    }
    return null
}

private fun checkContainsEditText(contactNodes: List<AccessibilityNodeInfo>): Boolean {
    if (contactNodes == null || contactNodes.size <= 0)
        return false
    contactNodes.forEach {
        val className = it.className.toString()
        if (TextUtils.equals(className, "android.widget.EditText"))
            return true
    }
    return false
}

private fun AccessibilityService.responseToSetText(editNodeInfo: AccessibilityNodeInfo) {
    requestData {
        var contactNodes = rootInActiveWindow.findAccessibilityNodeInfosByText(faqunText)
        while (!checkContainsEditText(contactNodes) && !TextUtils.equals(
                recursionEditNode(
                    rootInActiveWindow
                )?.text, faqunText
            )
        ) {
            val arguments = Bundle()
            arguments.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                faqunText
            )
            editNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
            val duration = (300..500).random()
            delay(duration.toLong())
            contactNodes = rootInActiveWindow.findAccessibilityNodeInfosByText(faqunText)
        }

        if (isStartFlag) {
            val duration = (100..500).random()
            delay(duration.toLong())
            performGlobalAction(GLOBAL_ACTION_BACK)
            responseToSendMsg()
        }
    }
}

private fun AccessibilityService.responseToClick(node: AccessibilityNodeInfo, flag: Int) {
    // 执行点击操作
    val rect = Rect()
    node!!.getBoundsInScreen(rect)
    val x = (rect.left + 1..<rect.right).random()
    val y = (rect.top + 1..<rect.bottom).random()
    simulateClick(x.toFloat(), y.toFloat(), flag)
}

private fun AccessibilityService.simulateClick(x: Float, y: Float, flag: Int) {
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
            requestData {
                val random = (1500 .. 2000).random()
                delay(random.toLong())
                if (flag == AUDIO_CLICK_FLAG) {
                    val editNodeInfo = recursionEditNode(rootInActiveWindow)
                    if (editNodeInfo != null) {
                        responseToClick(editNodeInfo, EDITTEXT_CLICK_FLAG)
                    }
                } else if (flag == EDITTEXT_CLICK_FLAG) {
                    val editNodeInfo = recursionEditNode(rootInActiveWindow)
                    responseToSetText(editNodeInfo!!)
                }
            }
        }

        override fun onCancelled(gesture: GestureDescription) {
            super.onCancelled(gesture) // 取消时的回调（可选）
        }
    }, null)
}

