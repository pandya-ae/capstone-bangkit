package com.dicoding.picodiploma.docplant.ui.result

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.docplant.data.datastore.DataStoreModel
import com.dicoding.picodiploma.docplant.data.datastore.UserPreference
import com.dicoding.picodiploma.docplant.databinding.ActivityResultBinding
import com.dicoding.picodiploma.docplant.helper.ViewModelFactory

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var dataStoreModel: DataStoreModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        setupViewMode()
        setupAction()
    }

    private fun setupViewMode() {
        dataStoreModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DataStoreModel::class.java]
    }

    private fun setupAction() {
        dataStoreModel.getUser().observe(this) { user ->
            if (user.last_picture != "-") {
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(user.last_picture))
                binding.diseases.text = user.disease_name
            } else {
                binding.diseases.text = user.disease_name
            }
        }
    }
}