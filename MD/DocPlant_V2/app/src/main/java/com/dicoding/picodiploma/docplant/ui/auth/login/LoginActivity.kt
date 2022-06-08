package com.dicoding.picodiploma.docplant.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.docplant.MainActivity
import com.dicoding.picodiploma.docplant.R
import com.dicoding.picodiploma.docplant.data.UserModel
import com.dicoding.picodiploma.docplant.data.datastore.DataStoreModel
import com.dicoding.picodiploma.docplant.data.datastore.UserPreference
import com.dicoding.picodiploma.docplant.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.docplant.helper.ViewModelFactory
import com.dicoding.picodiploma.docplant.ui.auth.register.RegisterActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dataStoreModel: DataStoreModel
    private var loginJob: Job = Job()
    private var messgae = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
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

    private fun setupViewModel() {
        dataStoreModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[DataStoreModel::class.java]
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun setupAction() {
        form()
//        binding.bt.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }
        binding.btnLogin.setOnClickListener{
            binding.etEmail.clearFocus()
            binding.etPassword.clearFocus()
            showLoading(true)
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            messgae = ""
            lifecycleScope.launchWhenResumed {
                if (loginJob.isActive) loginJob.cancel()

                loginJob = launch {
                    loginViewModel.userLogin(email, password).collect { res ->
                        res.onSuccess {
                            it.loginResult.let { responseLogin ->
                                val user = UserModel(responseLogin.name.toString(), email, responseLogin.token.toString(), true)
                                dataStoreModel.saveUser(user)
                                showLoading(false)
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                            Toast.makeText(this@LoginActivity,getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        }

                        res.onFailure {
                            Toast.makeText(this@LoginActivity,getString(R.string.login_err), Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun form(){
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun showLoading(state: Boolean){
//        if (state){
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
    }

    private fun setMyButtonEnable() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        binding.btnLogin.isEnabled = password.isNotEmpty() && email.isNotEmpty()
    }
}