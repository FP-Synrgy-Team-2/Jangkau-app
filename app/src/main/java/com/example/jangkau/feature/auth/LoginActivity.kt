package com.example.jangkau.feature.auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityLoginBinding
import com.example.jangkau.failedPopUp
import com.example.jangkau.gone
import com.example.jangkau.viewmodel.AuthViewModel
import com.example.jangkau.viewmodel.BankAccountViewModel
import com.example.jangkau.viewmodel.UserViewModel
import com.example.jangkau.visible
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity() {


    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(
            layoutInflater
        )
    }
    private val authViewModel : AuthViewModel by inject()
    private val userViewModel : UserViewModel by inject()



    private val loginReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val result = intent?.getStringExtra("RESULT")
            val error = intent?.getStringExtra("ERROR")

            if (result == "SUCCESS") {
                openHomeActivity()
                finish()
            } else if (result == "ERROR") {
                failedPopUp(error ?: "Unknown error", this@LoginActivity)
                triggerVibrationWithDelay()

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        LocalBroadcastManager.getInstance(this).registerReceiver(
            loginReceiver,
            IntentFilter("RESULT_ACTION")
        )

        observeLoginStatus()

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

    private fun observeLoginStatus() {
        lifecycleScope.launchWhenStarted {
            authViewModel.isLoggedIn.collect { loggedIn ->
                if (loggedIn) {
                    userViewModel.getUser()
                    observeUserState()
                }
            }
        }
    }
    private fun observeUserState() {
        userViewModel.userState.observe(this@LoginActivity) { state ->
            when (state) {
                is State.Error -> {hideLoadingDialog()}
                State.Loading -> showLoadingDialog()
                is State.Success -> {
                    hideLoadingDialog()
                    updateUserUI(state.data.username)
                }
            }
        }
    }
    private fun updateUserUI(username: String) {
        binding.textInputLayoutUsername.editText?.setText(username)
        binding.textInputLayoutUsername.editText?.isEnabled = false // Disable editing if logged in
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginReceiver)
        super.onDestroy()
    }
}