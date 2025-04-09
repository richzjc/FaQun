package com.wallstreetcn.helper.utils

import android.text.TextUtils
import com.wallstreetcn.helper.utils.system.EquipmentUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * 1.版本更新弹出
 * 2.不同意后，隔天弹出
 */
fun checkIsShowUserPrivacy(): Boolean {
    val notAgree = SharedPrefsUtil.getBoolean("userPrivacy", true)
    val currentVersionCode = getCurrentUserPrivacyKey()
    val getLastTime = SharedPrefsUtil.getString("lastDate", "")
    val currentDate = getCurrentDate()
    val previousKey = SharedPrefsUtil.getString("user_privacy_key", "")

    if (notAgree) {
        return !TextUtils.equals(previousKey, "$currentVersionCode")
                || !TextUtils.equals(getLastTime, currentDate)
    } else {
        return !TextUtils.equals(previousKey, "$currentVersionCode")
    }
}

fun getCurrentUserPrivacyKey(): String? {
    return "${EquipmentUtils.getVersionCode()}"
}

fun getCurrentDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(Date(System.currentTimeMillis()))
}

fun checkIsShowGuideActivity(): Boolean {
    val lastVersionCode = SharedPrefsUtil.getInt("versionCode", -1)
    val newVersionCode = EquipmentUtils.getVersionCode()
    SharedPrefsUtil.saveInt("versionCode", newVersionCode)
    return lastVersionCode != newVersionCode
}