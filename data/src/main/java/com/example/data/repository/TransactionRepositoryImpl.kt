package com.example.data.repository

import com.example.data.local.DataStorePref
import com.example.data.local.room.SavedAccountDao
import com.example.data.local.room.SavedAccountEntity
import com.example.data.network.ApiService
import com.example.data.network.model.transaction.TransactionHistoryRequest
import com.example.data.network.model.transaction.TransactionRequest
import com.example.data.network.utils.SafeApiRequest
import com.example.domain.model.Transaction
import com.example.domain.model.TransactionGroup
import com.example.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TransactionRepositoryImpl(
    apiService: ApiService,
    dataStorePref: DataStorePref,
    private val savedAccountDao : SavedAccountDao
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
        return Transaction(
            transactionId = transactionId,
            accountId = accountId,
            amount = transactionResponse.total.toInt(),
            transactionDate = transactionResponse.transactionDate ?: "",
            date = transactionResponse.transactionDate ?: "",
            note = transactionResponse.note ?: "",
            adminFee = transactionResponse.adminFee.toInt(),
            isSaved = null,
            beneficiaryAccount = transactionResponse.from.accountNumber ?: "",
            beneficiaryName = transactionResponse.to.ownerName ?: "",
            beneficiaryAccountId = transactionResponse.to?.accountId ?: ""
        )
    }

    override suspend fun makeTransferRequest(
        rekeningTujuan: String,
        nominal: Int,
        catatan: String,
        isSaved: Boolean
    ): Transaction {
        val accountId = dataStorePref.accountId.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()
        val userId = dataStorePref.userId.firstOrNull()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val currentDateTime = LocalDateTime.now().format(formatter)

        if (accountId == null || token == null || userId == null) {
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

        // Insert the beneficiary account into saved accounts if isSaved is true
        if (isSaved) {
            val savedAccount = SavedAccountEntity(
                ownerName = transactionResponse.to.ownerName ?: "",
                accountNumber = transactionResponse.to.accountNumber ?: "",
                savedBy = userId
            )
            savedAccountDao.insertSavedAccount(savedAccount)
        }


        return Transaction(
            transactionId = transactionResponse.transactionId,
            amount = transactionResponse.total.toInt(),
            beneficiaryAccount = transactionResponse.to.accountNumber ?: "",
            beneficiaryAccountId = transactionResponse.to.accountId ?: "",
            beneficiaryName = transactionResponse.to.ownerName ?: "",
            note = transactionResponse.note ?: "",
            transactionDate = transactionResponse.transactionDate ?: "",
            isSaved = isSaved,
            accountId = transactionResponse.from.accountId,
            adminFee = transactionResponse.adminFee.toInt(),
            date = transactionResponse.transactionDate ?: ""
        )
    }


    override suspend fun getTransactionHistory(fromDate: String, toDate: String): List<TransactionGroup> {
        val userId = dataStorePref.userId.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedFromDate = fromDate.format(formatter)
        val formattedToDate = toDate.format(formatter)

        if (userId == null || token == null) {
            throw Exception("User ID or Access Token not found")
        }
        val response = performRequestWithTokenHandling {
            apiService.getTransactionHistory(
                transactionHistoryRequest = TransactionHistoryRequest(
                    startDate = formattedFromDate,
                    endDate = formattedToDate
                ),
                userId = userId,
                token = "Bearer $token"
            )
        }
        val transactionHistoryResponse = response.data ?: throw Exception(response.message)

        // Group transactions by date
        val groupedTransactions = transactionHistoryResponse.groupBy {
            // Convert transactionDate to LocalDate and format as yyyy-MM-dd
            LocalDate.parse(it.transactionDate.substring(0, 10)).toString()
        }.map { (date, transactions) ->
            TransactionGroup(
                date = date,
                transactions = transactions.map { transaction ->
                    Transaction(
                        accountId = transaction.from.accountId,
                        adminFee = 0,
                        amount = transaction.total.toInt(),
                        date = transaction.transactionDate,
                        isSaved = null,
                        note = "",
                        transactionDate = transaction.transactionDate,
                        transactionId = transaction.transactionId,
                        beneficiaryAccount = transaction.to.accountNumber,
                        beneficiaryName = transaction.to.ownerName,
                        beneficiaryAccountId = transaction.to.accountId
                    )
                }
            )
        }

        return groupedTransactions
    }


}