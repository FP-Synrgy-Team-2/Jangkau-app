package com.example.jangkau.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Resource
import com.example.domain.model.User
import com.example.domain.usecase.user.GetUserUseCase
import com.example.jangkau.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UserViewModel (
    private val getUserUseCase: GetUserUseCase
) : ViewModel(){

    private val _userState = MutableLiveData<State<User>>()
    val userState : LiveData<State<User>> = _userState

    fun getUser(){
        getUserUseCase.invoke().onEach { result ->
            when(result){
                is Resource.Error -> {
                    Log.e("GetUser", "Error: ${result.message}")
                    _userState.value = State.Error(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _userState.value = State.Loading
                }
                is Resource.Success -> {
                    _userState.value = result.data?.let { State.Success(it) }
                }
            }
        }.launchIn(viewModelScope)
    }



}