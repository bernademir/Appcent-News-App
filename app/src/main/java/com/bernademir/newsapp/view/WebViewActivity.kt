package com.bernademir.newsapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bernademir.newsapp.R

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val actionbar = supportActionBar
        actionbar!!.title = "News Source"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val data = intent.extras
        val webDataUrl = data?.getString("webUrl")

        webView = findViewById(R.id.web_view)
        true.also { webView.settings.javaScriptEnabled = it }
        webView.settings.setSupportZoom(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }
         webView.loadUrl(webDataUrl!!.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}