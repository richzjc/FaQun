package com.wallstreetcn.webview.Template

interface WebLoadCallback {
    fun onLoadFinish()
    fun onLoadTitle(title: String?);
}