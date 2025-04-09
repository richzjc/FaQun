package com.wallstreetcn.global.flyer

import android.content.Context

interface FlyerTrace {

    fun purchase(context: Context?, price: Double?, category_type: String? = "", category_id: String? = "")

    fun event()
}