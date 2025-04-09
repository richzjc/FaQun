package com.wallstreetcn.rpc

import com.kronos.volley.Request
import com.kronos.volley.VolleyError
import com.wallstreetcn.rpc.coroutines.requestData
import kotlinx.coroutines.delay

fun checkResponseTime(request: Request<Any>) {
    if (request is IGetResponseTime) {
        val longTime = request.responseTime
        if (longTime > 0) {
            requestData {
                delay(longTime)
                if(!request.hasHadResponseDelivered()){
                    request?.errorListener?.onErrorResponse(VolleyError("响应超时"))
                    request?.cancel()
                }
            }
        }
    }
}