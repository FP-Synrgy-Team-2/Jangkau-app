package com.example.jangkau

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivitySplashBinding
import com.example.jangkau.feature.auth.LoginActivity

class SplashActivity : BaseActivity() {

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {    
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { openLoginActivity() }
    }

    private fun openLoginActivity() {
        LoginActivity.startActivity(this)
    }
}