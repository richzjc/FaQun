package com.wallstreetcn.global.queue

import android.text.TextUtils
import com.wallstreetcn.rpc.coroutines.requestData

object MainDialogQueue {
    private var curRequest: DialogMessageRequest? = null
    private var tailRequest: DialogMessageRequest? = null
    var isStart = false
    private var isWhileStart = false

    fun start() {
        if (isWhileStart)
            return
        isWhileStart = true
        requestData {
            while (curRequest != null && (isStart || curRequest!!.isDirectShow) && !curRequest!!.isAdded) {
                curRequest?.deliverResponse()
                curRequest = curRequest?.next
            }
            isWhileStart = false
        }
    }

    private fun pause() = curRequest?.pause()

    fun checkExist(request: DialogMessageRequest) : Boolean{
        var tempRequest = curRequest
        while (tempRequest != null){
            if(TextUtils.equals(curRequest!!.javaClass.simpleName, request.javaClass.simpleName))
                return true
            else
                tempRequest = tempRequest.next
        }

        return false
    }

    fun addRequest(request: DialogMessageRequest?) {
        request?.also {
            if (curRequest != null)
                if (request.isDirectShow) {
                    insertRequest(request)
                } else {
                    tailRequest?.next = request
                    tailRequest = request
                }
            else {
                curRequest = request
                tailRequest = request
                start()
            }
        }
    }

    private fun insertRequest(request: DialogMessageRequest) {
        if (curRequest!!.isDirectShow) {
            var tempRequest = curRequest
            while (true) {
                if (tempRequest?.next == null || !tempRequest!!.next!!.isDirectShow) {
                    val next = tempRequest?.next
                    request.next = next
                    tempRequest?.next = request
                    if (next == null)
                        tailRequest = request
                    break
                } else {
                    tempRequest = tempRequest.next
                }
            }
        } else {
            val next = curRequest!!.next
            request.next = next
            curRequest!!.next = request
            if (next == null)
                tailRequest = request
            pause()
        }
    }
}