package com.example.data.repository

import android.util.Log
import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.utils.SafeApiRequest
import com.example.domain.model.BankAccount
import com.example.domain.model.PinValidation
import com.example.domain.repository.BankAccountRepository
import kotlinx.coroutines.flow.firstOrNull

class BankAccountRepositoryImpl(
    private val apiService : ApiService,
    private val dataStorePref: DataStorePref
) : BankAccountRepository, SafeApiRequest() {


    override suspend fun getBankAccountById(): BankAccount {
        val userId = dataStorePref.userId.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()

        if (userId == null) {
            throw Exception("User ID not found")
        } else {
            Log.d("UserRepositoryImpl", "User ID found: $userId")
        }

        val response = safeApiRequest {
            apiService.getBankAccountById(userId, "Bearer $token")
        }

        val bankAccountResponse = response.data ?: throw Exception("Bank account not found")
        return BankAccount(
            accountNumber = bankAccountResponse.accountNumber,
            userId = bankAccountResponse.accountId,
            balance = bankAccountResponse.balance,
            ownerName = bankAccountResponse.ownerName
        )
    }

//    override suspend fun getBankAccountByAccountNumber(accountNumber: String): BankAccount {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getAllBankAccount(): BankAccount {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getPinValidation(pinValidation: PinValidation): BankAccount {
//        TODO("Not yet implemented")
//    }
}