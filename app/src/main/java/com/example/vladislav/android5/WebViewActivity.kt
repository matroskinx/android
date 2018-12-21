package com.example.vladislav.android5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vladislav.android5.Extensions.NewsWebViewClient
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val link = intent.getStringExtra("URL_FOR_WEB_VIEW")
        webView.webViewClient = NewsWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(link)
    }
}
