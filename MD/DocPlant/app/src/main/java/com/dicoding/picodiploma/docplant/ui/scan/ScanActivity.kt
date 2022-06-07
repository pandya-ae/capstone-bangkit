package com.dicoding.picodiploma.docplant.ui.scan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.docplant.R
import com.dicoding.picodiploma.docplant.databinding.ActivityScanBinding
import com.dicoding.picodiploma.docplant.ui.main.MainActivity
import com.dicoding.picodiploma.docplant.ui.setting.SettingActivity

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigation()
    }

    private fun setNavigation() {
        val navigation = binding.navView
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Navigation_Home -> {
                    onMenuItemSelected(R.id.Navigation_Home, item)
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0,0)
                }

                R.id.Navigation_Scan -> { }

                R.id.Navigation_Setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    overridePendingTransition(0,0)
                }
            }
            false
        }
    }
}