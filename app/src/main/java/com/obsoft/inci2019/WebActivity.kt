package com.obsoft.inci2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView

class WebActivity : AppCompatActivity() {
    private var web : WebView? = null
    private var addressBar: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        web = findViewById<WebView>(R.id.web_view)
        web?.loadUrl("https://www.usj.edu.lb")
    }

    fun goToWebsite(v: View) {
        addressBar = findViewById<EditText>(R.id.web_input)
        web?.loadUrl(addressBar?.text.toString())
    }

}
