package com.dicoding.picodiploma.docplant.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.dicoding.picodiploma.docplant.R
import com.dicoding.picodiploma.docplant.ui.Home
import com.dicoding.picodiploma.docplant.ui.HomeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

}