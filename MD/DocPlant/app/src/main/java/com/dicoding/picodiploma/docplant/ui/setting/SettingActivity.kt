package com.dicoding.picodiploma.docplant.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.docplant.R
import com.dicoding.picodiploma.docplant.data.datastore.DataStoreModel
import com.dicoding.picodiploma.docplant.data.datastore.UserPreference
import com.dicoding.picodiploma.docplant.databinding.ActivitySettingBinding
import com.dicoding.picodiploma.docplant.helper.ViewModelFactory
import com.dicoding.picodiploma.docplant.ui.auth.login.LoginActivity
import com.dicoding.picodiploma.docplant.ui.main.MainActivity
import com.dicoding.picodiploma.docplant.ui.scan.ScanActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var dataStoreModel: DataStoreModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigation()
        setupViewModel()
        setAction()
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

                R.id.Navigation_Scan -> {
                    startActivity(Intent(this, ScanActivity::class.java))
                    overridePendingTransition(0,0)
                }

                R.id.Navigation_Setting -> { }
            }
            false
        }
    }
    private fun setupViewModel() {
        dataStoreModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[DataStoreModel::class.java]
    }

    private fun setAction() {
        binding.apply {
            btnLanguageSetting.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            btnLogout.setOnClickListener {
                showLogoutDialog()
            }
        }
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.logout_dialog_title))
            .setMessage(getString(R.string.logout_dialog_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.logout)) { _, _ ->
                dataStoreModel.logout()
                Intent(this, LoginActivity::class.java).also { intent ->
                    startActivity(intent)
                    this.finish()
                }
                Toast.makeText(this, getString(R.string.logout_message_success), Toast.LENGTH_SHORT).show()
            }.show()
    }
}