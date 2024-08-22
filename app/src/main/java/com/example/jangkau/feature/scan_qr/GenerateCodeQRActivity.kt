package com.example.jangkau.feature.scan_qr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.ErrorActivity
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityGenerateCodeQractivityBinding
import com.example.jangkau.viewmodel.BankAccountViewModel
import org.koin.android.ext.android.inject

class GenerateCodeQRActivity : BaseActivity() {

    private lateinit var binding: ActivityGenerateCodeQractivityBinding
    private val bankViewModel: BankAccountViewModel by inject()

    companion object {
        const val PIN_INPUT_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateCodeQractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openPinInputActivity()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIN_INPUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            
            bankViewModel.generateQr()
            bankViewModel.showDataBankAcc()
            setupObservers()
            startCountdownTimer()
            binding.btnBatal.setOnClickListener { finish() }
        }
    }

    private fun setupObservers() {
        // Observe Bank Account Data
        bankViewModel.state.observe(this) { state ->
            when (state) {
                is State.Error -> {
                    val intent = Intent(this, ErrorActivity::class.java)
                    intent.putExtra("ERROR_MESSAGE", state.error)
                    startActivity(intent)
                }
                State.Loading -> {

                }
                is State.Success -> {
                    binding.tvOwnerName.text = state.data.ownerName
                    binding.tvAccountNumber.text = state.data.accountNumber
                }
            }
        }

        // Observe QR Code Generation
        bankViewModel.qrState.observe(this) { state ->
            when (state) {
                is State.Error -> {
                    showToast(state.error)
                }
                State.Loading -> {

                }
                is State.Success -> {
                    binding.imgQRIS.setImageBitmap(state.data)
                }
            }
        }
    }

    private fun startCountdownTimer() {
        val totalTime = 180000L // 1 minute in milliseconds
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
