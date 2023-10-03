package com.example.webviewtest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONUtil

@SuppressLint("SetJavaScriptEnabled")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main()
    }

    private fun main() {
        val webView = findViewById<WebView>(R.id.web_view).apply {
            webViewClient = object : WebViewClient() {

                //重写此方法，解决WebView在重定向时打开系统浏览器的问题
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    val url = request.url.toString()
                    //禁止WebView加载未知协议的URL
                    if(!url.startsWith("http")) return true
                    view.loadUrl(url)
                    return true
                }
            }
            settings.run {
                //必须打开，否则网页可能显示为空白
                javaScriptEnabled = true
            }
        }
        findViewById<Button>(R.id.btn).run {
            setOnClickListener {
                Toast.makeText(this@MainActivity, "后退", Toast.LENGTH_SHORT).show()
                webView.goBack()
            }
        }
        webView.run {
            loadUrl("file:///android_asset/dist/index.html")
            addJavascriptInterface(TestJsInterface(this@MainActivity), "android_TestJsInterface")
        }
    }
}

class TestJsInterface(private val context: Context) {

    @JavascriptInterface
    fun test1() {
        Toast.makeText(context, "JavaScriptInterface测试", Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun loadApi(): String = try {
        val res = HttpUtil.get("https://api.bilibili.com/x/web-interface/wbi/index/top/feed/rcmd")
        JSONUtil.parse(res).toStringPretty()
    } catch(t: Throwable) {
        "load failed"
    }
}