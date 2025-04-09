package com.faqun

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AlertDialog
import com.faqun.databinding.ActivitySecondBinding
import com.faqun.service.FaQunAccessibilityService
import com.faqun.service.OverlayService
import com.faqun.service.isStartFlag
import com.richzjc.observer.Observer
import com.richzjc.observer.ObserverManger
import com.wallstreetcn.baseui.base.BaseActivity
import com.wallstreetcn.baseui.base.BasePresenter
import com.wallstreetcn.helper.utils.InstallUtil
import com.wallstreetcn.helper.utils.SharedPrefsUtil
import com.wallstreetcn.helper.utils.observer.ObserverIds
import com.wallstreetcn.helper.utils.snack.MToastHelper
import com.wallstreetcn.webview.faqunText


class SecondActivity : BaseActivity<Any, BasePresenter<Any>>(), Observer {

    private var timeGap: String? = "1,4"
    override fun doInitSubViews(view: View?) {
        setStatusBarTranslucentCompat()
        super.doInitSubViews(view)
        ObserverManger.getInstance().registerObserver(this, ObserverIds.UPDATE_TIME_GAP)
        binding.sendText.text = faqunText
        initPermission()
        responsePermissionClick()
        binding.titlebar.setRightBtn2OnclickListener {
            finish()
        }

        binding.timeGap.setOnClickListener {
            val intent = Intent(this, TimeGapActivity::class.java)
            startActivity(intent)
        }

        timeGap = SharedPrefsUtil.getString("timeGap", "1,4")
        val splitArr = timeGap!!.split(",")
        binding.timeGap.setRightText("随机${splitArr[0]}~${splitArr[1]}秒")


        binding.sendToChat.setOnClickListener {
            responseToChat()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Settings.canDrawOverlays(this)) {
            binding.openFloatPermission.setRightText("已开启")
        } else {
            binding.openFloatPermission.setRightText("未开启")
        }

        if (isAccessibilityServiceEnabled(this)) {
            binding.openHelpPermission.setRightText("已开启")
        } else {
            binding.openHelpPermission.setRightText("未开启")
        }
    }

    private fun initPermission() {
        if (Settings.canDrawOverlays(this)) {
            binding.openFloatPermission.visibility = View.GONE
        } else {
            binding.openFloatPermission.visibility = View.VISIBLE
            binding.openFloatPermission.setRightText("未开启")
        }

        if (isAccessibilityServiceEnabled(this)) {
            binding.openHelpPermission.visibility = View.GONE
        } else {
            binding.openHelpPermission.visibility = View.VISIBLE
            binding.openHelpPermission.setRightText("未开启")
        }
    }

    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (enabledServices == null) return false


        // 构造完整的组件名（包名+类名）
        val targetComponent = ComponentName(context, FaQunAccessibilityService::class.java)
        val flattenedName = targetComponent.flattenToString()
        return enabledServices.contains(flattenedName)
    }


    private fun responsePermissionClick() {
        binding.openFloatPermission.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                AlertDialog.Builder(this)
                    .setTitle("开启悬浮窗")
                    .setMessage(
                        "用于显示悬浮窗，目的是为了帮助您通过悬浮面板实现快捷操作。您可以随时在系统设置中关闭该权限，您取消" +
                                "授权会导致您无法使用所有需要悬浮窗权限的相关功能，但不会导致您无法使用其它功能。"
                    )
                    .setPositiveButton("去开启") { _, _ ->
                        val drawOverlaysSettingsIntent =
                            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        drawOverlaysSettingsIntent.setData(Uri.parse("package:" + this.getPackageName()))
                        this.startActivity(drawOverlaysSettingsIntent)
                    }
                    .show()
            }
        }

        binding.openHelpPermission.setOnClickListener {
            if (!isAccessibilityServiceEnabled(this)) {
                AlertDialog.Builder(this)
                    .setTitle("开启辅助功能权限")
                    .setMessage(
                        "用于实现智能化操作，解放双手，代替您执行重复性的操作。\n\n" +
                                "操作说明：点击去开启——>点击已下载的应用——>点击发群app并打开"
                    )
                    .setPositiveButton("去开启") { _, _ ->
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    }
                    .show()
            }
        }
    }

    private fun responseToChat() {
        if (TextUtils.isEmpty(faqunText)) {
            MToastHelper.showToast("请先输入消息文本")
        } else if (!Settings.canDrawOverlays(this)) {
            MToastHelper.showToast("请先开启悬浮窗权限")
        } else if (!isAccessibilityServiceEnabled(this)) {
            MToastHelper.showToast("请先开启辅助功能权限")
        } else if (InstallUtil.isInstallWX(this, true)) {
            isStartFlag = false
            val intent = packageManager.getLaunchIntentForPackage("com.tencent.mm")
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                startService(Intent(this, OverlayService::class.java))
            }
        }
    }

    private lateinit var binding: ActivitySecondBinding

    override fun doGetContentView(): View {
        binding = ActivitySecondBinding.inflate(LayoutInflater.from(this))
        return binding.root
    }

    override fun update(p0: Int, vararg p1: Any?) {
        if (p0 == ObserverIds.UPDATE_TIME_GAP && p1 != null) {
            timeGap = p1!![0].toString()
            val splitArr = timeGap!!.split(",")
            binding.timeGap.setRightText("随机${splitArr[0]}~${splitArr[1]}秒")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverManger.getInstance().removeObserver(this)
    }
}