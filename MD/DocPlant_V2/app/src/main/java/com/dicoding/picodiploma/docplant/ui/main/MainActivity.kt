package com.dicoding.picodiploma.docplant.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.docplant.data.datastore.DataStoreModel
import com.dicoding.picodiploma.docplant.data.datastore.UserPreference
import com.dicoding.picodiploma.docplant.databinding.ActivityMainBinding
import com.dicoding.picodiploma.docplant.helper.ViewModelFactory
import com.dicoding.picodiploma.docplant.ui.result.ResultActivity
import com.dicoding.picodiploma.docplant.ui.camera.CameraActivity
import com.dicoding.picodiploma.docplant.ui.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataStoreModel: DataStoreModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        dataStoreModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[DataStoreModel::class.java]
    }

    private fun setupAction() {
        binding.apply {
            btnScanMenu.setOnClickListener {
                startActivity(Intent(this@MainActivity, CameraActivity::class.java))
            }
            btnSearchMenu.setOnClickListener {
                startActivity(Intent(this@MainActivity, ResultActivity::class.java))
            }
            btnSetting.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
        }
    }

    companion object {
        const val EXTRA_LAST_PICT = "extra_token"
    }
}