package com.example.jangkau.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Resource
import com.example.domain.model.Auth
import com.example.domain.model.Login
import com.example.domain.usecase.auth.ForgotPasswordUseCase
import com.example.domain.usecase.auth.GetLoginStatusUseCase
import com.example.domain.usecase.auth.LoginUseCase
import com.example.domain.usecase.auth.LogoutUseCase
import com.example.domain.usecase.auth.PinValidationUseCase
import com.example.domain.usecase.auth.ValidateOtpUseCase
import com.example.jangkau.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val pinValidationUseCase: PinValidationUseCase,
    private val getLoginStatusUseCase: GetLoginStatusUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val validateOtpUseCase: ValidateOtpUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {


    private val _state = MutableLiveData<State<String>>()
    val state: MutableLiveData<State<String>> = _state

    private val _loginState = MutableLiveData<State<Login>>()
    val loginState: MutableLiveData<State<Login>> = _loginState

    private val _pinValidated = MutableLiveData<State<Boolean>>()
    val pinValidated: MutableLiveData<State<Boolean>> = _pinValidated

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            _isLoggedIn.value = getLoginStatusUseCase.isLoggedIn()
        }
    }

    fun validateOtp(otp: String) {
        validateOtpUseCase(otp).onEach {result->
            when(result){
                is Resource.Error -> {
                    Log.e("ValidateOTP", "Error: ${result.message}")
                    state.value = State.Error(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    Log.d("ValidateOTP", "Loading...")
                    state.value = State.Loading
                }
                is Resource.Success -> {
                    Log.d("ValidateOTP", "Success: ${result.data}")
                    state.value = result.data?.let { State.Success(it) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun forgotPassword(email: String) {
        forgotPasswordUseCase(email).onEach { result->
            when(result){
                is Resource.Error -> {
                    Log.e("ForgotPassword", "Error: ${result.message}")
                    state.value = State.Error(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    Log.d("ForgotPassword", "Loading...")
                    state.value = State.Loading
                }
                is Resource.Success -> {
                    Log.d("ForgotPassword", "Success: ${result.data}")
                    state.value = result.data?.let { State.Success(it) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loginUser(username: String, password: String) {
        Log.d("AuthViewModel", "loginUser called with username: $username")
        loginUseCase(
            Auth(username = username, password = password)
        ).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    Log.e("LoginUser", "Error: ${result.message}")
                    _loginState.value = State.Error(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    Log.d("LoginUser", "Loading...")
                    _loginState.value = State.Loading
                }
                is Resource.Success -> {
                    Log.d("LoginUser", "Success: ${result.data}")
                    _loginState.value = result.data?.let { State.Success(it) }
                    _isLoggedIn.value = true
                }
            }
        }.launchIn(viewModelScope)
    }

    fun validatePin(pin: String) {
        Log.d("AuthViewModel", "validatePin called with pin: $pin")
        _pinValidated.value = State.Loading
        pinValidationUseCase(pin).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    Log.e("PinValidation", "Error: ${result.message}")
                    _pinValidated.value = State.Error(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    Log.d("PinValidation", "Loading...")
                    _pinValidated.value = State.Loading
                }
                is Resource.Success -> {
                    Log.d("PinValidation", "Success")
                    _pinValidated.value = State.Success(true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout() {
        Log.d("AuthViewModel", "logout called")
        viewModelScope.launch {
            logoutUseCase.execute().collect { success ->
                if (success) {
                    Log.d("AuthViewModel", "Logout successful")
                    _isLoggedIn.value = false
                } else {
                    Log.e("AuthViewModel", "Logout failed")
                }
            }
        }
    }
}
