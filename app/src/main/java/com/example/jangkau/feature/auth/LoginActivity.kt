package com.example.jangkau.feature.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityLoginBinding
import com.example.jangkau.feature.transfer.TransferInputActivity
import com.example.jangkau.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity() {

    private val authViewModel: AuthViewModel by inject()

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener { finish() }

        binding.btnForgotPassword.setOnClickListener {
            openInputEmailActivity()
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.textInputLayoutUsername.editText?.text.toString()
            val password = binding.textInputLayoutPassword.editText?.text.toString()

            var hasError = false

            if (username.isEmpty()) {
                binding.textInputLayoutUsername.error = getString(R.string.empty_username)
                hasError = true
            } else {
                binding.textInputLayoutUsername.error = null
            }

            if (password.isEmpty()) {
                binding.textInputLayoutPassword.error = getString(R.string.empty_password)
                hasError = true
            } else {
                binding.textInputLayoutPassword.error = null
            }

            if (!hasError) {
                openLoadingActivity(username, password)
            }
        }


    }
}