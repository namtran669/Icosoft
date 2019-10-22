package com.coff.icosoft

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    fun showWebview() {
        tv_contact.visibility = View.GONE
        wv_main.visibility = View.VISIBLE
        // Force links and redirects to open in the WebView instead of in a browser
        wv_main!!.webChromeClient = WebChromeClient()
        wv_main!!.settings.javaScriptEnabled = true
        wv_main!!.settings.domStorageEnabled = true

        wv_main!!.webViewClient = WebViewClient()

        wv_main!!.settings.saveFormData = true
        wv_main!!.settings.allowContentAccess = true
        wv_main!!.settings.allowFileAccess = true
        wv_main!!.settings.allowFileAccessFromFileURLs = true
        wv_main!!.settings.allowUniversalAccessFromFileURLs = true
        wv_main!!.settings.setSupportZoom(false)
        wv_main!!.isClickable = true

        // Use remote resource
        wv_main!!.postDelayed({ wv_main!!.loadUrl(PAGE_URL) }, 0)


        wv_main!!.onCheckIsTextEditor()

        wv_main!!.requestFocus(View.FOCUS_DOWN)
        wv_main!!.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> if (!v.hasFocus()) {
                    v.requestFocus()
                }
            }
            false
        }
    }

    fun showTextContact() {
        wv_main.visibility = View.GONE
        tv_contact.visibility = View.VISIBLE
    }

    fun isConnectInternet(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() < 2000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        if (isConnectInternet()) {
            showWebview()
        } else {
            showTextContact()
        }
    }

    // Prevent the back-button from closing the app
    override fun onBackPressed() {
        if (wv_main!!.canGoBack()) {
            wv_main!!.goBack()
        } else {
            super.onBackPressed()
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv_main!!.canGoBack()) {
            wv_main!!.goBack()
            //If there is history, then the canGoBack method will return ‘true’//
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        private val PAGE_URL = "https://icosoft.vn"
    }
}
