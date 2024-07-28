package com.example.data.repository

import android.util.Log
import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.utils.SafeApiRequest
import com.example.domain.model.BankAccount
import com.example.domain.repository.BankAccountRepository
import kotlinx.coroutines.flow.firstOrNull

class BankAccountRepositoryImpl(
    apiService: ApiService,
    dataStorePref: DataStorePref
) : BaseRepository(apiService, dataStorePref), BankAccountRepository {

    companion object {
        private const val TAG = "BankAccountRepoImpl"
    }

    override suspend fun getBankAccountById(): BankAccount {
        val (userId, token) = getUserCredentials()

        val response = performRequestWithTokenHandling {
            apiService.getBankAccountById(userId, "Bearer $token")
        }

        val bankAccountResponse = response.data ?: throw Exception("Bank account not found")

        Log.d(TAG, "Storing Account Data")
        dataStorePref.storeBankAccountData(
            accountNumber = bankAccountResponse.accountNumber,
            accountId = bankAccountResponse.accountId
        ).collect { success ->
            if (success) {
                Log.d(TAG, "Account Data Stored")
            } else {
                Log.d(TAG, "Account Data Not Stored")
            }
        }

        return BankAccount(
            accountNumber = bankAccountResponse.accountNumber,
            accountId = bankAccountResponse.accountId,
            userId = userId,
            balance = bankAccountResponse.balance,
            ownerName = bankAccountResponse.ownerName
        )
    }

    override suspend fun getBankAccountByAccountNumber(accountNumber: String): BankAccount {
        val (userId, token) = getUserCredentials()

        val response = performRequestWithTokenHandling {
            apiService.getBankAccountByAccountNumber(accountNumber, "Bearer $token")
        }

        val bankAccountResponse = response.data ?: throw Exception("Bank account not found")
        return bankAccountResponse.toDomain()
    }

    override suspend fun getSavedBankAccount(): List<BankAccount> {
        val (userId, token) = getUserCredentials()

        val response = performRequestWithTokenHandling {
            apiService.getSavedBankAccount(userId, "Bearer $token")
        }

        val bankAccountListResponse = response.data
        return bankAccountListResponse?.map {
            BankAccount(
                accountId = it.accountId,
                accountNumber = it.accountNumber,
                ownerName = it.ownerName,
                balance = null,
                userId = null
            )
        } ?: emptyList()
    }

    private suspend fun getUserCredentials(): Pair<String, String> {
        val userId = dataStorePref.userId.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()

        if (userId == null) {
            Log.e(TAG, "User ID not found")
            throw Exception("User ID not found in Impl")
        }

        if (token == null) {
            Log.e(TAG, "Access token not found")
            throw Exception("Access token not found")
        }

        return Pair(userId, token)
    }
}
