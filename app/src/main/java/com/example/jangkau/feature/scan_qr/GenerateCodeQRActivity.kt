package com.example.jangkau.feature.scan_qr

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityGenerateCodeQractivityBinding

class GenerateCodeQRActivity : BaseActivity() {

    private lateinit var binding: ActivityGenerateCodeQractivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateCodeQractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}