package com.example.data.repository

import com.example.data.local.DataStorePref
import com.example.data.local.room.SavedAccountDao
import com.example.data.local.room.SavedAccountEntity
import com.example.data.network.ApiService
import com.example.data.network.model.transaction.TransactionHistoryRequest
import com.example.data.network.model.transaction.TransactionRequest
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
            ownerName = transactionResponse.from.ownerName,
            ownerAccount = transactionResponse.from.accountNumber,

            amount = transactionResponse.total.toInt(),
            transactionDate = transactionResponse.transactionDate ?: "",
            date = transactionResponse.transactionDate ?: "",
            note = transactionResponse.note ?: "",
            adminFee = transactionResponse.adminFee.toInt(),
            isSaved = null,

            beneficiaryAccount = transactionResponse.to.accountNumber ?: "",
            beneficiaryName = transactionResponse.to.ownerName ?: "",
            beneficiaryAccountId = transactionResponse.to?.accountId ?: "",
            transactionalType = transactionResponse.transactionalType
        )
    }

    override suspend fun transferQris(rekeningTujuan: String, nominal: Int): Transaction {
        val accountId = dataStorePref.accountId.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()
        val userId = dataStorePref.userId.firstOrNull()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val currentDateTime = LocalDateTime.now().format(formatter)

        if (accountId == null || token == null || userId == null) {
            throw Exception("Account ID or Access Token not found")
        }
        val response = performRequestWithTokenHandling {
            val requestBody = mapOf(
                "account_id" to accountId,
                "beneficiary_account" to rekeningTujuan,
                "amount" to nominal.toString(),
                "transaction_date" to currentDateTime
            )
            apiService.tranferQris(requestBody, "Bearer $token")
        }
        val transactionResponse = response.data ?: throw Exception(response.message)
        return Transaction(
            transactionId = transactionResponse.transactionId,
            amount = transactionResponse.total.toInt(),
            beneficiaryAccount = transactionResponse.to.accountNumber ?: "",
            beneficiaryAccountId = transactionResponse.to.accountId ?: "",
            beneficiaryName = transactionResponse.to.ownerName ?: "",
            note = transactionResponse.note ?: "",
            transactionDate = transactionResponse.transactionDate ?: "",
            isSaved = false,
            accountId = transactionResponse.from.accountId,
            adminFee = transactionResponse.adminFee.toInt(),
            date = transactionResponse.transactionDate ?: "",
            ownerName = null,
            ownerAccount = null,
            transactionalType = transactionResponse.transactionalType
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
            date = transactionResponse.transactionDate ?: "",
            ownerName = null,
            ownerAccount = null,
            transactionalType = transactionResponse.transactionalType
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

        val groupedTransactions = transactionHistoryResponse.groupBy {
            LocalDate.parse(it.transactionDate.substring(0, 10)).toString()
        }.map { (date, transactions) ->
            TransactionGroup(
                date = date,
                transactions = transactions.map { transaction ->
                    Transaction(
                        accountId = transaction.from.accountId,
                        ownerName = transaction.from.ownerName,
                        ownerAccount = transaction.from.accountNumber,
                        type = transaction.type,
                        adminFee = 0,
                        amount = transaction.total.toInt(),
                        date = transaction.transactionDate,
                        isSaved = null,
                        note = "",
                        transactionDate = transaction.transactionDate,
                        transactionId = transaction.transactionId,
                        transactionalType = transaction.transactionalType,
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