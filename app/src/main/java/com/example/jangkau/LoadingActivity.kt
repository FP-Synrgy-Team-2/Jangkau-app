package com.example.jangkau

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityLoadingBinding
import com.example.jangkau.viewmodel.AuthViewModel
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
                    showToast(state.error)
                    finish()
                }

                State.Loading -> {
                    Log.d("LoadingActivity", "Loading state")  // Debug log
                }

                is State.Success -> {
                    openHomeActivity()
                    finish()
                }
            }
        })
        viewModel.loginUser(username, password)
    }
}