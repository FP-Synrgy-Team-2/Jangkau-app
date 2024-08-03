package com.example.jangkau

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityLoadingBinding
import com.example.jangkau.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoadingActivity : BaseActivity() {

    private val viewModel: AuthViewModel by inject()
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("USERNAME") ?: return
        val password = intent.getStringExtra("PASSWORD") ?: return

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Error -> {
                    lifecycleScope.launch {
                        sendResult("ERROR", state.error)
                        delay(2000)
                        finish()
                    }
                }

                State.Loading -> {
                    // Show loading state if necessary
                }

                is State.Success -> {
                    sendResult("SUCCESS")
                    finish()
                }
            }
        })
        viewModel.loginUser(username, password)
    }

    private fun sendResult(result: String, error: String? = null) {
        val intent = Intent("LOGIN_RESULT").apply {
            putExtra("RESULT", result)
            error?.let { putExtra("ERROR", it) }
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}
