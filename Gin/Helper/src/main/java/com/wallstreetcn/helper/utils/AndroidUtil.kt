package com.wallstreetcn.helper.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.EXTRA_APP_PACKAGE
import android.provider.Settings.EXTRA_CHANNEL_ID
import androidx.core.app.NotificationManagerCompat
import androidx.appcompat.app.AlertDialog
import android.text.TextUtils
import android.widget.Toast
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException


object AndroidUtil {

//    /**
//     * 设备唯一ID
//     * 目前stackoverflow用的比较多的一种方法
//     *
//     * @return
//     */
//    //13 位
//    //serial需要一个初始化
//    //
//    @JvmStatic
//    val uniqueDeviceID: String
//        get() {
//            var serial: String? = null
//
//            val m_szDevIDShort = "35" +
//                    Build.BOARD.length % 10 + Build.BRAND.length % 10 +
//
//                    Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
//
//                    Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
//
//                    Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
//
//                    Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
//
//                    Build.TAGS.length % 10 + Build.TYPE.length % 10 +
//
//                    Build.USER.length % 10
//
//            return try {
//                serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
//                "android-" + UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
//            } catch (exception: Exception) {
//                serial = "serial"
//                "android-" + UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
//            }
//
//        }

    /**
     * 获取本机IP地址
     *
     * @return
     */
    @JvmStatic
    val ipAddressString: String
        get() {
            try {
                val enNetI = NetworkInterface
                        .getNetworkInterfaces()
                while (enNetI.hasMoreElements()) {
                    val netI = enNetI.nextElement()
                    val enumIpAddr = netI
                            .inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (inetAddress is Inet4Address && !inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress()
                        }
                    }
                }
            } catch (e: SocketException) {
                e.printStackTrace()
            }

            return ""
        }

    /**
     * 适用于打开内部页面
     *
     * @param context
     * @param pkg
     * @param cls
     */
    fun startActivityByComponentName(context: Context, pkg: String, cls: String) {
        try {
            val componentName = ComponentName(pkg, cls)
            val intent = Intent()
            intent.component = componentName
            intent.action = Intent.ACTION_VIEW
            context.startActivity(intent)
        } catch (e: Exception) {
            e.message
        }

    }

    /**
     * 根据包名打开某个app
     *
     * @param context
     * @param pkg
     */
    fun startActivityByPkgManager(context: Context, pkg: String) {
        val packageManager = context.packageManager
        var intent: Intent? = Intent()
        try {
            intent = packageManager.getLaunchIntentForPackage(pkg)
        } catch (e: Exception) {
            e.message
        }

        context.startActivity(intent)
    }


    fun checkApkInstall(context: Context, pkgName: String): Boolean {
        var packageInfo: PackageInfo?
        try {
            packageInfo = context.packageManager.getPackageInfo(
                    pkgName, 0)
        } catch (e: NameNotFoundException) {
            packageInfo = null
            e.printStackTrace()
        }

        return packageInfo != null
    }

    /**
     * 检查服务是否运行
     *
     * @param context
     * @param className
     * @return
     */
    fun isServiceRunning(context: Context, className: String): Boolean {
        var isRunning = false
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val serviceList = activityManager.getRunningServices(Integer.MAX_VALUE)
        if (serviceList == null || serviceList.isEmpty())
            return false
        for (i in serviceList.indices) {
            if (serviceList[i].service.className == className && TextUtils.equals(
                            serviceList[i].service.packageName, context.packageName)) {
                isRunning = true
                break
            }
        }
        return isRunning
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    fun getPackageInfo(context: Context): PackageInfo {
        var info: PackageInfo? = null
        try {
            info = context.packageManager.getPackageInfo(
                    context.packageName, 0)
        } catch (e: NameNotFoundException) {
            e.printStackTrace(System.err)
        }

        if (info == null)
            info = PackageInfo()
        return info
    }

    @JvmStatic
    fun rateApp(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=" + context.packageName)
            //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
            //存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
            if (intent.resolveActivity(context.packageManager) != null) { //可以接收
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "调用打分失败", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "调用打分失败", Toast.LENGTH_SHORT).show()
        }

    }

    fun showNotifySettingDialog(context: Context, currentFinish: Boolean = false) {
        val dialog = AlertDialog.Builder(context)
                .setTitle("您的通知功能还未开启")
                .setMessage("如果您希望免费使用预警提醒功能，请在手机“设置-通知”中找到选股宝，并打开通知功能。")
                .setPositiveButton("立即开启") { dialog, which ->
                    openSystemNotifySetting(context)
                }
                .setNegativeButton("下次再说") { dialog, which ->
                    if (currentFinish) {
                        if (context is Activity) {
                            context.finish()
                        }
                    }
                }
                .create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    fun areNotifyEnable(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }


    fun openSystemNotifySetting(context: Context) {

        try {
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    intent.putExtra(EXTRA_APP_PACKAGE, context.packageName)
                    intent.putExtra(EXTRA_CHANNEL_ID, context.applicationInfo.uid)
                }
                "MI 6" == Build.MODEL -> {
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                }
                else -> {
                    intent.putExtra("app_package", context.packageName)
                    intent.putExtra("app_uid", context.applicationInfo.uid)
                }
            }
            context.startActivity(intent)

        } catch (e: java.lang.Exception) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    }
}
