package com.wallstreetcn.helper.utils.rx

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadPoolShared {
    private var MAX_PROCESS = Runtime.getRuntime().availableProcessors()
    private var SERVICE = ThreadPoolExecutor(MAX_PROCESS * 2, MAX_PROCESS * 4,
            60, TimeUnit.SECONDS, LinkedBlockingDeque(MAX_PROCESS * 16), ThreadPoolExecutor.DiscardPolicy())

    @JvmStatic
    fun get(): ThreadPoolExecutor {
        return SERVICE
    }
}