package com.example.data.repository

import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.model.transaction.TransactionRequest
import com.example.data.network.utils.SafeApiRequest
import com.example.domain.model.Transaction
import com.example.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TransactionRepositoryImpl(
    private val apiService: ApiService,
    private val dataStorePref: DataStorePref
) : TransactionRepository, SafeApiRequest() {

    override suspend fun makeTransferRequest(rekeningTujuan: String, nominal: Int, catatan: String, isSaved : Boolean) : Transaction {
        val accountId = dataStorePref.accountId.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentDateTime = LocalDateTime.now().format(formatter)
        val response = safeApiRequest {
            apiService.transaction(
                transactionRequest = TransactionRequest(
                    accountId = accountId,
                    note = catatan,
                    isSaved = isSaved,
                    amount = nominal,
                    transactionDate = currentDateTime,
                    beneficiaryAccount = rekeningTujuan
                ),
                token = "Bearer $token"
            )
        }
        val transactionResponse = response.body()?.data ?: throw Exception(response.message())

        return Transaction(
            amount = transactionResponse.amount,
            beneficiaryAccount = transactionResponse.beneficiaryAccount,
            note = transactionResponse.note,
            transactionDate = transactionResponse.transactionDate,
            isSaved = true,
            accountId = transactionResponse.accountId,
            adminFee = transactionResponse.adminFee
        )

    }

}