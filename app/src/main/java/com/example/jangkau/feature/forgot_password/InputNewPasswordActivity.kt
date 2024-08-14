package com.example.jangkau.feature.forgot_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityInputNewPasswordBinding
import com.example.jangkau.gone
import com.example.jangkau.viewmodel.AuthViewModel
import com.example.jangkau.visible
import org.koin.android.ext.android.inject

class InputNewPasswordActivity : BaseActivity() {
    private lateinit var binding : ActivityInputNewPasswordBinding
    private val authViewModel: AuthViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("EXTRA_EMAIL")
        val otp = intent.getStringExtra("EXTRA_OTP")

        setupPasswordValidation()
        binding.btnNext.setOnClickListener {
            val newPassword = binding.textInputLayoutNewPassword.editText?.text.toString()
            authViewModel.resetPassword(email.toString(), otp.toString(), newPassword)
            authViewModel.state.observe(this){ state->
                when(state){
                    is State.Error -> {
                        binding.btnNext.visible()
                        binding.progressBar.gone()
                        binding.textInputLayoutNewPassword.error = state.error
                    }
                    State.Loading -> {
                        binding.progressBar.visible()
                        binding.btnNext.gone()
                    }
                    is State.Success -> {
                        binding.progressBar.gone()
                        binding.btnNext.visible()
                        finish()
                        openLoginActivity()
                    }
                }
            }
        }
    }

    private fun setupPasswordValidation() {
        val passwordEditText = binding.edtNewPassword

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(password: String) {
        binding.cbMin8Huruf.isChecked = password.length >= 8
        binding.cbTerdapatAngka.isChecked = password.any { it.isDigit() }
        binding.cbHurufKapital.isChecked = password.any { it.isUpperCase() }
        binding.cbKarakter.isChecked = password.any { it in listOf('@', '#', '!') }

        checkAllCriteriaMet()
    }

    private fun checkAllCriteriaMet() {
        val allCriteriaMet = binding.cbMin8Huruf.isChecked &&
                binding.cbTerdapatAngka.isChecked &&
                binding.cbHurufKapital.isChecked &&
                binding.cbKarakter.isChecked

        binding.btnNext.isEnabled = allCriteriaMet
    }
}