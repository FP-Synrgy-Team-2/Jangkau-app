package com.example.domain.repository

import com.example.domain.model.Transaction

interface TransactionRepository {

    suspend fun getTransactionById(transactionId : String) : Transaction

    suspend fun makeTransferRequest(rekeningTujuan: String, nominal: Int, catatan: String, isSaved : Boolean) : Transaction

}