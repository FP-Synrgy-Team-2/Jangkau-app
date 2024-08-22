package com.example.domain.repository

import com.example.domain.model.Transaction
import com.example.domain.model.TransactionGroup
import java.time.LocalDate

interface TransactionRepository {

    suspend fun getTransactionById(transactionId : String) : Transaction

    suspend fun makeTransferRequest(rekeningTujuan: String, nominal: Int, catatan: String, isSaved : Boolean) : Transaction

    suspend fun getTransactionHistory(fromDate: String, toDate: String) : List<TransactionGroup>

    suspend fun transferQris(rekeningTujuan: String, nominal: Int) : Transaction

}