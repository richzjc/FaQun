package com.faqun.service.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GestureResultCallback
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.graphics.Path
import android.graphics.Rect
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.faqun.service.FaQunAccessibilityService
import com.faqun.service.isStartFlag
import com.pawegio.kandroid.runOnUiThread
import com.wallstreetcn.helper.utils.SharedPrefsUtil
import com.wallstreetcn.helper.utils.snack.MToastHelper
import com.wallstreetcn.helper.utils.system.ScreenUtils
import com.wallstreetcn.rpc.coroutines.requestData
import kotlinx.coroutines.delay
import kotlin.math.abs


var lastClickX = 0f
var lastClickY = 0f
var firstNodeRect: Rect? = null

var lastClickNode: AccessibilityNodeInfo? = null
fun AccessibilityService.responseToQunLiaoList() {
    lastClickNode = null
    requestData {
        val random = (2000..3000).random()
        delay(random.toLong())
        if (isStartFlag) {
            //1 获取第一个节点的坐标
            FaQunAccessibilityService.logBuilder.append("1\n")
            firstNodeRect = getFirstNodeRect()
            responseToClickNextChat()
        }
    }
}

private fun AccessibilityService.responseToClickNextChat() {
    if (!isStartFlag)
        return
    FaQunAccessibilityService.logBuilder.append("2\n")
    val listNode = getListViewNode(rootInActiveWindow)
    if (listNode != null && listNode.childCount > 0) {
        FaQunAccessibilityService.logBuilder.append("3\n")
        if (firstNodeRect != null) {
            FaQunAccessibilityService.logBuilder.append("4\n")
            val x = (firstNodeRect!!.left + 1..<firstNodeRect!!.right).random()
            val y = (firstNodeRect!!.top + 1..<firstNodeRect!!.bottom).random()
            simulateClick(x.toFloat(), y.toFloat())
        }
    }
}

private fun AccessibilityService.getFirstNodeRect(): Rect? {
    val listViewNode = getListViewNode(rootInActiveWindow) ?: return null
    val childCount = listViewNode.childCount
    if (childCount <= 0)
        return null
    val lastNode = listViewNode.getChild(0)
    val rect = Rect()
    lastNode.getBoundsInScreen(rect)
    return rect
}

private fun AccessibilityService.isScrollBottom(): Boolean {
    val listViewNode = getListViewNode(rootInActiveWindow) ?: return true
    val childCount = listViewNode.childCount
    if (childCount <= 0)
        return true
    val lastNode = listViewNode.getChild(childCount - 1)
    val screenHeight = ScreenUtils.getScreenHeight()
    val rect = Rect()
    lastNode.getBoundsInScreen(rect)

    return !TextUtils.equals(
        lastNode.className.toString(),
        "android.widget.LinearLayout"
    ) && (rect.top <= screenHeight || rect.bottom <= screenHeight)
}

private fun AccessibilityService.getListViewNode(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
    // 递归子节点
    for (i in 0 until rootNode.getChildCount()) {
        val node = rootNode.getChild(i)
        val className: String = node.className.toString() // 控件类名
        FaQunAccessibilityService.logBuilder.append("${className}\n")
        if (TextUtils.equals(className, "android.widget.ListView")) {
            return node
        } else {
            val result = getListViewNode(node)
            if (result != null)
                return result
        }
    }
    return null
}

suspend fun AccessibilityService.continueSend() {
    if (!isStartFlag)
        return

    val random = (1500..2000).random()
    delay(random.toLong())
    //2 判断是否滑动到底部
    if (isScrollBottom()) {
        realSendMsg()
    } else {
        simulateScroll()
    }
}

