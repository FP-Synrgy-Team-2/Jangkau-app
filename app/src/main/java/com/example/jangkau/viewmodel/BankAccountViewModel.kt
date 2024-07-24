package com.example.jangkau.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Resource
import com.example.domain.model.BankAccount
import com.example.domain.usecase.bank_account.ShowDataBankAccUseCase
import com.example.domain.usecase.bank_account.ShowSavedBankAccUseCase
import com.example.jangkau.ListState
import com.example.jangkau.State
import com.example.jangkau.toListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BankAccountViewModel (
    private val showDataBankAccUseCase: ShowDataBankAccUseCase,
    private val showSavedBankAccUseCase: ShowSavedBankAccUseCase
) : ViewModel(){

    private val _state = MutableLiveData<State<BankAccount>>()
    val state : LiveData<State<BankAccount>> = _state

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

    private val _savedBankAcc = MutableLiveData<State<ListState<BankAccount>>>()
    val savedBankAcc: LiveData<State<ListState<BankAccount>>> = _savedBankAcc

    fun showSavedBankAcc() {
        viewModelScope.launch {
            showSavedBankAccUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("GetSavedBankAccount", "Error: ${result.message}")
                        _savedBankAcc.value = State.Error(result.message ?: "An unexpected error occurred")
                    }
                    is Resource.Loading -> {
                        _savedBankAcc.value = State.Loading
                    }
                    is Resource.Success -> {
                        _savedBankAcc.value = result.data?.let { list ->
                            list.toListState().let {
                                when (it) {
                                    is ListState.Empty -> State.Success(ListState.Empty)
                                    is ListState.Success -> State.Success(it)
                                }
                            }
                        } ?: State.Error("An unexpected error occurred")
                    }
                }
            }
        }
    }


}