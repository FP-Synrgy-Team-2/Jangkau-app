package com.example.data.repository

import android.util.Log
import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.model.auth.AuthRequest
import com.example.data.network.model.bank_account.PinRequest
import com.example.data.network.utils.SafeApiRequest
import com.example.domain.model.Auth
import com.example.domain.model.BankAccount
import com.example.domain.model.Login
import com.example.domain.model.PinValidation
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val dataStorePref: DataStorePref
) : AuthRepository, SafeApiRequest() {

    override suspend fun login(auth: Auth): Login {
        val response = safeApiRequest {
            apiService.loginAuth(
                authRequest = AuthRequest(auth.username, auth.password)
            )
        }

        val loginResponse = response.data ?: throw Exception(response.message)

        Log.d("AuthRepositoryImpl", "Storing login data")
        dataStorePref.storeLoginData(
            accessToken = loginResponse.accessToken,
            userId = loginResponse.userId,
            tokenType = loginResponse.tokenType
        ).collect { success ->
            if (success) {
                Log.d("AuthRepositoryImpl", "Login data stored successfully")
            } else {
                Log.e("AuthRepositoryImpl", "Failed to store login data")
            }
        }

        return loginResponse.toDomain()
    }

    override suspend fun pinValidation(pin : String): BankAccount {
        val accountNumber = dataStorePref.accountNumber.firstOrNull()
        val token = dataStorePref.accessToken.firstOrNull()
        val response = safeApiRequest {
            apiService.pinValidation(
                PinRequest(
                    pin = pin,
                    accountNumber = accountNumber.toString()
                ),
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
