package com.faqun.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.faqun.R
import com.faqun.SecondActivity
import com.wallstreetcn.helper.utils.system.ScreenUtils
import com.wallstreetcn.helper.utils.text.StringUtils


class OverlayService : Service() {
    private var windowManager: WindowManager? = null
    private var floatView: View? = null
    override fun onCreate() {
        super.onCreate()

        // 1. 初始化WindowManager
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager


        // 2. 创建悬浮窗布局参数
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )


        // 3. 设置初始位置（右上角）
        params.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL

        // 4. 加载布局
        floatView = LayoutInflater.from(this).inflate(R.layout.view_float_window, null)

        // 5. 绑定返回按钮事件
        val btnBack: TextView = floatView!!.findViewById(R.id.back)
        btnBack.setOnClickListener { v: View? ->
            isStartFlag = false
            StringUtils.copyToClipboard(FaQunAccessibilityService.logBuilder.toString())
            val intent = Intent(this, SecondActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            // 移除悬浮窗并停止服务
            if (windowManager != null && floatView != null) {
                windowManager?.removeView(floatView)
            }
            stopSelf()
        }

        val start: TextView = floatView!!.findViewById(R.id.start)
        start.setOnClickListener {
            isStartFlag = true
            FaQunAccessibilityService.logBuilder.clear()
            val intent = Intent()
            intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            FaQunAccessibilityService.instance?.startAccessibilityService()

//            params.width = WindowManager.LayoutParams.MATCH_PARENT
//            params.height = WindowManager.LayoutParams.MATCH_PARENT
//            windowManager?.updateViewLayout(floatView, params)
        }

        var stop: TextView = floatView!!.findViewById(R.id.stop)
        stop.setOnClickListener {
            isStartFlag = false
//            params.width = WindowManager.LayoutParams.WRAP_CONTENT
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT
//            windowManager?.updateViewLayout(floatView, params)
        }


        // 6. 添加视图到窗口
        if (Settings.canDrawOverlays(this)) {
            windowManager!!.addView(floatView, params)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}