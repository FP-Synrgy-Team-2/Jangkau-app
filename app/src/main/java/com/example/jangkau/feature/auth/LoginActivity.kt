package com.example.jangkau.feature.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener { finish() }

        binding.btnLogin.setOnClickListener {
            openHomeActivity()
        }
    }
}