package com.example.jangkau.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Resource
import com.example.domain.model.Transaction
import com.example.domain.usecase.transfer.TransferRequestUseCase
import com.example.jangkau.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TransactionViewModel(
    private val transferRequestUseCase: TransferRequestUseCase

) : ViewModel(){

    private val _transactions = MutableLiveData<State<Transaction>>()
    val transactions: MutableLiveData<State<Transaction>> = _transactions


    fun transfer(rekeningTujuan: String, nominal: Int, catatan: String, isSaved : Boolean){
        transferRequestUseCase.invoke(
            rekeningTujuan = rekeningTujuan,
            nominal = nominal,
            catatan = catatan,
            isSaved = isSaved
        ).onEach {result->
            when(result){
                is Resource.Error -> {
                    Log.e("TransferRequestUseCase", "Error: ${result.message}")
                    _transactions.value = State.Error(result.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _transactions.value = State.Loading
                }
                is Resource.Success -> {
                    _transactions.value = result.data?.let { State.Success(it) }
                }
            }
        }.launchIn(viewModelScope)
    }

}