package com.dicoding.picodiploma.docplant.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.docplant.R
import com.dicoding.picodiploma.docplant.databinding.ActivityMainBinding
import com.dicoding.picodiploma.docplant.ui.scan.ScanActivity
import com.dicoding.picodiploma.docplant.ui.setting.SettingActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigation()
    }

    private fun setNavigation() {
        val navigation = binding.navView
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Navigation_Home -> {}

                R.id.Navigation_Scan -> {
                    startActivity(Intent(this, ScanActivity::class.java))
                    overridePendingTransition(0,0)
                }

                R.id.Navigation_Setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    overridePendingTransition(0,0)
                }
            }
            false
        }
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}