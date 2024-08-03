package com.example.jangkau.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Resource
import com.example.domain.model.Auth
import com.example.domain.model.Login
import com.example.domain.usecase.auth.GetLoginStatusUseCase
import com.example.domain.usecase.auth.LoginUseCase
import com.example.domain.usecase.auth.LogoutUseCase
import com.example.domain.usecase.auth.PinValidationUseCase
import com.example.jangkau.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.jangkau.failedPopUp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase : LoginUseCase,
    private val pinValidationUseCase: PinValidationUseCase,
    private val getLoginStatusUseCase: GetLoginStatusUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableLiveData<State<Login>>()
    val state : MutableLiveData<State<Login>> = _state

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


    fun loginUser(username : String, password : String){
        loginUseCase(
            Auth(username = username, password = password)
        ).onEach { result ->
            when(result){
                is Resource.Error -> {
                    Log.e("LoginUser", "Error: ${result.message}")
                    _state.value = State.Error(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = State.Loading
                }
                is Resource.Success -> {
                    _state.value = result.data?.let { State.Success(it) }
                    _isLoggedIn.value = true
                }
            }
        }.launchIn(viewModelScope)
    }

    fun validatePin(pin: String) {
        _pinValidated.value = State.Loading
        pinValidationUseCase(pin).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    Log.e("PinValidation", "Error: ${result.message}")
                    _pinValidated.value = State.Error(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _pinValidated.value = State.Loading
                }
                is Resource.Success -> {
                    _pinValidated.value = State.Success(true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.execute().collect { success ->
                if (success) {
                    _isLoggedIn.value = false
                }
            }
        }
    }


}