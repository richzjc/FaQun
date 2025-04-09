package com.wallstreetcn.global.utils

import android.content.Context
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

object LubanUtils {
    const val SUCCESS = 0
    const val ERROR = -1
    const val START = 1
    fun launch(
        context: Context?,
        path: String,
        ignoreSize: Int = 400,
        callback: (code: Int, obj: Any?) -> Unit
    ) {
        Luban.with(context)
            .load(path)
            .ignoreBy(ignoreSize)
            .filter {
                !it.endsWith(".gif")
            }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                    callback(START, null)
                }

                override fun onSuccess(file: File?) {
                    callback(SUCCESS, file)
                }

                override fun onError(e: Throwable) {
                    callback(ERROR, e)
                }
            })
            .launch()
    }
}

