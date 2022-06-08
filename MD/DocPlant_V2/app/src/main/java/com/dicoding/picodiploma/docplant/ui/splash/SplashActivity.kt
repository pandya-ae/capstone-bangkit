package com.dicoding.picodiploma.docplant.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.docplant.MainActivity
import com.dicoding.picodiploma.docplant.MainActivity.Companion.EXTRA_TOKEN
import com.dicoding.picodiploma.docplant.R
import com.dicoding.picodiploma.docplant.data.datastore.DataStoreModel
import com.dicoding.picodiploma.docplant.data.datastore.UserPreference
import com.dicoding.picodiploma.docplant.helper.ViewModelFactory
import com.dicoding.picodiploma.docplant.ui.auth.login.LoginActivity
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var dataStoreModel: DataStoreModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupView()
        Handler(Looper.getMainLooper()).postDelayed({
            determineUserDirection()
            finish()
        }, 2000)
    }

    /**
     * Decide which activity to display based on user isLogin?
     */
    private fun determineUserDirection() {
        dataStoreModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[DataStoreModel::class.java]

        lifecycleScope.launchWhenCreated {
            launch {
                dataStoreModel.getUser().observe(this@SplashActivity) { user ->
                    if (user.isLogin) {
                        // User is Login, go to MainActivity
                        Intent(this@SplashActivity, MainActivity::class.java).also { intent ->
                            intent.putExtra(EXTRA_TOKEN, user.token)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // User not login, go to LoginActivity
                        Intent(this@SplashActivity, LoginActivity::class.java).also { intent ->
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}