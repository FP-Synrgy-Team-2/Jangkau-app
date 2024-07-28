package com.example.jangkau.feature.forgot_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityInputEmailBinding

class InputEmailActivity : BaseActivity() {

    private lateinit var binding: ActivityInputEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

       setupEmailValidation()

    }

    private fun setupEmailValidation() {
        val emailEditText = binding.edtUsername

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnNext.setOnClickListener {
            if (validateEmail(emailEditText.text.toString())) {
                openInputOtpActivity()
            } else {
                emailEditText.error = getString(R.string.invalid_email)
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.textInputLayoutEmail.error = getString(R.string.email_belum_di_isi)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = getString(R.string.invalid_email)
            false
        } else {
            binding.textInputLayoutEmail.error = null
            true
        }
    }
}