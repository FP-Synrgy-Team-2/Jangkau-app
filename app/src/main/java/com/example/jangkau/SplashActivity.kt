package com.example.jangkau

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {    
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnQris.setOnClickListener { openQrisActivity() }

        binding.btnLogin.setOnClickListener { openLoginActivity() }
    }
}