package com.wallstreetcn.webview.api

import com.kronos.volley.toolbox.BaseApiParser
import com.wallstreetcn.rpc.CustomApi
import com.wallstreetcn.rpc.CustomGsonApiListParser
import com.wallstreetcn.rpc.ResponseListener
import com.wallstreetcn.rpc.ServerAPI
import com.wallstreetcn.webview.entity.MiniProgramEntity

class MiniProgramApi(listener: ResponseListener<List<MiniProgramEntity>>) :
    CustomApi<List<MiniProgramEntity>>(listener) {
    init {
        setNeedToast(false)
    }

    override fun getUrl() = "${ServerAPI.API_V1}kvconfig/items/webviewjs"


    override fun getParser(): BaseApiParser {
        return CustomGsonApiListParser(MiniProgramEntity::class.java)
    }
}