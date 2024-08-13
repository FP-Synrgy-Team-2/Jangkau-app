package com.example.jangkau.feature.forgot_password

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityInputOtpBinding
import com.example.jangkau.failedPopUp
import com.example.jangkau.gone
import com.example.jangkau.viewmodel.AuthViewModel
import com.example.jangkau.visible
import org.koin.android.ext.android.inject

class InputOtpActivity : BaseActivity() {

    private lateinit var binding: ActivityInputOtpBinding
    private val authViewModel: AuthViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.firstPinView.isPasswordHidden = true

        startCountdownTimer()

        binding.btnLogin.setOnClickListener {
            val otp = binding.firstPinView.text.toString()
            authViewModel.validateOtp(otp)
            authViewModel.state.observe(this){state->
                when(state){
                    is State.Error -> {
                        binding.progressBar.gone()
                        binding.btnLogin.visible()
                        failedPopUp(state.error, this)
                    }
                    State.Loading -> {
                        binding.progressBar.visible()
                        binding.btnLogin.gone()
                    }
                    is State.Success -> {
                        binding.progressBar.gone()
                        binding.btnLogin.visible()
                        openInputNewPasswordActivity()
                    }
                }
            }
        }
    }

    private fun startCountdownTimer() {
        val totalTime = 60000L // 1 minute in milliseconds
        val countDownInterval = 1000L // 1 second in milliseconds

        object : CountDownTimer(totalTime, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvCountDown.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.btnResend.isEnabled = true
            }
        }.start()
    }
}