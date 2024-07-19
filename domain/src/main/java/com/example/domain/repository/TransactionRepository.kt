package com.example.domain.repository

import com.example.domain.model.Transaction
import com.example.domain.model.TransferRequest

interface TransactionRepository {

    suspend fun getTransactionById(transactionId : String) : Transaction

    suspend fun makeTransferRequest(transferRequest: TransferRequest) : Transaction

}