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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class AuthRepositoryImpl(
    apiService: ApiService,
    dataStorePref: DataStorePref
) : BaseRepository(apiService, dataStorePref), AuthRepository {

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }

    override suspend fun resetPassword(email: String, otp: String, newPassword: String): String {
        val response = safeApiRequest {
            val requestBody = mapOf(
                "email_address" to email,
                "otp" to otp,
                "newPassword" to newPassword
            )
            apiService.resetPassword(requestBody)
        }
        when (response.code()) {
            400 -> throw Exception("OTP salah")
            404 -> throw Exception("Email tidak ditemukan")
            500 -> throw Exception("Server bermasalah, coba lagi nanti")
        }
        val resetPasswordResponse = response.body()?.data ?: throw Exception(response.message())
        return resetPasswordResponse
    }

    override suspend fun validateOTP(otp: String): String {
        val response = safeApiRequest {
            apiService.validateOTP(otp)
        }
        when (response.code()) {
            400 -> throw Exception("OTP salah")
            500 -> throw Exception("Server bermasalah, coba lagi nanti")
        }

        val validateOTPResponse = response.body()?.data ?: throw Exception(response.message())
        return validateOTPResponse
    }
    override suspend fun forgotPassword(email: String) : String {
        val response = safeApiRequest {
            apiService.forgotPassword(email)
        }

        when (response.code()){
            404 -> throw Exception("Email belum terdaftar")
            500 -> throw Exception("Server bermasalah, coba lagi nanti")
        }

        val forgotPasswordResponse = response.body()?.data ?: throw Exception(response.message())

        return forgotPasswordResponse
    }


    override suspend fun login(auth: Auth): Login {
        val response = safeApiRequest {
            apiService.loginAuth(AuthRequest(auth.username, auth.password))
        }

//        val errorMessage = handleResponseCodes(response)
//        if (errorMessage != null) {
//            throw Exception(errorMessage)
//        }

        when (response.code()) {
            400, 404 -> throw Exception("Username dan password salah, silahkan coba lagi")
            500 -> throw Exception("Server bermasalah, coba lagi nanti")
        }

        val loginResponse = response.body()?.data ?: throw Exception(response.message())

        Log.d(TAG, "Storing login data")
        dataStorePref.storeLoginData(
            accessToken = loginResponse.accessToken,
            userId = loginResponse.userId,
            tokenType = loginResponse.tokenType,
            refreshToken = loginResponse.refreshToken
        ).collect { success ->
            if (success) {
                Log.d(TAG, "Login data stored successfully")
            } else {
                Log.e(TAG, "Failed to store login data")
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
                it // Pass the token dynamically
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

    override suspend fun logout() {
        dataStorePref.clearAllData()
    }

    override suspend fun getLoginStatus(): Boolean {
        return dataStorePref.isLogin.first()
    }
}
