package com.example.data.repository

import android.util.Log
import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.model.auth.AuthRequest
import com.example.data.network.model.bank_account.PinRequest
import com.example.domain.model.Auth
import com.example.domain.model.BankAccount
import com.example.domain.model.Login
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull

class AuthRepositoryImpl(
    apiService: ApiService,
    dataStorePref: DataStorePref
) : BaseRepository(apiService, dataStorePref), AuthRepository {

    override suspend fun login(auth: Auth): Login {
        val response = safeApiRequest {
            apiService.loginAuth(AuthRequest(auth.username, auth.password))
        }

        val loginResponse = response.data ?: throw Exception(response.message)

        Log.d("AuthRepositoryImpl", "Storing login data")
        dataStorePref.storeLoginData(
            accessToken = loginResponse.accessToken,
            userId = loginResponse.userId,
            tokenType = loginResponse.tokenType,
            refreshToken = loginResponse.refreshToken
        ).collect { success ->
            if (success) {
                Log.d("AuthRepositoryImpl", "Login data stored successfully")
            } else {
                Log.e("AuthRepositoryImpl", "Failed to store login data")
            }
        }

        return loginResponse.toDomain()
    }

    override suspend fun pinValidation(pin: String): BankAccount {
        val accountNumber = dataStorePref.accountNumber.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()

        if (accountNumber == null) {
            throw Exception("Account number not found")
        }

        val response = performRequestWithTokenHandling {
            apiService.pinValidation(
                PinRequest(pin = pin, accountNumber = accountNumber),
                "Bearer $token"
            )
        }

        val pinValidationResponse = response.data ?: throw Exception(response.message)
        return BankAccount(
            accountId = pinValidationResponse.accountId,
            accountNumber = pinValidationResponse.accountNumber,
            ownerName = pinValidationResponse.ownerName,
            balance = pinValidationResponse.balance,
            userId = null
        )
    }
}
