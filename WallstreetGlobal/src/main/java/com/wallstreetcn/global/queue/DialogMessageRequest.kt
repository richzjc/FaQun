package com.wallstreetcn.global.queue

import com.richzjc.dialoglib.base.BaseDialogFragment

/**
 * Created by Leif Zhang on 16/9/26.
 * Email leifzhanggithub@gmail.com
 */
abstract class DialogMessageRequest : BaseDialogFragment() {
    open var isDirectShow: Boolean = false
    var isPause: Boolean = false
    var next: DialogMessageRequest? = null

    abstract suspend fun deliverResponse()

    open fun pause() {
        isPause = true
        if (isAdded) {
            dismiss()
        }
    }
}