package com.example.jangkau.feature.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Resource
import com.example.domain.model.BankAccount
import com.example.domain.usecase.bank_account.ShowDataBankAccUseCase
import com.example.jangkau.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class BankAccountViewModel (
    private val showDataBankAccUseCase: ShowDataBankAccUseCase
) : ViewModel(){

    private val _state = MutableLiveData<State<BankAccount>>()
    val state : MutableLiveData<State<BankAccount>> = _state

    fun showDataBankAcc(){
        showDataBankAccUseCase.invoke().onEach { result->
            when(result){
                is Resource.Error -> {
                    Log.e("GetBankAccount", "Error: ${result.message}")
                    _state.value = State.Error(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = State.Loading
                }
                is Resource.Success -> {
                    _state.value = result.data?.let { State.Success(it) }
                }
            }
        }.launchIn(viewModelScope)

    }

}