package com.example.jangkau.feature.forgot_password

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityInputOtpBinding

class InputOtpActivity : BaseActivity() {

    private lateinit var binding: ActivityInputOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.firstPinView.isPasswordHidden = true

        startCountdownTimer()

        binding.btnLogin.setOnClickListener {
            openInputNewPasswordActivity()
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