package com.example.jangkau.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Resource
import com.example.domain.model.Transaction
import com.example.domain.model.TransactionGroup
import com.example.domain.usecase.transaction.GetTransactionByIdUseCase
import com.example.domain.usecase.transaction.GetTransactionHistoryUseCase
import com.example.domain.usecase.transaction.TransferQrisUseCase
import com.example.domain.usecase.transaction.TransferRequestUseCase
import com.example.jangkau.ListState
import com.example.jangkau.State
import com.example.jangkau.toListState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate

class TransactionViewModel(
    private val transferRequestUseCase: TransferRequestUseCase,
    private val getTransactionUseCase: GetTransactionByIdUseCase,
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private val transferQrisUseCase: TransferQrisUseCase

) : ViewModel(){

    private val _transactions = MutableLiveData<State<Transaction>>()
    val transactions: MutableLiveData<State<Transaction>> = _transactions

    fun getTransactionById(transactionId: String) {
        getTransactionUseCase.invoke(transactionId).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    Log.e("GetTransactionByID", "Error: ${result.message}")
                    _transactions.value =
                        State.Error(result.message ?: "An unexpected error occured")
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

    fun transferWithQris(rekeningTujuan: String, nominal: Int){
        transferQrisUseCase.invoke(rekeningTujuan,nominal).onEach { result->
            when(result){
                is Resource.Error -> {
                    Log.e("TransferQrisUseCase", "Error: ${result.message}")
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

    private val _transactionsHistory = MutableLiveData<State<ListState<TransactionGroup>>>()
    val transactionsHistory: LiveData<State<ListState<TransactionGroup>>> = _transactionsHistory

    fun getTransactionHistory(fromDate: String, toDate: String) {
        getTransactionHistoryUseCase.invoke(fromDate, toDate).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    val errorMessage = result.message ?: "No Transactions Found"
                    if (errorMessage.contains("No Transactions", ignoreCase = true)) {
                        _transactionsHistory.value = State.Success(ListState.Empty)
                    } else {
                        Log.e("GetTransactionHistory", "Error: $errorMessage")
                        _transactionsHistory.value = State.Error(errorMessage)
                    }
                }
                is Resource.Loading -> {
                    _transactionsHistory.value = State.Loading
                }
                is Resource.Success -> {
                    _transactionsHistory.value = result.data?.let { list ->
                        list.toListState().let {
                            when (it) {
                                is ListState.Empty -> State.Success(ListState.Empty)
                                is ListState.Success -> State.Success(it)
                            }
                        }
                    } ?: State.Error("An unexpected error occurred")
                }
            }
        }.launchIn(viewModelScope)
    }




}