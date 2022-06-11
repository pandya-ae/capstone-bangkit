package com.dicoding.picodiploma.docplant.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.docplant.R
import com.dicoding.picodiploma.docplant.data.BitmapModel
import com.dicoding.picodiploma.docplant.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_RESULT = "extra_result"
    }
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val result = intent.getParcelableExtra<BitmapModel>(EXTRA_RESULT) as BitmapModel
        binding.diseases.text = result.string
        binding.previewImageView.setImageBitmap(result.bitmap)
    }
}