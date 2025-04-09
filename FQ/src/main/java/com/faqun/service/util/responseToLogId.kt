package com.faqun.service.util

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.faqun.service.FaQunAccessibilityService

fun AccessibilityService.logId(node: AccessibilityNodeInfo) {
    val nodeCLassName = node.className?.toString()
    val nodeText = node.text?.toString()
    val nodeId = node.viewIdResourceName?.toString()
    val rect = Rect()
    node.getBoundsInScreen(rect)
    if (rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0)
        Log.e(
            "logId",
            "left = ${rect.left}; top = ${rect.top}; right = ${rect.right}; bottom = ${rect.bottom}, className = ${nodeCLassName}; text = ${nodeText}; id = ${nodeId}"
        )
    if (node.childCount > 0) {
        (0 until node.childCount)?.forEach {
            logId(node.getChild(it))
        }
    }
}