package com.example.jangkau.feature.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityLoginBinding
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity() {

    private val viewModel: AuthViewModel by inject()
    private val userViewModel : UserViewModel by inject()



    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.state.observe(this) { state ->
            when (state) {
                is State.Error -> {
                    showToast(state.error)
                }

                State.Loading -> {
                    Log.d("LoginActivity", "Loading state")  // Debug log

                }

                is State.Success -> {
                    openHomeActivity()
                    Log.d("LoginActivity", "User: ${state.data}")
                }
            }
        }

        binding.btnCancel.setOnClickListener { finish() }

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
                viewModel.loginUser(username, password)
//                viewModel.state.observe(this) { state ->
//                    when (state) {
//                        is State.Error -> {
//                            showToast(state.error)
//                        }
//                        State.Loading -> {
//
//                        }
//                        is State.Success -> {
//                            openHomeActivity()
//                            Log.d("TAG", "User: ${state.data}")
//                            userViewModel.getUser(state.data.userId)
//                            userViewModel.state.observe(this) { state ->
//                                when (state) {
//                                    is State.Error -> {
//                                        showToast(state.error)
//                                    }
//                                    State.Loading -> {
//
//                                    }
//                                    is State.Success -> {
//                                        showToast("Login Success, welcome back $username")
//                                        openHomeActivity()
//                                        Log.d("TAG", "User: ${state.data}")
//                                    }
//                                }
//                            }
                        }
                    }
//                }
//            }
//        }



    }
}