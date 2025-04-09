package com.wallstreetcn.global.flyer

import com.wallstreetcn.baseui.manager.AppManager

object FlyerBridge {
    private var mTrace: FlyerTrace? = null
    @JvmStatic
    fun register(trace: FlyerTrace?) {
        mTrace = trace
    }

    @JvmStatic
    fun purchase(price: Double?, category_type: String? = "", category_id: String? = "") {
        mTrace?.purchase(AppManager.getAppManager().topActivity, price, category_type, category_id)
    }
}