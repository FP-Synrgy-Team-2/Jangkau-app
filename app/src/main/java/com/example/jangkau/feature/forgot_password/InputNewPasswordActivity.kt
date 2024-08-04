package com.example.jangkau.feature.forgot_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityInputNewPasswordBinding

class InputNewPasswordActivity : BaseActivity() {
    private lateinit var binding : ActivityInputNewPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPasswordValidation()
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