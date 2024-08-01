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
    apiService: ApiService,
    dataStorePref: DataStorePref
) : BaseRepository(apiService, dataStorePref), TransactionRepository {
    override suspend fun getTransactionById(transactionId: String): Transaction {
        val accountId = dataStorePref.accountId.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()

        if (accountId == null || token == null) {
            throw Exception("Account ID or Access Token not found")
        }
        val response = performRequestWithTokenHandling {
            apiService.getTransactionById(
                transactionId = transactionId,
                token = "Bearer $token"
            )
        }
        val transactionResponse = response.data ?: throw Exception(response.message)
        return transactionResponse.toDomain()
    }

    override suspend fun makeTransferRequest(
        rekeningTujuan: String,
        nominal: Int,
        catatan: String,
        isSaved: Boolean
    ): Transaction {
        val accountId = dataStorePref.accountId.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val currentDateTime = LocalDateTime.now().format(formatter)

        if (accountId == null || token == null) {
            throw Exception("Account ID or Access Token not found")
        }

        val response = performRequestWithTokenHandling {
            apiService.transaction(
                transactionRequest = TransactionRequest(
                    accountId = accountId,
                    note = catatan,
                    saved = isSaved,
                    amount = nominal,
                    transactionDate = currentDateTime,
                    beneficiaryAccount = rekeningTujuan
                ),
                token = "Bearer $token"
            )
        }

        val transactionResponse = response.data ?: throw Exception(response.message)

        return Transaction(
            transactionId = transactionResponse.transactionId,
            amount = transactionResponse.amount,
            beneficiaryAccount = transactionResponse.beneficiaryAccount,
            note = transactionResponse.note,
            transactionDate = transactionResponse.transactionDate,
            isSaved = isSaved,
            accountId = transactionResponse.accountId,
            adminFee = transactionResponse.adminFee
        )
    }


}