package com.example.jangkau.feature.forgot_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityInputEmailBinding
import com.example.jangkau.gone
import com.example.jangkau.viewmodel.AuthViewModel
import com.example.jangkau.visible
import org.koin.android.ext.android.inject

class InputEmailActivity : BaseActivity() {

    private lateinit var binding: ActivityInputEmailBinding
    private val authViewModel: AuthViewModel by inject()

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
            val email = binding.textInputLayoutEmail.editText?.text.toString()
            if (validateEmail(email)) {
                authViewModel.forgotPassword(email)
                authViewModel.state.observe(this){state->
                    when(state){
                        is State.Error -> {
                            binding.btnNext.visible()
                            binding.progressBar.gone()
                            binding.textInputLayoutEmail.error = state.error
                        }
                        State.Loading -> {
                            binding.progressBar.visible()
                            binding.btnNext.gone()
                        }
                        is State.Success -> {
                            binding.progressBar.gone()
                            binding.btnNext.visible()
                            openInputOtpActivity(email)
                        }
                    }
                }
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