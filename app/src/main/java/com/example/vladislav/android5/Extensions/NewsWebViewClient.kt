package com.example.vladislav.android5.Extensions

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class NewsWebViewClient() : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view?.loadUrl(request?.url.toString())
        return true
    }
}