fun AccessibilityService.realSendMsg() {
    requestData {
        val timeGap = SharedPrefsUtil.getString("timeGap", "1,4")
        val arr = timeGap.split(",")
        var start = arr.get(0).toInt() * 1000L
        var end = arr.get(1).toInt() * 1000L
        val random = (start..end).random()
        delay(random)

        if (isStartFlag && firstNodeRect != null) {
            val listViewNode = getListViewNode(rootInActiveWindow)
            if (listViewNode != null) {
                val childCount = listViewNode.childCount
                if (childCount > 0) {
                    var node: AccessibilityNodeInfo? = null
                    var nodeRect: Rect? = null
                    (0 until childCount)?.forEach {
                        if (node == null) {
                            val childNode = listViewNode.getChild(it)
                            val childRect = Rect()
                            childNode.getBoundsInScreen(childRect)
                            if (childRect.top >= 0 && childRect.top > lastClickY) {
                                node = childNode
                                nodeRect = childRect
                            }
                        }
                    }

                    if (nodeRect != null) {
                        val x = (nodeRect!!.left + 1..<nodeRect!!.right).random()
                        val y = (nodeRect!!.top + 1..<nodeRect!!.bottom).random()

                        simulateClick(
                            x.toFloat(),
                            y.toFloat()
                        )
                    }
                }
            }
        }
    }
}

private fun AccessibilityService.checkClickInArea(x: Float, y: Float): Boolean {
    val listViewNode = getListViewNode(rootInActiveWindow) ?: return false
    val childCount = listViewNode.childCount
    if (childCount <= 0)
        return false

    var screenHeight = ScreenUtils.getScreenHeight()
    if (y > screenHeight)
        return false

    val listRect = Rect()
    listViewNode.getBoundsInScreen(listRect)
    if (listRect.bottom > 0 && y >= listRect.bottom)
        return false

    val lastNode = listViewNode.getChild(childCount - 1)
    if (!TextUtils.equals(lastNode.className.toString(), "android.widget.LinearLayout")) {
        val rect = Rect()
        lastNode.getBoundsInScreen(rect)
        if (rect.top >= 0 && rect.bottom > 0 && y >= rect.top && y <= rect.bottom)
            return false
    }

    return true
}

private fun AccessibilityService.simulateClick(x: Float, y: Float) {
    lastClickX = x
    lastClickY = y
    Log.e(
        "simulateClick",
        "x = ${x}; y = ${y}; firstNodeHeight = ${abs(firstNodeRect!!.top - firstNodeRect!!.bottom)}"
    )
    if (checkClickInArea(x, y)) {
        FaQunAccessibilityService.logBuilder.append("5\n")
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
                responseToChat()
            }

            override fun onCancelled(gesture: GestureDescription) {
                super.onCancelled(gesture) // 取消时的回调（可选）
            }
        }, null)
    } else {
        isStartFlag = false
        runOnUiThread {
            MToastHelper.showToast("发送完成")
        }
    }
}


private fun AccessibilityService.simulateScroll() {
    if (firstNodeRect != null) {
        val height = abs(firstNodeRect!!.bottom - firstNodeRect!!.top)
        val rect = Rect()
        val listNode = getListViewNode(rootInActiveWindow) ?: return
        listNode.getBoundsInScreen(rect)

        val path = Path()
        val startX = (rect.left + 1..<rect.right).random()
        val y0 = (rect.bottom + rect.top) / 2
        val y1 = (y0 + rect.bottom) / 2
        val startY = (y0..y1).random()
        val endY = startY - height
        path.moveTo(startX.toFloat(), startY.toFloat())
        path.lineTo(startX.toFloat(), endY.toFloat())
        val builder = GestureDescription.Builder()
        val random = (400..600).random()
        val startTime = (0 .. 100).random()
        builder.addStroke(StrokeDescription(path, startTime.toLong(), random.toLong()))
        val gesture = builder.build()
        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                scrollClick()
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
            }
        }, null)
    }
}

private fun AccessibilityService.scrollClick() {
    requestData {
        if (firstNodeRect != null) {
            val random = (1000..2000).random()
            delay(random.toLong())
            val x = (firstNodeRect!!.left + 1..<firstNodeRect!!.right).random()
            val y = (firstNodeRect!!.top + 1..<firstNodeRect!!.bottom).random()
            simulateClick(
                x.toFloat(),
                y.toFloat()
            )
        }
    }
}